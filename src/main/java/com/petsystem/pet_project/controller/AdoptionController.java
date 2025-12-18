package com.petsystem.pet_project.controller;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.service.AdoptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // EKLENEN KISIM: Gelen istekleri listeleme
    @GetMapping("/received")
    public List<AdoptionRequest> getReceivedRequests(@RequestParam String mail) {
        return adoptionService.getReceivedRequests(mail);
    }

    // EKLENEN KISIM: Gönderilen istekleri listeleme
    @GetMapping("/sent")
    public List<AdoptionRequest> getSentRequests(@RequestParam String mail) {
        return adoptionService.getSentRequests(mail);
    }
}