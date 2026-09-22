package com.example.Notification.controller;

import com.example.Notification.dto.NotificationResponseDTO;
import com.example.Notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>>
    getUserNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId)
        );
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>>
    getUnreadNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId)
        );
    }

    @PutMapping("/{notificationId}/read/{userId}")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId,
            @PathVariable Long userId) {

        notificationService.markAsRead(
                notificationId,
                userId
        );

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<String> markAllAsRead(
            @PathVariable Long userId) {

        notificationService.markAllAsRead(userId);

        return ResponseEntity.ok(
                "All notifications marked as read"
        );
    }
}
