package com.ai.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.user.dto.InviteUserRequest;
import com.ai.user.model.Invitation;
import com.ai.user.model.User;
import com.ai.user.service.InvitationService;
import com.ai.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/invitations")
@CrossOrigin(origins = "*")
public class InvitationController {

    private final InvitationService invitationService;
    private final UserService userService;

    public InvitationController(
            InvitationService invitationService,
            UserService userService) {

        this.invitationService = invitationService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createInvitation(
            @Valid @RequestBody InviteUserRequest request,
            Authentication authentication) {

        // Get logged-in ADMIN email from JWT
        String adminEmail =
                authentication.getName();

        // Find ADMIN in database
        User admin =
                userService.getUserByEmail(adminEmail);

        // Create invitation + send email
        Invitation invitation =
                invitationService.createInvitation(
                        admin,
                        request
                );

        return ResponseEntity.ok(invitation);
    }
}