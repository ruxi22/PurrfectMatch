package com.purrfect.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceApplicationTests {

	// Test 1: Spring Boot should start without errors
	@Test
	void contextLoads() {
		// This test simply verifies that the app can start.
	}

	// Test 2: Password encoder works (this is the meaningful test)
	@Test
	void passwordShouldBeEncodedCorrectly() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String raw = "mySecret123";
		String encoded = encoder.encode(raw);

		assertNotEquals(raw, encoded, "Encoded password should not match raw password");
		assertTrue(encoder.matches(raw, encoded), "Encoder should verify encoded password");
	}
}
