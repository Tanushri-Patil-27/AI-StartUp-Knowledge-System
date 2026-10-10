package com.example.OrganizationService.service;

import com.example.OrganizationService.dto.CreateOrganizationRequest;
import com.example.OrganizationService.dto.OrganizationResponse;
import com.example.OrganizationService.dto.UpdateOrganizationRequest;

public interface OrganizationService {

    OrganizationResponse createOrganization(
            CreateOrganizationRequest request
    );

    OrganizationResponse getOrganization(
            Long organizationId
    );

    OrganizationResponse updateOrganization(
            Long organizationId,
            UpdateOrganizationRequest request
    );

    void deleteOrganization(
            Long organizationId
    );
}