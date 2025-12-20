package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.AdoptionRequestRepository;
import com.petsystem.pet_project.repository.PetRepository;
import com.petsystem.pet_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AdoptionService {
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public AdoptionService(AdoptionRequestRepository adoptionRequestRepository, PetRepository petRepository, UserRepository userRepository) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public AdoptionRequest sendRequest(Long petId, String requesterEmail) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("No pet found with id: " +  petId));
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("No user found with email: " +  requesterEmail));

        if (pet.getOwner().getId().equals(requester.getId())) {
            throw new RuntimeException("You cannot sent request to yourself.");
        }
        if (!pet.getStatus()) {
            throw new RuntimeException("This pet is not currently available for adoption.");
        }

        AdoptionRequest request = new AdoptionRequest();
        request.setPet(pet);
        request.setRequester(requester);
        request.setStatus(AdoptionRequest.Status.PENDING);

        return adoptionRequestRepository.save(request);
    }

    @Transactional
    public void approveRequest(Long requestId, String currentOwnerEmail) {
        AdoptionRequest request = adoptionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("No request found with id: " +  requestId));
        User currentOwner = userRepository.findByEmail(currentOwnerEmail)
                .orElseThrow(() -> new RuntimeException("No user found with email: " +  currentOwnerEmail));

        if (!request.getPet().getOwner().getId().equals(currentOwner.getId())) {
            throw new RuntimeException("You don't have permission to perform this action!");
        }

        request.setStatus(AdoptionRequest.Status.APPROVED);
        adoptionRequestRepository.save(request);

        Pet pet = request.getPet();
        pet.setOwner(request.getRequester());
        pet.setStatus(false);
        petRepository.save(pet);

        List<AdoptionRequest> pendingRequests = adoptionRequestRepository.findByPetAndStatus(pet, AdoptionRequest.Status.PENDING);
        for (AdoptionRequest pending : pendingRequests) {
            if (!pending.getId().equals(requestId)) {
                pending.setStatus(AdoptionRequest.Status.REJECTED);
                adoptionRequestRepository.save(pending);
            }
        }
    }

    public void rejectRequest(Long requestId, String currentOwnerEmail) {
        AdoptionRequest request = adoptionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("No request found with id: " +  requestId));
        User currentOwner = userRepository.findByEmail(currentOwnerEmail)
                .orElseThrow(() -> new RuntimeException("No user found with email: " +  currentOwnerEmail));

        if (!request.getPet().getOwner().getId().equals(currentOwner.getId())) {
            throw new RuntimeException("You do not have permission to perform this action!");
        }

        request.setStatus(AdoptionRequest.Status.REJECTED);
        adoptionRequestRepository.save(request);
    }

    public List<AdoptionRequest> getIncomingRequests(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("No user found with email: " +  ownerEmail));
        return adoptionRequestRepository.findByPet_OwnerAndStatus(owner, AdoptionRequest.Status.PENDING);
    }

    public List<AdoptionRequest> getMyRequests(String requesterEmail) {
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("No user found with email: " +  requesterEmail));
        return adoptionRequestRepository.findByRequester(requester);
    }
}