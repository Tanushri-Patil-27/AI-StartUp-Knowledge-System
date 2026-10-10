package com.example.OrganizationService.controller;

import com.example.OrganizationService.dto.CreateOrganizationRequest;
import com.example.OrganizationService.dto.OrganizationResponse;
import com.example.OrganizationService.dto.UpdateOrganizationRequest;
import com.example.OrganizationService.service.OrganizationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;


    // =========================================================
    // CREATE ORGANIZATION
    // =========================================================

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(
            @Valid @RequestBody
            CreateOrganizationRequest request) {

        OrganizationResponse response =
                organizationService.createOrganization(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET ORGANIZATION
    // =========================================================

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> getOrganization(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                organizationService.getOrganization(
                        organizationId
                )
        );
    }


    // =========================================================
    // UPDATE ORGANIZATION
    // =========================================================

    @PutMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @PathVariable Long organizationId,

            @Valid
            @RequestBody
            UpdateOrganizationRequest request) {

        return ResponseEntity.ok(
                organizationService.updateOrganization(
                        organizationId,
                        request
                )
        );
    }


    // =========================================================
    // DELETE ORGANIZATION
    // =========================================================

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(
            @PathVariable Long organizationId) {

        organizationService.deleteOrganization(
                organizationId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}