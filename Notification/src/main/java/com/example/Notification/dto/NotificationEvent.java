package com.example.Notification.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    private Long userId;

    private Long organizationId;

    private String title;

    private String message;

    private String type;
}
