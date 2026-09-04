package com.ai.document.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ai.document.exception.DocumentNotFoundException;
import com.ai.document.model.Document;
import com.ai.document.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document getDocumentById(String documentId) {

        return documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new DocumentNotFoundException(
                                "Document not found with id: " + documentId
                        )
                );
    }

    @Override
    public List<Document> getDocumentsByOrganization(Long organizationId) {

        return documentRepository.findByOrganizationId(organizationId);
    }

    @Override
    public List<Document> getDocumentsByUser(Long userId) {

        return documentRepository.findByUploadedByUserId(userId);
    }

    @Override
    public Document saveDocument(Document document) {

        return documentRepository.save(document);
    }

    @Override
    public void deleteDocument(String documentId) {

        if (!documentRepository.existsById(documentId)) {

            throw new DocumentNotFoundException(
                    "Document not found with id: " + documentId
            );
        }

        documentRepository.deleteById(documentId);
    }
}