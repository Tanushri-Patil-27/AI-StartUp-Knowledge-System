package com.example.Notification.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {

    private Long id;

    private Long userId;

    private Long organizationId;

    private String title;

    private String message;

    private String type;

    private boolean read;

    private LocalDateTime createdAt;
}