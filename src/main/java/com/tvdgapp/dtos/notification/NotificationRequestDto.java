package com.tvdgapp.dtos.notification;

import lombok.Data;

import java.util.List;
@Data
public class NotificationRequestDto {
    private Long userId;
    private List<String> targets;
    private String title;
    private String message;
    private String notificationType; // push, email, reminder
    private String role; // e.g., rider, customer, affiliate, admin

}
