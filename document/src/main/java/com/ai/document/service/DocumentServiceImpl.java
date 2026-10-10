package com.ai.document.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ai.document.exception.DocumentNotFoundException;
import com.ai.document.model.Document;
import com.ai.document.model.DocumentType;
import com.ai.document.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final S3StorageService s3StorageService;

    public DocumentServiceImpl(
            DocumentRepository documentRepository,
            S3StorageService s3StorageService) {

        this.documentRepository = documentRepository;
        this.s3StorageService = s3StorageService;
    }

    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @Override
    public Document getDocumentById(
            String documentId) {

        return documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new DocumentNotFoundException(
                                "Document not found with id: "
                                        + documentId
                        )
                );
    }

    // =========================================================
    // GET ORGANIZATION DOCUMENTS
    // =========================================================

    @Override
    public List<Document> getDocumentsByOrganization(
            Long organizationId) {

        return documentRepository
                .findByOrganizationId(
                        organizationId
                );
    }

    // =========================================================
    // GET USER DOCUMENTS
    // =========================================================

    @Override
    public List<Document> getDocumentsByUser(
            Long userId) {

        return documentRepository
                .findByUploadedByUserId(
                        userId
                );
    }

    // =========================================================
    // SAVE DOCUMENT
    // =========================================================

    @Override
    public Document saveDocument(
            Document document) {

        return documentRepository.save(
                document
        );
    }

    // =========================================================
    // UPLOAD DOCUMENT
    // =========================================================

    @Override
    public Document uploadDocument(
            MultipartFile file,
            DocumentType documentType,
            String description,
            Long userId,
            Long organizationId) throws Exception {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "File cannot be empty"
            );
        }

        // =====================================================
        // UPLOAD FILE TO S3
        // =====================================================

        String s3Url =
                s3StorageService.uploadFile(
                        file,
                        organizationId,
                        userId
                );

        // =====================================================
        // CREATE S3 KEY
        // =====================================================

        String s3Key =
                extractS3KeyFromUrl(
                        s3Url
                );

        // =====================================================
        // CREATE DOCUMENT
        // =====================================================

        Document document =
                new Document();

        document.setOrganizationId(
                organizationId
        );

        document.setUploadedByUserId(
                userId
        );

        document.setFileName(
                file.getOriginalFilename()
        );

        document.setFileType(
                file.getContentType()
        );

        document.setFileSize(
                file.getSize()
        );

        document.setS3Key(
                s3Key
        );

        document.setS3Url(
                s3Url
        );

        document.setDocumentType(
                documentType
        );

        document.setDescription(
                description
        );

        document.setCreatedAt(
                LocalDateTime.now()
        );

        document.setUpdatedAt(
                LocalDateTime.now()
        );

        // =====================================================
        // SAVE METADATA TO MONGODB
        // =====================================================

        return documentRepository.save(
                document
        );
    }

    // =========================================================
    // EXTRACT S3 KEY
    // =========================================================

    private String extractS3KeyFromUrl(
            String s3Url) {

        int index =
                s3Url.indexOf(".amazonaws.com/");

        if (index == -1) {
            return s3Url;
        }

        return s3Url.substring(
                index + ".amazonaws.com/".length()
        );
    }

    // =========================================================
    // DELETE DOCUMENT
    // =========================================================

    @Override
    public void deleteDocument(
            String documentId) {

        if (!documentRepository.existsById(
                documentId)) {

            throw new DocumentNotFoundException(
                    "Document not found with id: "
                            + documentId
            );
        }

        documentRepository.deleteById(
                documentId
        );
    }
}