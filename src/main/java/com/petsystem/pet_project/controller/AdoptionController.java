package com.petsystem.pet_project.controller;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.service.AdoptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adoptions")
@CrossOrigin(origins = "*")
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @PostMapping
    public AdoptionRequest requestAdoption(@RequestParam Long petId,
                                           @RequestParam String mail) {
        return adoptionService.requestAdoption(petId, mail);
    }

    @PostMapping("/{id}/approve")
    public void approve(@PathVariable Long id,
                        @RequestParam String mail) {
        adoptionService.approveRequest(id, mail);
    }
    @PostMapping("/{id}/reject")
    public void reject(@PathVariable Long id, @RequestParam String mail) {
        adoptionService.rejectRequest(id, mail);
    }
}
