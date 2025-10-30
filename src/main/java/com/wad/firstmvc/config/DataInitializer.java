package com.wad.firstmvc.config;

import java.math.BigDecimal;
import java.util.List;

import com.wad.firstmvc.domain.Pet;
import com.wad.firstmvc.domain.User;
import com.wad.firstmvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.wad.firstmvc.domain.Role;
import com.wad.firstmvc.repositories.PetRepository;
import com.wad.firstmvc.factory.PetFactory;
import com.wad.firstmvc.factory.DefaultPetFactory;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PetRepository petRepository;

    private final PetFactory petFactory = new DefaultPetFactory();

    @Override
    public void run(String... args) throws Exception {

        userRepository.deleteAll();
        if (userRepository.count() == 0) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), Role.ADMIN);
            User adopter = new User("adopter", passwordEncoder.encode("adopter123"), Role.ADOPTER);
            userRepository.saveAll(List.of(admin, adopter));
        }

        petRepository.deleteAll();
        if (petRepository.count() == 0) {
            Pet bella = petFactory.create("Bella", "Golden Retriever", 2.5, "Friendly", "src/main/resources/static/mittens.png");
            Pet max = petFactory.create("Max", "Bulldog", 4.0, "Calm", "src/main/resources/static/max.png");
            Pet luna = petFactory.create("Luna", "Siamese Cat", 1.2, "Playful", "src/main/resources/static/sunny.png");

            petRepository.saveAll(List.of(bella, max, luna));
        }

    }
}