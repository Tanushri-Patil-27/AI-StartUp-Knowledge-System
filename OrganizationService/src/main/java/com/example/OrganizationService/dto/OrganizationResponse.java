package com.example.OrganizationService.dto;

import java.time.LocalDateTime;

public record OrganizationResponse(

        Long id,

        String name,

        String slug,

        String description,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
