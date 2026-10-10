package com.ai.document.security;

public class JwtUserPrincipal {

    private final Long userId;
    private final String email;
    private final String role;
    private final Long organizationId;

    public JwtUserPrincipal(
            Long userId,
            String email,
            String role,
            Long organizationId) {

        this.userId = userId;
        this.email = email;
        this.role = role;
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Long getOrganizationId() {
        return organizationId;
    }
}