package com.example.AuditService.dto;

import com.example.AuditService.model.AuditAction;
import com.example.AuditService.model.AuditLog;

import java.time.LocalDateTime;

public record AuditLogResponse(

        Long id,

        Long organizationId,

        Long userId,

        AuditAction action,

        String resourceType,

        String resourceId,

        String ipAddress,

        String details,

        LocalDateTime timestamp
) {

    public static AuditLogResponse fromEntity(
            AuditLog auditLog) {

        return new AuditLogResponse(

                auditLog.getId(),

                auditLog.getOrganizationId(),

                auditLog.getUserId(),

                auditLog.getAction(),

                auditLog.getResourceType(),

                auditLog.getResourceId(),

                auditLog.getIpAddress(),

                auditLog.getDetails(),

                auditLog.getTimestamp()
        );
    }
}
