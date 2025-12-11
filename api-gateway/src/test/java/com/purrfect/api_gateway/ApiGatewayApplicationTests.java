package com.purrfect.api_gateway;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiGatewayBasicTest {

	@Test
	void basicLogicShouldWork() {
		int a = 5;
		int b = 7;
		assertEquals(12, a + b, "Basic math sanity check for pipeline");
	}
}
