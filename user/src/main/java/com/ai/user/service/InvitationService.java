package com.ai.user.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ai.user.dto.InviteUserRequest;
import com.ai.user.model.Invitation;
import com.ai.user.model.Role;
import com.ai.user.model.User;
import com.ai.user.repository.InvitationRepository;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    public InvitationService(
            InvitationRepository invitationRepository,
            EmailService emailService) {

        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
    }

    public Invitation createInvitation(
            User admin,
            InviteUserRequest request) {

        

        if (admin.getRole() != Role.ADMIN) {

            throw new RuntimeException(
                    "Only ADMIN can create invitations"
            );
        }

        // -----------------------------------------------------
        // ADMIN cannot invite another ADMIN
        // -----------------------------------------------------

        if (request.getRole() == Role.ADMIN) {

            throw new RuntimeException(
                    "ADMIN cannot invite another ADMIN"
            );
        }

        // -----------------------------------------------------
        // Create invitation
        // -----------------------------------------------------

        Invitation invitation = new Invitation();

        invitation.setEmail(
                request.getEmail()
        );

        invitation.setRole(
                request.getRole()
        );

        // Organization comes from logged-in ADMIN
        invitation.setOrganizationId(
                admin.getOrganizationId()
        );

        // Generate unique token
        invitation.setToken(
                UUID.randomUUID().toString()
        );

        // Invitation valid for 24 hours
        invitation.setExpiresAt(
                LocalDateTime.now()
                        .plusHours(24)
        );

        invitation.setUsed(false);

        // Save invitation
        Invitation savedInvitation =
                invitationRepository.save(invitation);

        // -----------------------------------------------------
        // Send actual email
        // -----------------------------------------------------

        emailService.sendInvitationEmail(
                savedInvitation
        );

        return savedInvitation;
    }
}