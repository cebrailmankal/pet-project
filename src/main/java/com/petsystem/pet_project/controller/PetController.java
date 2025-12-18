package com.petsystem.pet_project.controller;

import com.petsystem.pet_project.model.HealthRecord;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.service.PetService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Fotoğraflı Ekleme
    @PostMapping
    public Pet addPet(@RequestParam("name") String name,
                      @RequestParam("species") String species,
                      @RequestParam("age") int age,
                      @RequestParam(value = "image", required = false) MultipartFile image,
                      @RequestParam("mail") String mail) throws IOException {
        return petService.addPet(name, species, age, image, mail);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id, @RequestParam String mail) {
        petService.deletePet(id, mail);
    }

    @GetMapping("/available")
    public List<Pet> availablePets() { return petService.getAvailablePets(); }

    @GetMapping("/my")
    public List<Pet> myPets(@RequestParam String mail) { return petService.getMyPets(mail); }

    // --- SAĞLIK KAYITLARI ---
    @PostMapping("/{petId}/health-records")
    public HealthRecord addHealthRecord(@PathVariable Long petId, @RequestBody HealthRecord record, @RequestParam String mail) {
        return petService.addHealthRecord(petId, record, mail);
    }

    @PutMapping("/health-records/{recordId}")
    public HealthRecord updateHealthRecord(@PathVariable Long recordId, @RequestBody HealthRecord record, @RequestParam String mail) {
        return petService.updateHealthRecord(recordId, record, mail);
    }

    @DeleteMapping("/health-records/{recordId}")
    public void deleteHealthRecord(@PathVariable Long recordId, @RequestParam String mail) {
        petService.deleteHealthRecord(recordId, mail);
    }

    // Veterinerin kendi kayıtlarını görmesi için
    @GetMapping("/health-records/by-vet")
    public List<HealthRecord> getVetRecords(@RequestParam String mail) {
        return petService.getRecordsByVet(mail);
    }

    @GetMapping("/{petId}/health-records")
    public List<HealthRecord> getHealthRecords(@PathVariable Long petId) {
        return petService.getHealthRecords(petId);
    }
}