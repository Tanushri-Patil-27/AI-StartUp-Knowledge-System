package com.ai.rag.repository;

import com.ai.rag.model.KnowledgeChunk;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class KnowledgeChunkRepository {

    private final VectorStore vectorStore;

    public KnowledgeChunkRepository(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Save one knowledge chunk into Qdrant.
     */
    public void save(KnowledgeChunk chunk) {

        Map<String, Object> metadata = new HashMap<>();

        metadata.put("organizationId", chunk.getOrganizationId());
        metadata.put("documentId", chunk.getDocumentId());
        metadata.put("uploadedByUserId", chunk.getUploadedByUserId());
        metadata.put("chunkIndex", chunk.getChunkIndex());
        metadata.put("sourceFileName", chunk.getSourceFileName());
        metadata.put("knowledgeSource", chunk.getKnowledgeSource());

        Document document = new Document(
                chunk.getId(),
                chunk.getContent(),
                metadata
        );

        vectorStore.add(List.of(document));
    }


    /**
     * Save multiple knowledge chunks into Qdrant.
     */
    public void saveAll(List<KnowledgeChunk> chunks) {

        if (chunks == null || chunks.isEmpty()) {
            return;
        }

        List<Document> documents = new ArrayList<>();

        for (KnowledgeChunk chunk : chunks) {

            Map<String, Object> metadata = new HashMap<>();

            metadata.put("organizationId", chunk.getOrganizationId());
            metadata.put("documentId", chunk.getDocumentId());
            metadata.put("uploadedByUserId", chunk.getUploadedByUserId());
            metadata.put("chunkIndex", chunk.getChunkIndex());
            metadata.put("sourceFileName", chunk.getSourceFileName());
            metadata.put("knowledgeSource", chunk.getKnowledgeSource());

            Document document = new Document(
                    chunk.getId(),
                    chunk.getContent(),
                    metadata
            );

            documents.add(document);
        }

        vectorStore.add(documents);
    }


    /**
     * Search Qdrant for similar knowledge.
     */
    public List<Document> similaritySearch(
            String query,
            Long organizationId,
            int topK,
            double similarityThreshold) {

        String filterExpression =
                "organizationId == " + organizationId;

        return vectorStore.similaritySearch(
                org.springframework.ai.vectorstore.SearchRequest
                        .builder()
                        .query(query)
                        .topK(topK)
                        .similarityThreshold(similarityThreshold)
                        .filterExpression(filterExpression)
                        .build()
        );
    }


    /**
     * Delete all chunks belonging to a document.
     */
    public void deleteByDocumentId(String documentId) {

        String filterExpression =
                "documentId == '" + escapeFilterValue(documentId) + "'";

        vectorStore.delete(filterExpression);
    }


    /**
     * Escape single quotes used inside Qdrant filter values.
     */
    private String escapeFilterValue(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("'", "\\'");
    }
}