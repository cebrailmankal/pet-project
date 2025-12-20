package com.petsystem.pet_project.controller;

import com.petsystem.pet_project.model.HealthRecord;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // --- PET İŞLEMLERİ ---

    @PostMapping
    public Pet addPet(@RequestParam("name") String name, @RequestParam("species") String species, @RequestParam("age") int age, @RequestParam(value = "image", required = false) String image, @RequestParam("mail") String mail) {
        return petService.addPet(name, species, age, image, mail);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id, @RequestParam String mail) {
        petService.deletePet(id, mail);
    }

    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestParam("name") String name, @RequestParam("species") String species, @RequestParam("age") int age, @RequestParam(value = "image", required = false) String image, @RequestParam("mail") String mail) {
        return petService.updatePet(id, name, species, age, image, mail);
    }

    @GetMapping("/available")
    public List<Pet> availablePets() {
        return petService.getAllAvailablePets(); // Metod ismi serviste böyle
    }

    @GetMapping("/my")
    public List<Pet> myPets(@RequestParam String mail) {
        return petService.getMyPets(mail);
    }


    // Health Records
    @PostMapping("/{petId}/health-records")
    public HealthRecord addHealthRecord(@PathVariable Long petId, @RequestParam String description, @RequestParam String mail) {
        return petService.addHealthRecord(petId, description, mail);
    }

    @PutMapping("/{petId}/health-records/latest")
    public HealthRecord updateLatestHealthRecord(@PathVariable Long petId, @RequestParam String description, @RequestParam String mail) {
        return petService.updateLatestRecord(petId, description, mail);
    }

    @GetMapping("/health-records/by-vet")
    public List<HealthRecord> getVetRecords(@RequestParam String mail) {
        return petService.getRecordsByVet(mail);
    }

    @GetMapping("/{petId}/health-records")
    public List<HealthRecord> getHealthRecords(@PathVariable Long petId) {
        return petService.getPetHealthHistory(petId); // Metod ismi serviste böyle
    }
}