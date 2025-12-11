package com.purrfect.adoption_service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdoptionServiceApplicationTests {

	@Test
	void stringFormattingShouldWorkCorrectly() {
		String petName = "Milo";
		int age = 2;

		String result = String.format("%s is %d years old", petName, age);

		assertEquals("Milo is 2 years old", result);
	}
}
