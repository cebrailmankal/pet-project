package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.AdoptionRequestRepository;
import com.petsystem.pet_project.repository.PetRepository;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdoptionService {

    private final AdoptionRequestRepository requestRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public AdoptionService(AdoptionRequestRepository requestRepository,
                           PetRepository petRepository,
                           UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public AdoptionRequest requestAdoption(Long petId, String mail) {
        User requester = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!"AVAILABLE".equals(pet.getStatus())) {
            throw new RuntimeException("Pet is not available");
        }

        AdoptionRequest request = new AdoptionRequest();
        request.setPet(pet);
        request.setRequester(requester);
        request.setStatus(AdoptionRequest.Status.PENDING);

        return requestRepository.save(request);
    }

    public void rejectRequest(Long requestId, String mail) {
        User owner = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdoptionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Pet pet = request.getPet();
        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Only owner can reject requests");
        }

        request.setStatus(AdoptionRequest.Status.REJECTED);
        requestRepository.save(request);
    }

    public void approveRequest(Long requestId, String mail) {
        User owner = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdoptionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Pet pet = request.getPet();
        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Only owner can approve");
        }

        pet.setOwner(request.getRequester());
        pet.setStatus("ADOPTED");
        petRepository.save(pet);

        request.setStatus(AdoptionRequest.Status.APPROVED);
        requestRepository.save(request);

        // Diğer bekleyen istekleri reddet
        List<AdoptionRequest> others = requestRepository.findByPetAndStatus(pet, AdoptionRequest.Status.PENDING);
        for (AdoptionRequest r : others) {
            r.setStatus(AdoptionRequest.Status.REJECTED);
            requestRepository.save(r);
        }
    }

    // YENİ: Bana gelen istekler (Benim petlerime yapılanlar)
    public List<AdoptionRequest> getReceivedRequests(String mail) {
        User owner = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Basit bir yöntem: Tüm petlerimi bul, onlara gelen requestleri topla
        List<Pet> myPets = petRepository.findByOwner(owner);
        List<AdoptionRequest> allRequests = requestRepository.findAll();

        return allRequests.stream()
                .filter(r -> myPets.contains(r.getPet()))
                .collect(Collectors.toList());
    }

    // YENİ: Benim gönderdiğim istekler
    public List<AdoptionRequest> getSentRequests(String mail) {
        User requester = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return requestRepository.findAll().stream()
                .filter(r -> r.getRequester().getId().equals(requester.getId()))
                .collect(Collectors.toList());
    }
}