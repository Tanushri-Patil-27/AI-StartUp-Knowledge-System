package com.ai.document.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ai.document.model.Document;

public interface DocumentRepository
        extends MongoRepository<Document, String> {

    List<Document> findByOrganizationId(Long organizationId);

    List<Document> findByUploadedByUserId(Long uploadedByUserId);

    List<Document> findByOrganizationIdAndUploadedByUserId(
            Long organizationId,
            Long uploadedByUserId
    );
}