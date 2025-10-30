package com.wad.firstmvc.factory;

import com.wad.firstmvc.domain.Pet;

public interface PetFactory {

    /**
     * Creates a Pet instance based on provided attributes.
     *
     * @param name         the name of the pet
     * @param breed        the breed of the pet
     * @param age          the age of the pet
     * @param personality  short description of personality
     * @param photoPath    the path to the pet's photo
     * @return             a new Pet instance
     */
    Pet create(String name, String breed, double age, String personality, String photoPath);
}
