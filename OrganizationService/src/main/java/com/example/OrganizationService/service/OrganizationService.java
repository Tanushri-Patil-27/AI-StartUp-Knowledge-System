package com.example.OrganizationService.service;

import com.example.OrganizationService.dto.*;

import java.util.List;

public interface OrganizationService {
    OrganizationResponse createOrganization(
            CreateOrganizationRequest request,
            Long currentUserId
    );

    OrganizationResponse getOrganization(
            Long organizationId,
            Long currentUserId
    );

    List<OrganizationResponse> getMyOrganizations(
            Long currentUserId
    );

    OrganizationResponse updateOrganization(
            Long organizationId,
            UpdateOrganizationRequest request,
            Long currentUserId
    );

    void deleteOrganization(
            Long organizationId,
            Long currentUserId
    );

    OrganizationMemberResponse addMember(
            Long organizationId,
            AddMemberRequest request,
            Long currentUserId
    );

    List<OrganizationMemberResponse> getMembers(
            Long organizationId,
            Long currentUserId
    );

    OrganizationMemberResponse updateMemberRole(
            Long organizationId,
            Long userId,
            UpdateMemberRoleRequest request,
            Long currentUserId
    );

    void removeMember(
            Long organizationId,
            Long userId,
            Long currentUserId
    );
}
