package com.purrfect.adoption_service.service;

import com.purrfect.adoption_service.client.AuthServiceClient;
import com.purrfect.adoption_service.client.PetServiceClient;
import com.purrfect.adoption_service.domain.Adoption;
import com.purrfect.adoption_service.exception.ExternalServiceException;
import com.purrfect.adoption_service.exception.PetNotAvailableException;
import com.purrfect.adoption_service.exception.PetNotFoundException;
import com.purrfect.adoption_service.exception.UserNotFoundException;
import com.purrfect.adoption_service.repository.AdoptionRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionServiceImpl.class);

    private final AdoptionRepository adoptionRepository;
    private final AuthServiceClient authServiceClient;
    private final PetServiceClient petServiceClient;

    @Autowired
    public AdoptionServiceImpl(
            AdoptionRepository adoptionRepository,
            AuthServiceClient authServiceClient,
            PetServiceClient petServiceClient) {
        this.adoptionRepository = adoptionRepository;
        this.authServiceClient = authServiceClient;
        this.petServiceClient = petServiceClient;
    }

    @Override
    public Adoption createAdoptionRequest(Long userId, Long petId, LocalDateTime appointmentDateTime) {
        logger.info("Creating adoption request for userId={}, petId={}, appointmentDateTime={}", 
                userId, petId, appointmentDateTime);

        // Validate user exists
        logger.info("Calling Auth Service to verify user with id={}", userId);
        try {
            Map<String, Object> user = authServiceClient.getUserById(userId);
            if (user == null || user.isEmpty()) {
                logger.warn("User not found with id={}", userId);
                throw new UserNotFoundException("User not found with id: " + userId);
            }
            logger.info("User verified successfully: userId={}", userId);
        } catch (FeignException.NotFound e) {
            logger.error("User not found in Auth Service: userId={}", userId, e);
            throw new UserNotFoundException("User not found with id: " + userId);
        } catch (FeignException e) {
            logger.error("Auth Service error when verifying user: userId={}, status={}", userId, e.status(), e);
            if (e.status() == 503 || e.status() == -1) {
                throw new ExternalServiceException("Auth Service is currently unavailable", e);
            }
            throw new ExternalServiceException("Error communicating with Auth Service", e);
        }

        // Validate pet exists and is available
        logger.info("Calling Pet Service to verify pet with id={}", petId);
        try {
            Map<String, Object> pet = petServiceClient.getPetById(petId);
            if (pet == null || pet.isEmpty()) {
                logger.warn("Pet not found with id={}", petId);
                throw new PetNotFoundException("Pet not found with id: " + petId);
            }
            
            String petStatus = (String) pet.get("status");
            logger.info("Pet found: petId={}, status={}", petId, petStatus);
            
            if (petStatus == null || !petStatus.equals("AVAILABLE")) {
                logger.warn("Pet is not available: petId={}, status={}", petId, petStatus);
                throw new PetNotAvailableException("Pet is not available for adoption. Current status: " + petStatus);
            }
            logger.info("Pet verified as available: petId={}", petId);
        } catch (FeignException.NotFound e) {
            logger.error("Pet not found in Pet Service: petId={}", petId, e);
            throw new PetNotFoundException("Pet not found with id: " + petId);
        } catch (PetNotAvailableException e) {
            throw e; // Re-throw our custom exception
        } catch (FeignException e) {
            logger.error("Pet Service error when verifying pet: petId={}, status={}", petId, e.status(), e);
            if (e.status() == 503 || e.status() == -1) {
                throw new ExternalServiceException("Pet Service is currently unavailable", e);
            }
            throw new ExternalServiceException("Error communicating with Pet Service", e);
        }

        // Create adoption request
        Adoption adoption = new Adoption();
        adoption.setUserId(userId);
        adoption.setPetId(petId);
        adoption.setAppointmentDateTime(appointmentDateTime);
        adoption.setStatus("PENDING");
        Adoption savedAdoption = adoptionRepository.save(adoption);
        logger.info("Adoption request created successfully: adoptionId={}", savedAdoption.getId());

        // Update pet status to PENDING (not ADOPTED - that happens on approval)
        logger.info("Updating pet status to PENDING: petId={}", petId);
        try {
            petServiceClient.updatePetStatus(petId, "PENDING");
            logger.info("Pet status updated to PENDING successfully: petId={}", petId);
        } catch (FeignException e) {
            logger.error("Failed to update pet status to PENDING: petId={}", petId, e);
            // Don't fail the adoption creation, but log the error
            // The pet status can be updated manually later if needed
        }

        return savedAdoption;
    }

    @Override
    public List<Adoption> getAdoptionRequestsByUser(Long userId) {
        return adoptionRepository.findByUserId(userId);
    }

    @Override
    public List<Adoption> getAllAdoptionRequests() {
        return adoptionRepository.findAll();
    }

    @Override
    public Optional<Adoption> getAdoptionRequestById(Long id) {
        return adoptionRepository.findById(id);
    }

    @Override
    public Adoption updateAdoptionStatus(Long id, String status) {
        logger.info("Updating adoption status: adoptionId={}, newStatus={}", id, status);
        
        Optional<Adoption> adoptionOpt = adoptionRepository.findById(id);
        if (adoptionOpt.isEmpty()) {
            logger.warn("Adoption not found: adoptionId={}", id);
            throw new RuntimeException("Adoption not found with id: " + id);
        }

        Adoption adoption = adoptionOpt.get();
        String oldStatus = adoption.getStatus();
        adoption.setStatus(status);
        Adoption updatedAdoption = adoptionRepository.save(adoption);
        logger.info("Adoption status updated: adoptionId={}, oldStatus={}, newStatus={}", 
                id, oldStatus, status);

        // If status is APPROVED, update pet status to ADOPTED
        if ("APPROVED".equals(status)) {
            Long petId = adoption.getPetId();
            logger.info("Adoption approved, updating pet status to ADOPTED: petId={}", petId);
            try {
                petServiceClient.updatePetStatus(petId, "ADOPTED");
                logger.info("Pet status updated to ADOPTED successfully: petId={}", petId);
            } catch (FeignException.NotFound e) {
                logger.error("Pet not found when updating status: petId={}", petId, e);
                // Don't fail the adoption update, but log the error
            } catch (FeignException e) {
                logger.error("Failed to update pet status to ADOPTED: petId={}", petId, e);
                // Don't fail the adoption update, but log the error
            }
        }

        return updatedAdoption;
    }
}




