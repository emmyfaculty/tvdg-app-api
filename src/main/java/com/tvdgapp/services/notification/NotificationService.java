//package com.tvdgapp.services.notification;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
//import com.tvdgapp.dtos.notification.NotificationRequestDto;
//import com.tvdgapp.repositories.notification.NotificationRepository;
//import com.tvdgapp.services.affiliate.AffiliateUserServiceImpl;
//import com.tvdgapp.services.user.UserService;
//import com.tvdgapp.utils.EmailTemplateUtils;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
//
//    private final UserService userService;
//    private final EmailTemplateUtils emailService;
//    private final NotificationRepository notificationRepository;
//
//    public void sendNotification(NotificationRequestDto notificationRequestDto) {
//        String type = notificationRequestDto.getNotificationType();
//        String role = notificationRequestDto.getRole();
//
//        if (type.equalsIgnoreCase("push")) {
//            sendPushNotification(notificationRequestDto, role);
//        } else if (type.equalsIgnoreCase("email")) {
//            sendEmailNotification(notificationRequestDto, role);
//        } else if (type.equalsIgnoreCase("reminder")) {
//            sendReminder(notificationRequestDto, role);
//        }
//
//        // Save the notification to the database
//        saveNotification(notificationRequestDto);
//    }
//
//    private void sendPushNotification(NotificationRequestDto notificationRequestDto, String role) {
//        List<String> targets = notificationRequestDto.getTargets();
//        if (targets == null || targets.isEmpty()) {
//            targets = userService.getUserTokensByRole(role);
//        }
//
//        for (String target : targets) {
//            Message message = Message.builder()
//                    .putData("title", notificationRequestDto.getTitle())
//                    .putData("message", notificationRequestDto.getMessage())
//                    .setToken(target)
//                    .build();
//
//            try {
//                String response = FirebaseMessaging.getInstance().send(message);
//                System.out.println("Successfully sent message to " + target + ": " + response);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    private void sendEmailNotification(NotificationRequestDto notificationRequestDto, String role) {
//        try {
//            this.emailService.sendEmailNotification(notificationRequestDto, role);
//        } catch (Exception e) {
//            LOGGER.error("Cannot send notification email", e);
//        }
//    }
//
//    private void sendReminder(NotificationRequestDto notificationRequestDto, String role) {
//        // Implement reminder logic here
//        // This can be a combination of push notifications, emails, or something specific
//        sendPushNotification(notificationRequestDto, role);
//        sendEmailNotification(notificationRequestDto, role);
//    }
//
//    private void saveNotification(NotificationRequestDto notificationRequestDto) {
//        Notification notification = new Notification();
//        notification.setUserId(notificationRequestDto.getUserId());
//        notification.setTitle(notificationRequestDto.getTitle());
//        notification.setMessage(notificationRequestDto.getMessage());
//        notification.setNotificationType(notificationRequestDto.getNotificationType());
//        notification.setRole(notificationRequestDto.getRole());
//        notificationRepository.save(notification);
//    }
//
//    public List<Notification> listNotificationsByUser(Long userId) {
//        return notificationRepository.findByUserId(userId);
//    }
//
//    public Notification fetchNotificationDetails(Long id) {
//        return notificationRepository.findById(id).orElse(null);
//    }
//
//    public List<Notification> listGeneralNotifications() {
//        return notificationRepository.findByUserIdIsNull();
//    }
//
//    public List<Notification> listAllNotificationsForUser(Long userId) {
//        List<Notification> userNotifications = notificationRepository.findByUserId(userId);
//        List<Notification> generalNotifications = notificationRepository.findByUserIdIsNull();
//        userNotifications.addAll(generalNotifications);
//        return userNotifications;
//    }
//}
