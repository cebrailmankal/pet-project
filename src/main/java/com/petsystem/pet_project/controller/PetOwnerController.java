package com.petsystem.pet_project.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
public class PetOwnerController {

    @GetMapping("/ping")
    public String ping() {
        return "PetOwner controller is alive";
    }
}
