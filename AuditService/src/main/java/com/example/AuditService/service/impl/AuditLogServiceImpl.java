package com.example.AuditService.service.impl;

import com.example.AuditService.dto.AuditLogResponse;
import com.example.AuditService.event.AuditEvent;
import com.example.AuditService.model.AuditLog;
import com.example.AuditService.repository.AuditLogRepository;
import com.example.AuditService.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;


    public AuditLogServiceImpl(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }


    @Override
    public AuditLog saveAudit(AuditEvent event) {

        AuditLog auditLog = new AuditLog();

        auditLog.setOrganizationId(
                event.getOrganizationId()
        );

        auditLog.setUserId(
                event.getUserId()
        );

        auditLog.setAction(
                event.getAction()
        );

        auditLog.setResourceType(
                event.getResourceType()
        );

        auditLog.setResourceId(
                event.getResourceId()
        );

        auditLog.setIpAddress(
                event.getIpAddress()
        );

        auditLog.setDetails(
                event.getDetails()
        );

        return auditLogRepository.save(auditLog);
    }


    @Override
    public List<AuditLogResponse> getByOrganization(
            Long organizationId) {

        return auditLogRepository
                .findByOrganizationIdOrderByTimestampDesc(
                        organizationId
                )
                .stream()
                .map(AuditLogResponse::fromEntity)
                .toList();
    }


    @Override
    public List<AuditLogResponse> getByUser(
            Long userId) {

        return auditLogRepository
                .findByUserIdOrderByTimestampDesc(
                        userId
                )
                .stream()
                .map(AuditLogResponse::fromEntity)
                .toList();
    }


    @Override
    public List<AuditLogResponse>
    getByOrganizationAndUser(
            Long organizationId,
            Long userId) {

        return auditLogRepository
                .findByOrganizationIdAndUserIdOrderByTimestampDesc(
                        organizationId,
                        userId
                )
                .stream()
                .map(AuditLogResponse::fromEntity)
                .toList();
    }


    @Override
    public AuditLogResponse getById(Long id) {

        AuditLog auditLog =
                auditLogRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Audit log not found"
                                )
                        );

        return AuditLogResponse.fromEntity(
                auditLog
        );
    }
}
