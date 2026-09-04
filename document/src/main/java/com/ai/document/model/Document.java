package com.ai.document.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {

    @Id
    private String id;

    private Long organizationId;
    private Long uploadedByUserId;

    private String fileName;
    private String fileType;
    private Long fileSize;

    private String s3Key;
    private String s3Url;

    private DocumentType documentType;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Document() {
    }

    public Document(
            String id,
            Long organizationId,
            Long uploadedByUserId,
            String fileName,
            String fileType,
            Long fileSize,
            String s3Key,
            String s3Url,
            DocumentType documentType,
            String description,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.uploadedByUserId = uploadedByUserId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.s3Key = s3Key;
        this.s3Url = s3Url;
        this.documentType = documentType;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Long getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(Long uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}