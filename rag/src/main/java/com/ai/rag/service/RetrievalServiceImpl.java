
package com.ai.rag.service;

import com.ai.rag.model.KnowledgeChunk;
import com.ai.rag.repository.KnowledgeChunkRepository;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        this.knowledgeChunkRepository = knowledgeChunkRepository;
    }

    @Override
    @Transactional
    public void indexKnowledge(List<KnowledgeChunk> chunks) {

        if (chunks == null || chunks.isEmpty()) {
            return;
        }

        for (KnowledgeChunk chunk : chunks) {

            if (chunk == null) {
                throw new IllegalArgumentException(
                        "Knowledge chunk cannot be null"
                );
            }

            if (chunk.getOrganizationId() == null) {
                throw new IllegalArgumentException(
                        "Organization ID is required for every chunk"
                );
            }

            if (chunk.getContent() == null
                    || chunk.getContent().isBlank()) {
                throw new IllegalArgumentException(
                        "Knowledge chunk content cannot be empty"
                );
            }
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

        if (topK <= 0) {
            throw new IllegalArgumentException(
                    "topK must be greater than zero"
            );
        }

        if (Double.isNaN(similarityThreshold)
                || similarityThreshold < 0.0
                || similarityThreshold > 1.0) {
            throw new IllegalArgumentException(
                    "Similarity threshold must be between 0 and 1"
            );
        }

        List<Document> documents =
                knowledgeChunkRepository.similaritySearch(
                        question,
                        organizationId,
                        topK,
                        similarityThreshold
                );

        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        List<KnowledgeChunk> chunks = new ArrayList<>();

        for (Document document : documents) {

            if (document == null) {
                continue;
            }

            Map<String, Object> metadata = document.getMetadata();

            if (metadata == null) {
                continue;
            }

            Long chunkOrganizationId =
                    toLong(metadata.get("organizationId"));

            // Defense in depth: reject missing or mismatched tenant metadata.
            if (chunkOrganizationId == null
                    || !organizationId.equals(chunkOrganizationId)) {
                continue;
            }

            String content = document.getText();

            if (content == null || content.isBlank()) {
                continue;
            }

            KnowledgeChunk chunk = new KnowledgeChunk();

            chunk.setId(document.getId());
            chunk.setOrganizationId(chunkOrganizationId);

            chunk.setDocumentId(
                    toStringValue(metadata.get("documentId"))
            );

            chunk.setUploadedByUserId(
                    toLong(metadata.get("uploadedByUserId"))
            );

            chunk.setChunkIndex(
                    toInteger(metadata.get("chunkIndex"))
            );

            chunk.setSourceFileName(
                    toStringValue(metadata.get("sourceFileName"))
            );

            chunk.setKnowledgeSource(
                    toStringValue(metadata.get("knowledgeSource"))
            );

            chunk.setContent(content);

            // This is retrieval time, not the original indexing time.
            chunk.setCreatedAt(LocalDateTime.now());

            chunks.add(chunk);
        }

        return chunks;
    }

    @Override
    @Transactional
    public void deleteDocumentKnowledge(String documentId) {

        if (documentId == null || documentId.isBlank()) {
            throw new IllegalArgumentException(
                    "Document ID is required"
            );
        }

        knowledgeChunkRepository.deleteByDocumentId(documentId);
    }

    private Long toLong(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.longValue();
        }

        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private Integer toInteger(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String toStringValue(Object value) {

        return value == null ? null : value.toString();
    }
}
