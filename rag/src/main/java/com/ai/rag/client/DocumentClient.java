package com.ai.rag.client;

import com.ai.rag.dto.SourceDocumentResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "document-service")
public interface DocumentClient {

    @GetMapping("/api/documents/{documentId}")
    SourceDocumentResponse getDocumentById(
            @PathVariable("documentId") String documentId
    );


    @GetMapping("/api/documents/organization/{organizationId}")
    List<SourceDocumentResponse> getDocumentsByOrganization(
            @PathVariable("organizationId") Long organizationId
    );


    @GetMapping("/api/documents/user/{userId}")
    List<SourceDocumentResponse> getDocumentsByUser(
            @PathVariable("userId") Long userId
    );
}