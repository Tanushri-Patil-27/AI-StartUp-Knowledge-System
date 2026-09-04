package com.ai.document.dto;

public class DocumentUploadResponse {

    private String documentId;
    private String fileName;
    private String message;

    public DocumentUploadResponse() {
    }

    public DocumentUploadResponse(
            String documentId,
            String fileName,
            String message) {

        this.documentId = documentId;
        this.fileName = fileName;
        this.message = message;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}