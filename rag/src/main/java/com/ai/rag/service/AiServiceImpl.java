package com.ai.rag.service;

import com.ai.rag.model.KnowledgeChunk;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    private final RetrievalService retrievalService;


    public AiServiceImpl(
            ChatClient.Builder chatClientBuilder,
            RetrievalService retrievalService) {

        this.chatClient =
                chatClientBuilder.build();

        this.retrievalService =
                retrievalService;
    }


    @Override
    public String askQuestion(
            String question,
            Long organizationId) {

        if (question == null || question.isBlank()) {

            throw new IllegalArgumentException(
                    "Question cannot be empty"
            );
        }


        if (organizationId == null) {

            throw new IllegalArgumentException(
                    "Organization ID is required"
            );
        }


        /*
         * Step 1:
         * Search Qdrant for relevant knowledge.
         */

        List<KnowledgeChunk> relevantChunks =
                retrievalService.retrieveRelevantKnowledge(
                        question,
                        organizationId
                );


        /*
         * Step 2:
         * If no relevant knowledge exists,
         * do not allow the AI to invent an answer.
         */

        if (relevantChunks.isEmpty()) {

            return "I could not find relevant information in your organization's knowledge base.";
        }


        /*
         * Step 3:
         * Build context from retrieved chunks.
         */

        String context =
                buildContext(relevantChunks);


        /*
         * Step 4:
         * Send question + retrieved context to LLM.
         */

        String systemPrompt = """
                You are an AI knowledge assistant for a startup.

                Your job is to answer questions using ONLY the
                provided knowledge context.

                Important rules:

                1. Do not invent information.
                2. Do not use knowledge outside the provided context.
                3. If the context does not contain enough information,
                   clearly say that the information is not available.
                4. Give a concise but useful answer.
                5. When possible, mention the source document.
                """;


        String userPrompt = """
                KNOWLEDGE CONTEXT:
                
                %s
                
                USER QUESTION:
                
                %s
                
                Answer the question using only the knowledge context.
                """.formatted(
                context,
                question
        );


        /*
         * Step 5:
         * Generate final answer.
         */

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }


    private String buildContext(
            List<KnowledgeChunk> chunks) {

        StringBuilder context =
                new StringBuilder();


        for (KnowledgeChunk chunk : chunks) {

            context.append("\n");
            context.append("----- SOURCE -----\n");

            context.append("Document: ")
                    .append(
                            chunk.getSourceFileName()
                    )
                    .append("\n");

            context.append("Document ID: ")
                    .append(
                            chunk.getDocumentId()
                    )
                    .append("\n");

            context.append("Chunk: ")
                    .append(
                            chunk.getChunkIndex()
                    )
                    .append("\n");

            context.append("\n");

            context.append(
                    chunk.getContent()
            );

            context.append("\n");
            context.append("------------------\n");
        }


        return context.toString();
    }
}