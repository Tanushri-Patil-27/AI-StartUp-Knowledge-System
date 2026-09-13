package com.ai.rag.service;

import com.ai.rag.model.KnowledgeChunk;

import java.util.List;

public interface RetrievalService {

    void indexKnowledge(List<KnowledgeChunk> chunks);

    List<KnowledgeChunk> retrieveRelevantKnowledge(
            String question,
            Long organizationId
    );

    List<KnowledgeChunk> retrieveRelevantKnowledge(
            String question,
            Long organizationId,
            int topK,
            double similarityThreshold
    );

    void deleteDocumentKnowledge(String documentId);
}