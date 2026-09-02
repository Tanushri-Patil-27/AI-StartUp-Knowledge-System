package com.example.OrganizationService.dto;


import com.example.OrganizationService.model.OrganizationRole;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Role is required")
        OrganizationRole role
) {
}
