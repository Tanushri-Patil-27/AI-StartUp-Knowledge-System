package com.example.OrganizationService.service.impl;

import com.example.OrganizationService.dto.*;
import com.example.OrganizationService.exception.BadRequestException;
import com.example.OrganizationService.exception.ResourceNotFoundException;
import com.example.OrganizationService.model.Organization;
import com.example.OrganizationService.model.OrganizationMember;
import com.example.OrganizationService.model.OrganizationRole;
import com.example.OrganizationService.repository.OrganizationMemberRepository;
import com.example.OrganizationService.repository.OrganizationRepository;
import com.example.OrganizationService.service.OrganizationService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository memberRepository;


    @Override
    public OrganizationResponse createOrganization(
            CreateOrganizationRequest request,
            Long currentUserId) {

        if (organizationRepository.existsByName(request.name())) {
            throw new BadRequestException(
                    "Organization name already exists");
        }

        String slug = generateUniqueSlug(request.name());

        Organization organization = Organization.builder()
                .name(request.name())
                .slug(slug)
                .description(request.description())
                .build();

        organization = organizationRepository.save(organization);

        OrganizationMember owner = OrganizationMember.builder()
                .organizationId(organization.getId())
                .userId(currentUserId)
                .role(OrganizationRole.OWNER)
                .build();

        memberRepository.save(owner);

        return mapToResponse(organization);
    }


    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganization(
            Long organizationId,
            Long currentUserId) {

        getAuthorizedMember(organizationId, currentUserId);

        Organization organization =
                getOrganizationOrThrow(organizationId);

        return mapToResponse(organization);
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getMyOrganizations(
            Long currentUserId) {

        List<OrganizationMember> memberships =
                memberRepository.findByUserId(currentUserId);

        return memberships.stream()
                .map(member ->
                        getOrganizationOrThrow(
                                member.getOrganizationId()))
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public OrganizationResponse updateOrganization(
            Long organizationId,
            UpdateOrganizationRequest request,
            Long currentUserId) {

        OrganizationMember member =
                getAuthorizedMember(
                        organizationId,
                        currentUserId);

        checkAdminOrOwner(member);

        Organization organization =
                getOrganizationOrThrow(organizationId);

        if (!organization.getName().equals(request.name())
                && organizationRepository.existsByName(request.name())) {

            throw new BadRequestException(
                    "Organization name already exists");
        }

        organization.setName(request.name());
        organization.setDescription(request.description());

        organizationRepository.save(organization);

        return mapToResponse(organization);
    }


    @Override
    public void deleteOrganization(
            Long organizationId,
            Long currentUserId) {

        OrganizationMember member =
                getAuthorizedMember(
                        organizationId,
                        currentUserId);

        if (member.getRole() != OrganizationRole.OWNER) {
            throw new BadRequestException(
                    "Only organization owner can delete the organization");
        }

        getOrganizationOrThrow(organizationId);

        // Delete memberships first
        memberRepository.deleteAll(
                memberRepository.findByOrganizationId(
                        organizationId)
        );

        organizationRepository.deleteById(organizationId);
    }


    @Override
    public OrganizationMemberResponse addMember(
            Long organizationId,
            AddMemberRequest request,
            Long currentUserId) {

        OrganizationMember currentMember =
                getAuthorizedMember(
                        organizationId,
                        currentUserId);

        checkAdminOrOwner(currentMember);

        getOrganizationOrThrow(organizationId);

        // OWNER cannot be assigned to a new member
        if (request.role() == OrganizationRole.OWNER) {
            throw new BadRequestException(
                    "OWNER role cannot be assigned to a new member");
        }

        // Check whether the user is already a member
        if (memberRepository.existsByOrganizationIdAndUserId(
                organizationId,
                request.userId())) {

            throw new BadRequestException(
                    "User is already a member of this organization");
        }

        /*
         * User validation is temporarily removed.
         *
         * OrganizationService is currently independent from UserService.
         * We only store the userId.
         *
         * Later, when OpenFeign + Eureka are added, you can validate
         * the user through UserService here.
         */

        OrganizationMember member =
                OrganizationMember.builder()
                        .organizationId(organizationId)
                        .userId(request.userId())
                        .role(request.role())
                        .build();

        member = memberRepository.save(member);

        /*
         * Since UserService is not connected yet,
         * we cannot return user name/email.
         *
         * So return only information available in this service.
         */
        return new OrganizationMemberResponse(
                member.getId(),
                member.getUserId(),
                null,
                null,
                member.getRole(),
                member.getJoinedAt()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMemberResponse> getMembers(
            Long organizationId,
            Long currentUserId) {

        getAuthorizedMember(
                organizationId,
                currentUserId);

        List<OrganizationMember> members =
                memberRepository.findByOrganizationId(
                        organizationId);

        return members.stream()
                .map(this::mapMemberToResponse)
                .toList();
    }


    @Override
    public OrganizationMemberResponse updateMemberRole(
            Long organizationId,
            Long userId,
            UpdateMemberRoleRequest request,
            Long currentUserId) {

        OrganizationMember currentMember =
                getAuthorizedMember(
                        organizationId,
                        currentUserId);

        checkAdminOrOwner(currentMember);

        OrganizationMember member =
                memberRepository
                        .findByOrganizationIdAndUserId(
                                organizationId,
                                userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization member not found"));

        if (member.getRole() == OrganizationRole.OWNER) {
            throw new BadRequestException(
                    "Owner role cannot be changed");
        }

        if (request.role() == OrganizationRole.OWNER) {
            throw new BadRequestException(
                    "Cannot assign OWNER role");
        }

        member.setRole(request.role());

        memberRepository.save(member);

        return mapMemberToResponse(member);
    }


    @Override
    public void removeMember(
            Long organizationId,
            Long userId,
            Long currentUserId) {

        OrganizationMember currentMember =
                getAuthorizedMember(
                        organizationId,
                        currentUserId);

        checkAdminOrOwner(currentMember);

        OrganizationMember member =
                memberRepository
                        .findByOrganizationIdAndUserId(
                                organizationId,
                                userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization member not found"));

        if (member.getRole() == OrganizationRole.OWNER) {
            throw new BadRequestException(
                    "Organization owner cannot be removed");
        }

        memberRepository.delete(member);
    }


    private Organization getOrganizationOrThrow(
            Long organizationId) {

        return organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization not found with id: "
                                        + organizationId));
    }


    private OrganizationMember getAuthorizedMember(
            Long organizationId,
            Long userId) {

        return memberRepository
                .findByOrganizationIdAndUserId(
                        organizationId,
                        userId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "You are not a member of this organization"));
    }


    private void checkAdminOrOwner(
            OrganizationMember member) {

        if (member.getRole() != OrganizationRole.OWNER
                && member.getRole() != OrganizationRole.ADMIN) {

            throw new BadRequestException(
                    "Only OWNER or ADMIN can perform this operation");
        }
    }


    private String generateUniqueSlug(String name) {

        String baseSlug = name
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        String slug = baseSlug;
        int counter = 1;

        while (organizationRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }


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


    private OrganizationMemberResponse mapMemberToResponse(
            OrganizationMember member) {



        return new OrganizationMemberResponse(
                member.getId(),
                member.getUserId(),
                null,
                null,
                member.getRole(),
                member.getJoinedAt()
        );
    }
}
