package com.example.OrganizationService.security;

import lombok.Getter;

@Getter
public class JwtUserPrincipal {

    private final String email;
    private final String role;
    private final Long organizationId;

    public JwtUserPrincipal(
            String email,
            String role,
            Long organizationId) {

        this.email = email;
        this.role = role;
        this.organizationId = organizationId;
    }
}