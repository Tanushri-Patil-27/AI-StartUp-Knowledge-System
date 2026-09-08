package com.example.AuditService.repository;

import com.example.AuditService.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByOrganizationIdOrderByTimestampDesc(
            Long organizationId
    );

    List<AuditLog> findByUserIdOrderByTimestampDesc(
            Long userId
    );

    List<AuditLog> findByOrganizationIdAndUserIdOrderByTimestampDesc(
            Long organizationId,
            Long userId
    );}