package com.ai.document.service;

import java.util.List;

import com.ai.document.model.Document;

public interface DocumentService {

    Document getDocumentById(String documentId);

    List<Document> getDocumentsByOrganization(Long organizationId);

    List<Document> getDocumentsByUser(Long userId);

    Document saveDocument(Document document);

    void deleteDocument(String documentId);
}