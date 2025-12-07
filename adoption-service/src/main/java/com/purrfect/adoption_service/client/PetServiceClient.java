package com.purrfect.adoption_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "pet-service", url = "${pet-service.url:http://localhost:8082}")
public interface PetServiceClient {
    @GetMapping("/api/pets/{id}")
    Map<String, Object> getPetById(@PathVariable Long id);
    
    @PutMapping("/api/pets/{id}/status")
    Map<String, Object> updatePetStatus(@PathVariable Long id, @RequestParam String status);
}




