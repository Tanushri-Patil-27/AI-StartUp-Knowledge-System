package com.example.Notification.service;

import com.example.Notification.dto.NotificationEvent;
import com.example.Notification.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {

    NotificationResponseDTO createNotification(
            NotificationEvent event
    );

    List<NotificationResponseDTO> getUserNotifications(
            Long userId
    );

    List<NotificationResponseDTO> getUnreadNotifications(
            Long userId
    );

    void markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);
}
