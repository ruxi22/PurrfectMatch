package com.wad.firstmvc.controllers;

import com.wad.firstmvc.domain.User;
import com.wad.firstmvc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/new")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "userForm";
    }

    @PostMapping
    public String saveUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users";
    }
}
