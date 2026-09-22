package com.example.Notification.kafka;

import com.example.Notification.dto.NotificationEvent;
import com.example.Notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "notification-events",
            groupId = "notification-service-group"
    )
    public void consume(String message) {

        try {

            NotificationEvent event =
                    objectMapper.readValue(
                            message,
                            NotificationEvent.class
                    );

            notificationService.createNotification(event);

        } catch (Exception e) {

            System.err.println(
                    "Failed to process notification event: "
                            + e.getMessage()
            );
        }
    }
}
