package com.example.searchService.service;

import com.example.searchService.dto.SearchResponse;
import com.example.searchService.entity.DocumentType;

import java.util.List;

public interface SearchService {
    List<SearchResponse> search(
            Long organizationId,
            String query,
            DocumentType documentType,
            Long uploadedByUserId );
}
