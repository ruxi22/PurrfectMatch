package com.purrfect.frontend_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FrontendController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/adopt")
    public String adopt() {
        return "adopt";
    }

    @GetMapping("/pet")
    public String petDetails() {
        return "pet";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/signup")
    public String signup() {
        return "register";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/admin/pets/new")
    public String addPet() {
        return "add-pet";
    }

    @GetMapping("/admin/pets/edit")
    public String editPet() {
        return "edit-pet";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }
}


