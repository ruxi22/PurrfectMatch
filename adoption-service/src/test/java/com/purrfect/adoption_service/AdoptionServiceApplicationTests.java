package com.purrfect.adoption_service;

import com.purrfect.adoption_service.domain.AdoptionStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdoptionServiceApplicationTests {

	@Test
	void adoptionStatusEnumShouldContainExpectedValues() {
		assertNotNull(AdoptionStatus.PENDING);
		assertNotNull(AdoptionStatus.APPROVED);
		assertNotNull(AdoptionStatus.REJECTED);

		assertEquals("PENDING", AdoptionStatus.PENDING.name());
	}
}
