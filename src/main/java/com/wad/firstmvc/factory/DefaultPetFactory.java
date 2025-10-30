package com.wad.firstmvc.factory;

import com.wad.firstmvc.domain.Pet;

public class DefaultPetFactory implements PetFactory {

    @Override
    public Pet create(String name, String breed, double age, String personality, String photoPath) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setBreed(breed);
        pet.setAge(age);
        pet.setPersonality(personality);
        pet.setPhotoPath(photoPath);
        pet.setStatus("Available");
        return pet;
    }
}
