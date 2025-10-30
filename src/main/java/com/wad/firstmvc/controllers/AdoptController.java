package com.wad.firstmvc.controllers;

import com.wad.firstmvc.domain.Pet;
import com.wad.firstmvc.domain.User;
import com.wad.firstmvc.repositories.PetRepository;
import com.wad.firstmvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class AdoptController {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/adopt")
    public String adopt(Model model) {
        List<Pet> pets = petRepository.findAll();
        System.out.println("Pets found: " + pets.size());
        model.addAttribute("pets", pets);
        return "adopt";
    }

        @PostMapping("/adoption/confirm")
        public String confirmAppointment(@RequestParam Long petId,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDateTime,
                                         Principal principal) {
            String username = principal.getName();
            Optional<User> user = userRepository.findByUsername(username);

            return "redirect:/appointments";
        }


    }
