package com.example.AuditService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic auditEventsTopic() {

        return new NewTopic(
                "audit-events",
                3,
                (short) 1
        );
    }
}
