package com.example.AuditService.event;


import com.example.AuditService.model.AuditAction;

public class AuditEvent {

    private Long organizationId;

    private Long userId;

    private AuditAction action;

    private String resourceType;

    private String resourceId;

    private String ipAddress;

    private String details;


    public AuditEvent() {
    }


    public AuditEvent(
            Long organizationId,
            Long userId,
            AuditAction action,
            String resourceType,
            String resourceId,
            String ipAddress,
            String details) {

        this.organizationId = organizationId;
        this.userId = userId;
        this.action = action;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.ipAddress = ipAddress;
        this.details = details;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
