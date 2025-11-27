package com.purrfect.notification_service.messaging;

import com.purrfect.notification_service.dto.AdoptionCreatedEvent;
import com.purrfect.notification_service.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdoptionEventListener {

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.name:adoption.created.queue}")
    public void handleAdoptionCreated(AdoptionCreatedEvent event) {
        String title = "Adoption Request Submitted";
        String message = String.format(
                "Your adoption request for pet ID %d has been submitted. Appointment scheduled for %s",
                event.getPetId(),
                event.getAppointmentDateTime()
        );

        notificationService.sendNotification(
                event.getUserId(),
                "IN_APP",
                title,
                message
        );
    }
}





