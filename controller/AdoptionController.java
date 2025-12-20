package com.petsystem.pet_project.controller;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.service.AdoptionService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> requestAdoption(@RequestParam Long petId, @RequestParam String mail) {
        try {
            return ResponseEntity.ok(adoptionService.sendRequest(petId, mail));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id, @RequestParam String mail) {
        try {
            adoptionService.approveRequest(id, mail);
            return ResponseEntity.ok("Request approved, owner is changed.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id, @RequestParam String mail) {
        try {
            adoptionService.rejectRequest(id, mail);
            return ResponseEntity.ok("Request rejected.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/received")
    public List<AdoptionRequest> getIncomingRequests(@RequestParam String mail) {
        return adoptionService.getIncomingRequests(mail);
    }

    @GetMapping("/sent")
    public List<AdoptionRequest> getMyRequests(@RequestParam String mail) {
        return adoptionService.getMyRequests(mail);
    }
}