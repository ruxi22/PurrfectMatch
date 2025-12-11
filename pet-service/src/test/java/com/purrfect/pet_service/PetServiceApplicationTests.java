package com.purrfect.pet_service;

import com.purrfect.pet_service.controller.PetController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnOkForPetsEndpoint() throws Exception {
		mockMvc.perform(get("/api/pets"))   // schimbă path-ul dacă la tine e altceva
				.andExpect(status().isOk());
	}
}
