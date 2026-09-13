package com.ai.rag.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class KnowledgeChunk {

    private String id;

    private Long organizationId;

    private String documentId;

    private Long uploadedByUserId;

    private String content;

    private Integer chunkIndex;

    private String sourceFileName;

    private String knowledgeSource;

    private Map<String, Object> metadata;

    private LocalDateTime createdAt;


    public KnowledgeChunk() {
        this.metadata = new HashMap<>();
    }


    public KnowledgeChunk(
            String id,
            Long organizationId,
            String documentId,
            Long uploadedByUserId,
            String content,
            Integer chunkIndex,
            String sourceFileName,
            String knowledgeSource,
            Map<String, Object> metadata,
            LocalDateTime createdAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.documentId = documentId;
        this.uploadedByUserId = uploadedByUserId;
        this.content = content;
        this.chunkIndex = chunkIndex;
        this.sourceFileName = sourceFileName;
        this.knowledgeSource = knowledgeSource;
        this.metadata = metadata;
        this.createdAt = createdAt;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public Long getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(Long uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }


    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }


    public String getKnowledgeSource() {
        return knowledgeSource;
    }

    public void setKnowledgeSource(String knowledgeSource) {
        this.knowledgeSource = knowledgeSource;
    }


    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}