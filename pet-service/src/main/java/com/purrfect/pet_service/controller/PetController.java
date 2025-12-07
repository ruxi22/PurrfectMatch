package com.purrfect.pet_service.controller;

import com.purrfect.pet_service.domain.Pet;
import com.purrfect.pet_service.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.findById(id);
        return pet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(
            @ModelAttribute Pet pet,
            @RequestParam(value = "photo", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                Path uploadDir = Paths.get("uploads", "pets");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                //generate unique filename with timestamp
                String originalFilename = file.getOriginalFilename();
                String baseName = "pet";
                String extension = "";
                
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    int lastDotIndex = originalFilename.lastIndexOf(".");
                    if (lastDotIndex > 0) {
                        baseName = originalFilename.substring(0, lastDotIndex);
                        extension = originalFilename.substring(lastDotIndex);
                    } else {
                        baseName = originalFilename;
                    }
                    //sanitize base name
                    baseName = baseName.replaceAll("[^a-zA-Z0-9.-]", "_");
                }
                
                String filename = System.currentTimeMillis() + "_" + baseName + extension;
                
                Path filePath = uploadDir.resolve(filename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                String photoPath = "/uploads/pets/" + filename;
                pet.setPhotoPath(photoPath);
                logger.info("File uploaded successfully: {} -> photoPath: {}", filePath.toAbsolutePath(), photoPath);
            } catch (IOException e) {
                logger.error("Failed to upload file: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        if (pet.getStatus() == null) {
            pet.setStatus("AVAILABLE");
        }

        Pet savedPet = petService.save(pet);
        logger.info("Pet created with ID: {}, photoPath: {}", savedPet.getId(), savedPet.getPhotoPath());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"})
    public ResponseEntity<Pet> updatePetJson(
            @PathVariable Long id,
            @RequestBody Pet pet) {
        Optional<Pet> existingPet = petService.findById(id);
        if (existingPet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pet existing = existingPet.get();
        
        //update fields from request, preserving photoPath if not provided
        if (pet.getName() != null) existing.setName(pet.getName());
        if (pet.getBreed() != null) existing.setBreed(pet.getBreed());

        existing.setAge(pet.getAge());
        if (pet.getGender() != null) existing.setGender(pet.getGender());
        if (pet.getHealth() != null) existing.setHealth(pet.getHealth());
        if (pet.getPersonality() != null) existing.setPersonality(pet.getPersonality());
        if (pet.getStatus() != null) existing.setStatus(pet.getStatus());
        
        if (pet.getPhotoPath() != null) existing.setPhotoPath(pet.getPhotoPath());

        Pet updatedPet = petService.save(existing);
        logger.info("Pet updated (JSON) with ID: {}, photoPath: {}", updatedPet.getId(), updatedPet.getPhotoPath());
        return ResponseEntity.ok(updatedPet);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Pet> updatePetWithFile(
            @PathVariable Long id,
            @ModelAttribute Pet pet,
            @RequestParam(value = "photo", required = false) MultipartFile file) {
        Optional<Pet> existingPet = petService.findById(id);
        if (existingPet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pet existing = existingPet.get();
        
        if (pet.getName() != null) existing.setName(pet.getName());
        if (pet.getBreed() != null) existing.setBreed(pet.getBreed());

        existing.setAge(pet.getAge());
        if (pet.getGender() != null) existing.setGender(pet.getGender());
        if (pet.getHealth() != null) existing.setHealth(pet.getHealth());
        if (pet.getPersonality() != null) existing.setPersonality(pet.getPersonality());
        if (pet.getStatus() != null) existing.setStatus(pet.getStatus());

        if (file != null && !file.isEmpty()) {
            try {
                Path uploadDir = Paths.get("uploads", "pets");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String originalFilename = file.getOriginalFilename();
                String baseName = "pet";
                String extension = "";
                
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    int lastDotIndex = originalFilename.lastIndexOf(".");
                    if (lastDotIndex > 0) {
                        baseName = originalFilename.substring(0, lastDotIndex);
                        extension = originalFilename.substring(lastDotIndex);
                    } else {
                        baseName = originalFilename;
                    }
                    baseName = baseName.replaceAll("[^a-zA-Z0-9.-]", "_");
                }
                
                String filename = System.currentTimeMillis() + "_" + baseName + extension;
                
                Path filePath = uploadDir.resolve(filename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                String photoPath = "/uploads/pets/" + filename;
                existing.setPhotoPath(photoPath);
                logger.info("File uploaded successfully: {} -> photoPath: {}", filePath.toAbsolutePath(), photoPath);
            } catch (IOException e) {
                logger.error("Failed to upload file: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Pet updatedPet = petService.save(existing);
        logger.info("Pet updated (with file) with ID: {}, photoPath: {}", updatedPet.getId(), updatedPet.getPhotoPath());
        return ResponseEntity.ok(updatedPet);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Pet> updatePetStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Optional<Pet> petOpt = petService.findById(id);
        if (petOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Pet pet = petOpt.get();
        pet.setStatus(status);
        Pet updatedPet = petService.save(pet);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        Optional<Pet> pet = petService.findById(id);
        if (pet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        petService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}




