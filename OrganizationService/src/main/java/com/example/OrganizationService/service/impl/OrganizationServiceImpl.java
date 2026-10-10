package com.example.OrganizationService.service.impl;

import com.example.OrganizationService.dto.CreateOrganizationRequest;
import com.example.OrganizationService.dto.OrganizationResponse;
import com.example.OrganizationService.dto.UpdateOrganizationRequest;
import com.example.OrganizationService.exception.BadRequestException;
import com.example.OrganizationService.exception.ResourceNotFoundException;
import com.example.OrganizationService.model.Organization;
import com.example.OrganizationService.repository.OrganizationRepository;
import com.example.OrganizationService.security.JwtUserPrincipal;
import com.example.OrganizationService.service.OrganizationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl
        implements OrganizationService {

    private final OrganizationRepository organizationRepository;


    // =========================================================
    // CREATE ORGANIZATION
    // =========================================================

    @Override
    public OrganizationResponse createOrganization(
            CreateOrganizationRequest request) {

        JwtUserPrincipal user =
                getCurrentUser();

        /*
         * Only ADMIN can create an organization.
         */
        if (!"ADMIN".equals(user.getRole())) {

            throw new BadRequestException(
                    "Only ADMIN can create an organization"
            );
        }

        /*
         * For the current single-organization architecture,
         * prevent multiple organizations.
         */
        if (organizationRepository.count() > 0) {

            throw new BadRequestException(
                    "Organization already exists"
            );
        }

        if (organizationRepository.existsByName(
                request.name())) {

            throw new BadRequestException(
                    "Organization name already exists"
            );
        }

        String slug =
                generateUniqueSlug(request.name());

        Organization organization =
                Organization.builder()
                        .name(request.name())
                        .slug(slug)
                        .description(request.description())
                        .build();

        organization =
                organizationRepository.save(
                        organization
                );

        return mapToResponse(organization);
    }


    // =========================================================
    // GET ORGANIZATION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganization(
            Long organizationId) {

        JwtUserPrincipal user =
                getCurrentUser();

        validateOrganizationAccess(
                organizationId,
                user
        );

        Organization organization =
                getOrganizationOrThrow(
                        organizationId
                );

        return mapToResponse(organization);
    }


    // =========================================================
    // UPDATE ORGANIZATION
    // =========================================================

    @Override
    public OrganizationResponse updateOrganization(
            Long organizationId,
            UpdateOrganizationRequest request) {

        JwtUserPrincipal user =
                getCurrentUser();

        validateOrganizationAccess(
                organizationId,
                user
        );

        /*
         * Only ADMIN can update organization details.
         */
        if (!"ADMIN".equals(user.getRole())) {

            throw new BadRequestException(
                    "Only ADMIN can update the organization"
            );
        }

        Organization organization =
                getOrganizationOrThrow(
                        organizationId
                );

        /*
         * Check duplicate organization name.
         */
        if (!organization.getName()
                .equals(request.name())
                && organizationRepository.existsByName(
                        request.name())) {

            throw new BadRequestException(
                    "Organization name already exists"
            );
        }

        organization.setName(
                request.name()
        );

        organization.setDescription(
                request.description()
        );

        /*
         * Keep slug unchanged when organization
         * name changes.
         *
         * This prevents breaking URLs or references
         * based on the existing slug.
         */

        organizationRepository.save(
                organization
        );

        return mapToResponse(organization);
    }


    // =========================================================
    // DELETE ORGANIZATION
    // =========================================================

    @Override
    public void deleteOrganization(
            Long organizationId) {

        JwtUserPrincipal user =
                getCurrentUser();

        validateOrganizationAccess(
                organizationId,
                user
        );

        /*
         * Only ADMIN can delete the organization.
         */
        if (!"ADMIN".equals(user.getRole())) {

            throw new BadRequestException(
                    "Only ADMIN can delete the organization"
            );
        }

        getOrganizationOrThrow(
                organizationId
        );

        organizationRepository.deleteById(
                organizationId
        );
    }


    // =========================================================
    // GET CURRENT USER
    // =========================================================

    private JwtUserPrincipal getCurrentUser() {

        Object principal =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        if (!(principal instanceof JwtUserPrincipal)) {

            throw new BadRequestException(
                    "Invalid authentication"
            );
        }

        return (JwtUserPrincipal) principal;
    }


    // =========================================================
    // ORGANIZATION ACCESS VALIDATION
    // =========================================================

    private void validateOrganizationAccess(
            Long organizationId,
            JwtUserPrincipal user) {

        if (!organizationId.equals(
                user.getOrganizationId())) {

            throw new BadRequestException(
                    "You do not have access to this organization"
            );
        }
    }


    // =========================================================
    // FIND ORGANIZATION
    // =========================================================

    private Organization getOrganizationOrThrow(
            Long organizationId) {

        return organizationRepository
                .findById(organizationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization not found with id: "
                                        + organizationId
                        )
                );
    }


    // =========================================================
    // SLUG GENERATION
    // =========================================================

    private String generateUniqueSlug(
            String name) {

        String baseSlug =
                name
                        .toLowerCase(Locale.ROOT)
                        .trim()
                        .replaceAll(
                                "[^a-z0-9]+",
                                "-"
                        )
                        .replaceAll(
                                "(^-|-$)",
                                ""
                        );

        String slug = baseSlug;

        int counter = 1;

        while (organizationRepository
                .existsBySlug(slug)) {

            slug =
                    baseSlug
                            + "-"
                            + counter;

            counter++;
        }

        return slug;
    }


    // =========================================================
    // MAP RESPONSE
    // =========================================================

    private OrganizationResponse mapToResponse(
            Organization organization) {

        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                organization.getDescription(),
                organization.getCreatedAt(),
                organization.getUpdatedAt()
        );
    }
}