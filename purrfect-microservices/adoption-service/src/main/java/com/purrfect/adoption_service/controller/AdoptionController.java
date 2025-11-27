package com.purrfect.adoption_service.controller;

import com.purrfect.adoption_service.domain.Adoption;
import com.purrfect.adoption_service.dto.AdoptionCreatedEvent;
import com.purrfect.adoption_service.messaging.AdoptionEventProducer;
import com.purrfect.adoption_service.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private AdoptionEventProducer eventProducer;

    @PostMapping
    public ResponseEntity<?> createAdoptionRequest(
            @RequestParam Long userId,
            @RequestParam Long petId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDateTime) {
        
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            Map<String, Object> error = new java.util.HashMap<>();
            error.put("error", "Invalid appointment date");
            error.put("message", "Appointment date cannot be in the past");
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Adoption adoption = adoptionService.createAdoptionRequest(userId, petId, appointmentDateTime);
        
        // publish event to RabbitMQ
        AdoptionCreatedEvent event = new AdoptionCreatedEvent(
                adoption.getId(),
                adoption.getUserId(),
                adoption.getPetId(),
                null, // petName need to fetch from pet-service
                null, // userName need to fetch from auth-service
                adoption.getAppointmentDateTime()
        );
        eventProducer.sendAdoptionCreatedEvent(event);

        return ResponseEntity.status(HttpStatus.CREATED).body(adoption);
    }

    @GetMapping
    public ResponseEntity<List<Adoption>> getAllAdoptions(
            @RequestParam(required = false) Long userId) {
        if (userId != null) {
            return ResponseEntity.ok(adoptionService.getAdoptionRequestsByUser(userId));
        }
        return ResponseEntity.ok(adoptionService.getAllAdoptionRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adoption> getAdoptionById(@PathVariable Long id) {
        Optional<Adoption> adoption = adoptionService.getAdoptionRequestById(id);
        return adoption.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAdoptionStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        if (!status.equals("PENDING") && !status.equals("APPROVED") && !status.equals("REJECTED")) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid status");
            error.put("message", "Status must be PENDING, APPROVED, or REJECTED");
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        Adoption adoption = adoptionService.updateAdoptionStatus(id, status);
        
        // publish event to RabbitMQ for status changes
        AdoptionCreatedEvent event = new AdoptionCreatedEvent(
                adoption.getId(),
                adoption.getUserId(),
                adoption.getPetId(),
                null,
                null,
                adoption.getAppointmentDateTime()
        );
        eventProducer.sendAdoptionCreatedEvent(event);
        
        return ResponseEntity.ok(adoption);
    }
}

