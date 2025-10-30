package com.wad.firstmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wad.firstmvc.domain.Role;
import com.wad.firstmvc.domain.User;
import com.wad.firstmvc.repositories.UserRepository;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password) {
        User user = new User(username, passwordEncoder.encode(password), Role.ADOPTER);
        userRepository.save(user);

        return "redirect:/login?registered";
    }

}
