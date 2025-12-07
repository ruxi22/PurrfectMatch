package com.purrfect.auth_service.controller;

import com.purrfect.auth_service.domain.Role;
import com.purrfect.auth_service.domain.User;
import com.purrfect.auth_service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //registration endpoint
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.ADOPTER);
        }

        return userService.save(user);
    }

    //login endpoint
    @PostMapping("/login")
    public User login(@RequestBody User loginData) {

        Optional<User> found = userService.findByUsername(loginData.getUsername());

        if (found.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = found.get();

        if (!passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
