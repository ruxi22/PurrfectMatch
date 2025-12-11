package com.purrfect.pet_service;

import com.purrfect.pet_service.domain.Pet;
import com.purrfect.pet_service.domain.PetType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PetServiceApplicationTests {

	@Test
	void petShouldStoreAndReturnValuesCorrectly() {
		Pet pet = new Pet();
		pet.setName("Milo");
		pet.setAge(2);
		pet.setType(PetType.CAT);

		assertEquals("Milo", pet.getName());
		assertEquals(2, pet.getAge());
		assertEquals(PetType.CAT, pet.getType());
	}
}
