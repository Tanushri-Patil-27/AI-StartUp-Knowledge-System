package com.ai.document.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ai.document.dto.DocumentResponse;
import com.ai.document.dto.DocumentUploadResponse;
import com.ai.document.model.Document;
import com.ai.document.model.DocumentType;
import com.ai.document.security.JwtUserPrincipal;
import com.ai.document.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(
            DocumentService documentService) {

        this.documentService = documentService;
    }

    // =========================================================
    // UPLOAD DOCUMENT
    // =========================================================

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,

            @RequestParam("documentType")
            DocumentType documentType,

            @RequestParam(value = "description", required = false)
            String description,

            Authentication authentication)
            throws Exception {

        JwtUserPrincipal principal =
                (JwtUserPrincipal)
                        authentication.getPrincipal();

        Long userId =
                principal.getUserId();

        Long organizationId =
                principal.getOrganizationId();

        // =====================================================
        // UPLOAD
        // =====================================================

        Document document =
                documentService.uploadDocument(
                        file,
                        documentType,
                        description,
                        userId,
                        organizationId
                );

        // =====================================================
        // RESPONSE
        // =====================================================

        DocumentUploadResponse response =
                new DocumentUploadResponse(
                        document.getId(),
                        document.getFileName(),
                        "Document uploaded successfully"
                );

        return ResponseEntity.ok(
                response
        );
    }

    // =========================================================
    // GET ORGANIZATION DOCUMENTS
    // =========================================================

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DocumentResponse>>
    getDocumentsByOrganization(
            @PathVariable Long organizationId) {

        List<Document> documents =
                documentService
                        .getDocumentsByOrganization(
                                organizationId
                        );

        List<DocumentResponse> response =
                documents.stream()
                        .map(DocumentResponse::fromDocument)
                        .toList();

        return ResponseEntity.ok(
                response
        );
    }

    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponse>
    getDocumentById(
            @PathVariable String documentId) {

        Document document =
                documentService.getDocumentById(
                        documentId
                );

        return ResponseEntity.ok(
                DocumentResponse.fromDocument(
                        document
                )
        );
    }

    // =========================================================
    // GET USER DOCUMENTS
    // =========================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentResponse>>
    getDocumentsByUser(
            @PathVariable Long userId) {

        List<Document> documents =
                documentService.getDocumentsByUser(
                        userId
                );

        List<DocumentResponse> response =
                documents.stream()
                        .map(DocumentResponse::fromDocument)
                        .toList();

        return ResponseEntity.ok(
                response
        );
    }

    // =========================================================
    // DELETE DOCUMENT
    // =========================================================

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String documentId) {

        documentService.deleteDocument(
                documentId
        );

        return ResponseEntity.noContent()
                .build();
    }
}