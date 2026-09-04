package com.ai.document.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ai.document.dto.OrganizationMemberAccessResponse;

@FeignClient(name = "organization-service")
public interface OrganizationClient {

    @GetMapping(
            "/api/organizations/{organizationId}/members/{userId}/access"
    )
    OrganizationMemberAccessResponse checkMemberAccess(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("userId") Long userId
    );
}