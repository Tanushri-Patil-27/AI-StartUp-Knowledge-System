package com.example.Notification.service.impl;

import com.example.Notification.dto.NotificationEvent;
import com.example.Notification.dto.NotificationResponseDTO;
import com.example.Notification.entity.Notification;
import com.example.Notification.repository.NotificationRepository;
import com.example.Notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO createNotification(
            NotificationEvent event) {

        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .organizationId(event.getOrganizationId())
                .title(event.getTitle())
                .message(event.getMessage())
                .type(event.getType())
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved =
                notificationRepository.save(notification);

        return mapToDTO(saved);
    }

    @Override
    public List<NotificationResponseDTO> getUserNotifications(
            Long userId) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotifications(
            Long userId) {

        return notificationRepository
                .findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void markAsRead(
            Long notificationId,
            Long userId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                ));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You cannot modify this notification"
            );
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndReadFalseOrderByCreatedAtDesc(
                                userId
                        );

        notifications.forEach(
                notification -> notification.setRead(true)
        );

        notificationRepository.saveAll(notifications);
    }

    private NotificationResponseDTO mapToDTO(
            Notification notification) {

        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .organizationId(notification.getOrganizationId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}