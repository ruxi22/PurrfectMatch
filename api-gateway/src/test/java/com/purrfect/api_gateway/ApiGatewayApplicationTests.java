package com.purrfect.api_gateway;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiGatewayApplicationTests {

	@Test
	void serviceUrlShouldBeFormattedCorrectly() {
		String baseUrl = "http://localhost:8081";
		String endpoint = "/auth/login";

		String fullUrl = baseUrl + endpoint;

		assertEquals("http://localhost:8081/auth/login", fullUrl);
	}
}
