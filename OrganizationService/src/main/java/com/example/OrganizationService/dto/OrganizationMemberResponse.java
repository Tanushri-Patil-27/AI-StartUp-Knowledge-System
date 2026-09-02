package com.example.OrganizationService.dto;


import com.example.OrganizationService.model.OrganizationRole;

import java.time.LocalDateTime;

public record OrganizationMemberResponse(

        Long id,

        Long userId,

        String name,

        String email,

        OrganizationRole role,

        LocalDateTime joinedAt
) {
}
