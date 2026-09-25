package com.ai.user.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ai.user.model.Role;
import com.ai.user.model.User;
import com.ai.user.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Read initial ADMIN configuration from PowerShell environment variables
    private final String adminName =
            System.getenv("INITIAL_ADMIN_NAME");

    private final String adminEmail =
            System.getenv("INITIAL_ADMIN_EMAIL");

    private final String adminPassword =
            System.getenv("INITIAL_ADMIN_PASSWORD");

    private final String organizationId =
            System.getenv("INITIAL_ADMIN_ORGANIZATION_ID");

    public AdminInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Check if an ADMIN already exists
        boolean adminExists =
                userRepository.findAll()
                        .stream()
                        .anyMatch(
                                user ->
                                        user.getRole()
                                                == Role.ADMIN
                        );

        if (adminExists) {

            System.out.println(
                    "ADMIN already exists. "
                            + "Skipping initial admin creation."
            );

            return;
        }

        // Check environment configuration
        if (adminName == null
                || adminName.isBlank()
                || adminEmail == null
                || adminEmail.isBlank()
                || adminPassword == null
                || adminPassword.isBlank()
                || organizationId == null
                || organizationId.isBlank()) {

            System.out.println(
                    "Initial ADMIN credentials are not configured."
            );

            System.out.println(
                    "Skipping initial admin creation."
            );

            return;
        }

        // Check whether admin email already exists
        if (userRepository.existsByEmail(adminEmail)) {

            System.out.println(
                    "Initial admin email already exists."
            );

            return;
        }

        // Create ADMIN
        User admin = new User();

        admin.setName(adminName);
        admin.setEmail(adminEmail);

        admin.setPassword(
                passwordEncoder.encode(adminPassword)
        );

        admin.setRole(Role.ADMIN);

        admin.setOrganizationId(
                Long.parseLong(organizationId)
        );

        admin.setEnabled(true);

        userRepository.save(admin);

        System.out.println(
                "======================================"
        );

        System.out.println(
                "INITIAL ADMIN CREATED"
        );

        System.out.println(
                "Email: " + adminEmail
        );

        System.out.println(
                "Organization ID: " + organizationId
        );

        System.out.println(
                "======================================"
        );
    }
}