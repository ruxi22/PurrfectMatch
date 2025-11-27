package com.purrfect.notification_service.service;

import com.purrfect.notification_service.domain.Notification;
import java.util.List;

public interface NotificationService {
    Notification sendNotification(Long userId, String type, String title, String message);
    List<Notification> getNotificationsByUser(Long userId);
    List<Notification> getUnreadNotificationsByUser(Long userId);
    Notification markAsRead(Long notificationId);
    Notification getNotificationById(Long id);
}





