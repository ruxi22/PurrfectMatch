package com.purrfect.pet_service.service;

import com.purrfect.pet_service.domain.Pet;
import java.util.List;
import java.util.Optional;

public interface PetService {
    List<Pet> findAll();
    Optional<Pet> findById(Long id);
    Pet save(Pet pet);
    void deleteById(Long id);
}






