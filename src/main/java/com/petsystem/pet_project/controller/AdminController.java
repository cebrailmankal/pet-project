package com.petsystem.pet_project.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/ping")
    public String ping() {
        return "Admin controller is alive";
    }
}
