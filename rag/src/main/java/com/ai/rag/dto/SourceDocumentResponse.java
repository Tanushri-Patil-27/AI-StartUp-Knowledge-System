package com.ai.rag.dto;

public class SourceDocumentResponse {

    private String documentId;

    private Long organizationId;

    private Long uploadedByUserId;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String documentType;

    private String description;

    private String s3Url;


    public SourceDocumentResponse() {
    }


    public SourceDocumentResponse(
            String documentId,
            Long organizationId,
            Long uploadedByUserId,
            String fileName,
            String fileType,
            Long fileSize,
            String documentType,
            String description,
            String s3Url) {

        this.documentId = documentId;
        this.organizationId = organizationId;
        this.uploadedByUserId = uploadedByUserId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.documentType = documentType;
        this.description = description;
        this.s3Url = s3Url;
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


    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }
}