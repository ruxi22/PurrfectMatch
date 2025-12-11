//package com.purrfect.auth_service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import static org.junit.jupiter.api.Assertions.*;
//
//class PasswordEncodingTest {
//
//	@Test
//	void passwordShouldBeEncodedCorrectly() {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//		String raw = "mySecret123";
//		String encoded = encoder.encode(raw);
//
//		assertNotEquals(raw, encoded, "Encoded password should not match raw password");
//		assertTrue(encoder.matches(raw, encoded), "Encoder should verify encoded password");
//	}
//}


@SpringBootTest
@AutoConfigureMockMvc // This annotation is the key to Grade 10 testing
class AuthServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	// Test 1: Sanity Check (The default)
	@Test
	void contextLoads() {
		// Verifies the application context starts successfully
	}

	// Test 2: Integration/Smoke Test (The Grade 10 Requirement)
	@Test
	void shouldReturnPong() throws Exception {
		mockMvc.perform(get("/ping"))            // 1. Perform a GET request to /ping
				.andExpect(status().isOk())       // 2. Expect a 200 OK status
				.andExpect(content().string("pong")); // 3. Expect the body to say "pong"
	}
}

