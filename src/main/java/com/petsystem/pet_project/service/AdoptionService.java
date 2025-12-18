package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.AdoptionRequestRepository;
import com.petsystem.pet_project.repository.PetRepository;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

        List<AdoptionRequest> others =
                requestRepository.findByPetAndStatus(
                        pet,
                        AdoptionRequest.Status.PENDING
                );

        for (AdoptionRequest r : others) {
            r.setStatus(AdoptionRequest.Status.REJECTED);
            requestRepository.save(r);
        }
    }

}
