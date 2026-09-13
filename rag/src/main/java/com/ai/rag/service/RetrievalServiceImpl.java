package com.ai.rag.service;

import com.ai.rag.model.KnowledgeChunk;
import com.ai.rag.repository.KnowledgeChunkRepository;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RetrievalServiceImpl implements RetrievalService {

    private final KnowledgeChunkRepository knowledgeChunkRepository;


    private static final int DEFAULT_TOP_K = 5;

    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.70;


    public RetrievalServiceImpl(
            KnowledgeChunkRepository knowledgeChunkRepository) {

        this.knowledgeChunkRepository =
                knowledgeChunkRepository;
    }


    @Override
    public void indexKnowledge(List<KnowledgeChunk> chunks) {

        if (chunks == null || chunks.isEmpty()) {
            return;
        }

        knowledgeChunkRepository.saveAll(chunks);
    }


    @Override
    public List<KnowledgeChunk> retrieveRelevantKnowledge(
            String question,
            Long organizationId) {

        return retrieveRelevantKnowledge(
                question,
                organizationId,
                DEFAULT_TOP_K,
                DEFAULT_SIMILARITY_THRESHOLD
        );
    }


    @Override
    public List<KnowledgeChunk> retrieveRelevantKnowledge(
            String question,
            Long organizationId,
            int topK,
            double similarityThreshold) {

        if (question == null || question.isBlank()) {
            return List.of();
        }

        if (organizationId == null) {
            throw new IllegalArgumentException(
                    "Organization ID is required"
            );
        }


        List<Document> documents =
                knowledgeChunkRepository.similaritySearch(
                        question,
                        organizationId,
                        topK,
                        similarityThreshold
                );


        List<KnowledgeChunk> chunks =
                new ArrayList<>();


        for (Document document : documents) {

            Map<String, Object> metadata =
                    document.getMetadata();


            KnowledgeChunk chunk =
                    new KnowledgeChunk();

            chunk.setId(document.getId());

            chunk.setOrganizationId(
                    toLong(metadata.get("organizationId"))
            );

            chunk.setDocumentId(
                    toStringValue(
                            metadata.get("documentId")
                    )
            );

            chunk.setUploadedByUserId(
                    toLong(
                            metadata.get("uploadedByUserId")
                    )
            );

            chunk.setChunkIndex(
                    toInteger(
                            metadata.get("chunkIndex")
                    )
            );

            chunk.setSourceFileName(
                    toStringValue(
                            metadata.get("sourceFileName")
                    )
            );

            chunk.setKnowledgeSource(
                    toStringValue(
                            metadata.get("knowledgeSource")
                    )
            );

            chunk.setContent(
                    document.getText()
            );

            chunk.setCreatedAt(
                    LocalDateTime.now()
            );


            chunks.add(chunk);
        }


        return chunks;
    }


    @Override
    public void deleteDocumentKnowledge(
            String documentId) {

        if (documentId == null || documentId.isBlank()) {
            return;
        }

        knowledgeChunkRepository
                .deleteByDocumentId(documentId);
    }


    private Long toLong(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.longValue();
        }

        return Long.parseLong(
                value.toString()
        );
    }


    private Integer toInteger(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        return Integer.parseInt(
                value.toString()
        );
    }


    private String toStringValue(Object value) {

        return value == null
                ? null
                : value.toString();
    }
}