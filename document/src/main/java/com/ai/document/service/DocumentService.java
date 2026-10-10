package com.ai.document.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ai.document.model.Document;
import com.ai.document.model.DocumentType;

public interface DocumentService {

    Document getDocumentById(String documentId);

    List<Document> getDocumentsByOrganization(
            Long organizationId
    );

    List<Document> getDocumentsByUser(
            Long userId
    );

    Document saveDocument(
            Document document
    );

    Document uploadDocument(
            MultipartFile file,
            DocumentType documentType,
            String description,
            Long userId,
            Long organizationId
    ) throws Exception;

    void deleteDocument(
            String documentId
    );
}
