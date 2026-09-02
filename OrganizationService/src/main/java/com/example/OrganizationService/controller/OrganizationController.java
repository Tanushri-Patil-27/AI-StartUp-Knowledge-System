package com.example.OrganizationService.controller;

import com.example.OrganizationService.dto.CreateOrganizationRequest;
import com.example.OrganizationService.dto.OrganizationResponse;
import com.example.OrganizationService.dto.UpdateOrganizationRequest;
import com.example.OrganizationService.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(
            @Valid @RequestBody CreateOrganizationRequest request,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        OrganizationResponse response =
                organizationService.createOrganization(
                        request,
                        currentUserId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrganizationResponse>> getMyOrganizations(
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        return ResponseEntity.ok(
                organizationService.getMyOrganizations(currentUserId)
        );
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> getOrganization(
            @PathVariable Long organizationId,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        return ResponseEntity.ok(
                organizationService.getOrganization(
                        organizationId,
                        currentUserId
                )
        );
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @PathVariable Long organizationId,
            @Valid @RequestBody UpdateOrganizationRequest request,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        return ResponseEntity.ok(
                organizationService.updateOrganization(
                        organizationId,
                        request,
                        currentUserId
                )
        );
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(
            @PathVariable Long organizationId,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        organizationService.deleteOrganization(
                organizationId,
                currentUserId
        );

        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId(Authentication authentication) {
        return Long.valueOf(authentication.getName());
    }
}