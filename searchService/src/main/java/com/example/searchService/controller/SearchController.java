package com.example.searchService.controller;

import com.example.searchService.dto.SearchResponse;
import com.example.searchService.entity.DocumentType;
import com.example.searchService.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<SearchResponse>> search(

            @RequestParam Long organizationId,

            @RequestParam(required = false)
            String query,

            @RequestParam(required = false)
            DocumentType documentType,

            @RequestParam(required = false)
            Long uploadedByUserId) {

        List<SearchResponse> results =
                searchService.search(
                        organizationId,
                        query,
                        documentType,
                        uploadedByUserId
                );

        return ResponseEntity.ok(results);
    }
}

