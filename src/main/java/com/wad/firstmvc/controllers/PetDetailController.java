package com.wad.firstmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PetDetailController {

    @GetMapping("/pet")
    public String pet() {
        return "pet";
    }
}