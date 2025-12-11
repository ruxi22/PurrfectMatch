package com.purrfect.notification_service;

import com.purrfect.notification_service.domain.Notification;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificationMessageTest {

	@Test
	void notificationShouldFormatMessageCorrectly() {
		Notification notif = new Notification();
		notif.setUserId(10L);
		notif.setMessage("Your adoption request was approved!");

		assertEquals(10L, notif.getUserId());
		assertEquals("Your adoption request was approved!", notif.getMessage());
	}
}
