package com.example.OrganizationService.controller;

import com.example.OrganizationService.dto.AddMemberRequest;
import com.example.OrganizationService.dto.OrganizationMemberResponse;
import com.example.OrganizationService.dto.UpdateMemberRoleRequest;
import com.example.OrganizationService.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations/{organizationId}/members")
@RequiredArgsConstructor
public class MembershipController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationMemberResponse> addMember(
            @PathVariable Long organizationId,
            @Valid @RequestBody AddMemberRequest request,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        OrganizationMemberResponse response =
                organizationService.addMember(
                        organizationId,
                        request,
                        currentUserId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrganizationMemberResponse>> getMembers(
            @PathVariable Long organizationId,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        return ResponseEntity.ok(
                organizationService.getMembers(
                        organizationId,
                        currentUserId
                )
        );
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<OrganizationMemberResponse> updateMemberRole(
            @PathVariable Long organizationId,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateMemberRoleRequest request,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        return ResponseEntity.ok(
                organizationService.updateMemberRole(
                        organizationId,
                        userId,
                        request,
                        currentUserId
                )
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long organizationId,
            @PathVariable Long userId,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        organizationService.removeMember(
                organizationId,
                userId,
                currentUserId
        );

        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId(Authentication authentication) {
        return Long.valueOf(authentication.getName());
    }
}
