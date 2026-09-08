package com.example.AuditService.controller;

import com.example.AuditService.dto.AuditLogResponse;
import com.example.AuditService.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {

    private final AuditLogService auditLogService;


    public AuditController(
            AuditLogService auditLogService) {

        this.auditLogService = auditLogService;
    }


    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<AuditLogResponse>>
    getOrganizationAudits(
            @PathVariable Long organizationId) {

        return ResponseEntity.ok(
                auditLogService.getByOrganization(
                        organizationId
                )
        );
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponse>>
    getUserAudits(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                auditLogService.getByUser(
                        userId
                )
        );
    }


    @GetMapping(
            "/organization/{organizationId}/user/{userId}"
    )
    public ResponseEntity<List<AuditLogResponse>>
    getOrganizationUserAudits(
            @PathVariable Long organizationId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                auditLogService
                        .getByOrganizationAndUser(
                                organizationId,
                                userId
                        )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse>
    getAuditById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditLogService.getById(id)
        );
    }
}