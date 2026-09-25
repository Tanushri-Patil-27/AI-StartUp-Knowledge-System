package com.ai.user.service;

import com.ai.user.dto.LoginRequest;
import com.ai.user.dto.LoginResponse;
import com.ai.user.dto.RegisterRequest;
import com.ai.user.model.Invitation;
import com.ai.user.model.User;
import com.ai.user.repository.InvitationRepository;
import com.ai.user.repository.UserRepository;
import com.ai.user.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            InvitationRepository invitationRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================================================
    // REGISTER USING INVITATION
    // =========================================================

    public String register(RegisterRequest request) {

        Invitation invitation = invitationRepository
                .findByToken(request.getInvitationToken())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid invitation token"
                        )
                );

        // Check invitation status
        if (invitation.isUsed()) {
            throw new RuntimeException(
                    "Invitation has already been used"
            );
        }

        // Check invitation expiration
        if (invitation.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Invitation has expired"
            );
        }

        // Email in registration must match invitation
        if (!invitation.getEmail()
                .equalsIgnoreCase(request.getEmail())) {

            throw new RuntimeException(
                    "Email does not match the invitation"
            );
        }

        // Check whether email already exists
        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(
                request.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        /*
         * VERY IMPORTANT
         *
         * User does NOT choose the role.
         *
         * Role comes from the server-side invitation.
         */
        user.setRole(
                invitation.getRole()
        );

        /*
         * Organization also comes from
         * the server-side invitation.
         */
        user.setOrganizationId(
                invitation.getOrganizationId()
        );

        user.setEnabled(true);

        userRepository.save(user);

        // Mark invitation as used
        invitation.setUsed(true);

        invitationRepository.save(invitation);

        return "User registered successfully as "
                + user.getRole().name();
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        if (!user.isEnabled()) {

            throw new RuntimeException(
                    "User account is disabled"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getOrganizationId()
        );

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getOrganizationId()
        );
    }
}