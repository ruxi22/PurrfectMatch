package com.purrfect.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncodingTest {

	@Test
	void passwordShouldBeEncodedCorrectly() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String raw = "mySecret123";
		String encoded = encoder.encode(raw);

		assertNotEquals(raw, encoded, "Encoded password should not match raw password");
		assertTrue(encoder.matches(raw, encoded), "Encoder should verify encoded password");
	}
}
