package com.tvdgapp.controllers.notification;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.notification.NotificationRequestDto;
import com.tvdgapp.models.notification.Notification;
import com.tvdgapp.services.notification.NotificationService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @PreAuthorize("hasAuthority({'ADMIN'})")
    public ResponseEntity<ApiDataResponse<String>> sendNotification(@RequestBody NotificationRequestDto notificationRequestDto) {
        notificationService.sendNotification(notificationRequestDto);
        return  ApiResponseUtils.response(HttpStatus.OK,"Notification sent successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiDataResponse<List<Notification>>> listNotificationsByUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.listNotificationsByUser(userId);
        return ApiResponseUtils.response(HttpStatus.OK,notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Notification>> fetchNotificationDetails(@PathVariable Long id) {
        Notification notification = notificationService.fetchNotificationDetails(id);
        return ApiResponseUtils.response(HttpStatus.OK,notification);
    }

    @GetMapping("/general")
    @PreAuthorize("hasAuthority({'ADMIN'})")
    public ResponseEntity<ApiDataResponse<List<Notification>>> listGeneralNotifications() {
        List<Notification> notifications = notificationService.listGeneralNotifications();
        return ApiResponseUtils.response(HttpStatus.OK, notifications);
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<ApiDataResponse<List<Notification>>> listAllNotificationsForUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.listAllNotificationsForUser(userId);
        return ApiResponseUtils.response(HttpStatus.OK, notifications);
    }
}
