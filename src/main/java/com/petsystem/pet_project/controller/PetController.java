package com.petsystem.pet_project.controller;

import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public Pet addPet(@RequestBody Pet pet,
                      @RequestParam String mail) {
        return petService.addPet(pet, mail);
    }

    @GetMapping("/available")
    public List<Pet> availablePets() {
        return petService.getAvailablePets();
    }

    @GetMapping("/my")
    public List<Pet> myPets(@RequestParam String mail) {
        return petService.getMyPets(mail);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id,
                          @RequestParam String mail) {
        petService.deletePet(id, mail);
    }
}
