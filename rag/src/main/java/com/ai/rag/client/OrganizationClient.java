package com.ai.rag.client;

import com.ai.rag.dto.OrganizationMemberAccessResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "organization-service")
public interface OrganizationClient {

    @GetMapping(
        "/api/organizations/{organizationId}/members/access/{userId}"
    )
    OrganizationMemberAccessResponse checkMemberAccess(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("userId") Long userId
    );
}