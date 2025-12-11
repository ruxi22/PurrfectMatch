package com.purrfect.pet_service;

import com.purrfect.pet_service.domain.Pet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetServiceApplicationTests {

	@Test
	void petShouldStoreValuesCorrectly() {
		Pet pet = new Pet();

		pet.setName("Bella");
		pet.setAge(3);

		assertEquals("Bella", pet.getName());
		assertEquals(3, pet.getAge());
	}
}
