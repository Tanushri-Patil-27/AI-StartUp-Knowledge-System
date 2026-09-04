package com.ai.document.dto;

public class OrganizationMemberAccessResponse {

    private boolean member;
    private String role;

    public OrganizationMemberAccessResponse() {
    }

    public OrganizationMemberAccessResponse(
            boolean member,
            String role) {

        this.member = member;
        this.role = role;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}