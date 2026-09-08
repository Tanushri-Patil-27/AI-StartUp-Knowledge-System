package com.example.AuditService.service;


import com.example.AuditService.dto.AuditLogResponse;
import com.example.AuditService.event.AuditEvent;
import com.example.AuditService.model.AuditLog;

import java.util.List;

public interface AuditLogService {

    AuditLog saveAudit(AuditEvent event);

    List<AuditLogResponse> getByOrganization(
            Long organizationId
    );

    List<AuditLogResponse> getByUser(
            Long userId
    );

    List<AuditLogResponse> getByOrganizationAndUser(
            Long organizationId,
            Long userId
    );

    AuditLogResponse getById(Long id);
}