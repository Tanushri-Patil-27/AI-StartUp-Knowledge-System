package com.example.OrganizationService.dto;

import com.example.OrganizationService.model.OrganizationRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest (
    @NotNull(message = "Role is required")
    OrganizationRole role
            ){
}
