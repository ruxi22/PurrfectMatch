package com.purrfect.adoption_service.service;

import com.purrfect.adoption_service.domain.Adoption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdoptionService {
    Adoption createAdoptionRequest(Long userId, Long petId, LocalDateTime appointmentDateTime);
    List<Adoption> getAdoptionRequestsByUser(Long userId);
    List<Adoption> getAllAdoptionRequests();
    Optional<Adoption> getAdoptionRequestById(Long id);
    Adoption updateAdoptionStatus(Long id, String status);
}

