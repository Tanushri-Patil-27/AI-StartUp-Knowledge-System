package com.example.searchService.service.impl;


import com.example.searchService.client.DocumentClient;
import com.example.searchService.dto.DocumentResponse;
import com.example.searchService.dto.SearchResponse;
import com.example.searchService.entity.DocumentType;
import com.example.searchService.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl
        implements SearchService {

    private final DocumentClient documentClient;

    @Override
    public List<SearchResponse> search(
            Long organizationId,
            String query,
            DocumentType documentType,
            Long uploadedByUserId) {

        List<DocumentResponse> documents =
                documentClient.getDocumentsByOrganization(
                        organizationId
                );

        String searchText =
                query == null
                        ? ""
                        : query.trim().toLowerCase();

        return documents.stream()

                // Search by filename or description
                .filter(document -> {

                    if (searchText.isBlank()) {
                        return true;
                    }

                    String fileName =
                            document.getFileName() == null
                                    ? ""
                                    : document.getFileName()
                                    .toLowerCase();

                    String description =
                            document.getDescription() == null
                                    ? ""
                                    : document.getDescription()
                                    .toLowerCase();

                    return fileName.contains(searchText)
                            || description.contains(searchText);
                })

                // Filter by document type
                .filter(document -> {

                    if (documentType == null) {
                        return true;
                    }

                    return document.getDocumentType()
                            == documentType;
                })

                // Filter by uploader
                .filter(document -> {

                    if (uploadedByUserId == null) {
                        return true;
                    }

                    return uploadedByUserId.equals(
                            document.getUploadedByUserId()
                    );
                })

                // Convert to SearchResponse
                .map(this::mapToResponse)

                .toList();
    }

    private SearchResponse mapToResponse(
            DocumentResponse document) {

        return SearchResponse.builder()

                .id(document.getId())

                .organizationId(
                        document.getOrganizationId()
                )

                .uploadedByUserId(
                        document.getUploadedByUserId()
                )

                .fileName(
                        document.getFileName()
                )

                .fileType(
                        document.getFileType()
                )

                .fileSize(
                        document.getFileSize()
                )

                .s3Url(
                        document.getS3Url()
                )

                .documentType(
                        document.getDocumentType()
                )

                .description(
                        document.getDescription()
                )

                .createdAt(
                        document.getCreatedAt()
                )

                .updatedAt(
                        document.getUpdatedAt()
                )

                .build();
    }
}
