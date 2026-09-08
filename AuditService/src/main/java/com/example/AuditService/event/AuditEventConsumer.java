package com.example.AuditService.event;

import com.example.AuditService.service.AuditLogService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventConsumer {

    private final AuditLogService auditLogService;


    public AuditEventConsumer(
            AuditLogService auditLogService) {

        this.auditLogService = auditLogService;
    }


    @KafkaListener(
            topics = "audit-events",
            groupId = "audit-service-group"
    )
    public void consumeAuditEvent(
            AuditEvent event) {

        System.out.println(
                "Received audit event: "
                        + event.getAction()
        );

        auditLogService.saveAudit(event);
    }
}
