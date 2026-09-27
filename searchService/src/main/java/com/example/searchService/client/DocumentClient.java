package com.example.searchService.client;


import com.example.searchService.config.FeignConfig;
import com.example.searchService.dto.DocumentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "document-service",
        configuration = FeignConfig.class
)
public interface DocumentClient {

    @GetMapping("/api/documents/organization/{organizationId}")
    List<DocumentResponse> getDocumentsByOrganization(
            @PathVariable("organizationId") Long organizationId
    );
}


