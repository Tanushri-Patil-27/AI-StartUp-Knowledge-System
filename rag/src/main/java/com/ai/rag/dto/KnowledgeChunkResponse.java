package com.ai.rag.dto;

public class KnowledgeChunkResponse {

    private String chunkId;

    private String documentId;

    private Long organizationId;

    private Integer chunkIndex;

    private String content;

    private String sourceFileName;

    private String knowledgeSource;

    private Double similarityScore;


    public KnowledgeChunkResponse() {
    }


    public KnowledgeChunkResponse(
            String chunkId,
            String documentId,
            Long organizationId,
            Integer chunkIndex,
            String content,
            String sourceFileName,
            String knowledgeSource,
            Double similarityScore) {

        this.chunkId = chunkId;
        this.documentId = documentId;
        this.organizationId = organizationId;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.sourceFileName = sourceFileName;
        this.knowledgeSource = knowledgeSource;
        this.similarityScore = similarityScore;
    }


    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }


    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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


    public Double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(
            Double similarityScore) {

        this.similarityScore = similarityScore;
    }
}