package com.example.searchService.dto;


import com.example.searchService.entity.DocumentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SearchResponse {

    private String id;

    private Long organizationId;

    private Long uploadedByUserId;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String s3Url;

    private DocumentType documentType;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
