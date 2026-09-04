package com.ai.document.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.document.dto.DocumentResponse;
import com.ai.document.model.Document;
import com.ai.document.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByOrganization(
            @PathVariable Long organizationId) {

        List<Document> documents =
                documentService.getDocumentsByOrganization(organizationId);

        List<DocumentResponse> response =
                documents.stream()
                        .map(DocumentResponse::fromDocument)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> getDocumentById(
            @PathVariable String documentId) {

        Document document =
                documentService.getDocumentById(documentId);

        return ResponseEntity.ok(
                DocumentResponse.fromDocument(document)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByUser(
            @PathVariable Long userId) {

        List<Document> documents =
                documentService.getDocumentsByUser(userId);

        List<DocumentResponse> response =
                documents.stream()
                        .map(DocumentResponse::fromDocument)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String documentId) {

        documentService.deleteDocument(documentId);

        return ResponseEntity.noContent().build();
    }
}