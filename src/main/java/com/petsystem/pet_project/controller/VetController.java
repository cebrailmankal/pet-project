package com.petsystem.pet_project.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vets")
public class VetController {

    @GetMapping("/ping")
    public String ping() {
        return "Vet controller is alive";
    }
}
