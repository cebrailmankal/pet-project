package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.model.AdoptionRequest.Status;
import com.petsystem.pet_project.repository.AdoptionRequestRepository;
import com.petsystem.pet_project.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService {

    private final AdoptionRequestRepository requestRepository;
    private final PetRepository petRepository;

    public AdoptionService(AdoptionRequestRepository requestRepository,
                           PetRepository petRepository) {
        this.requestRepository = requestRepository;
        this.petRepository = petRepository;
    }

    public AdoptionRequest createRequest(Long petId, User requester) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getStatus().equals("AVAILABLE")) {
            throw new RuntimeException("Pet is not available");
        }

        if (pet.getOwner().getId().equals(requester.getId())) {
            throw new RuntimeException("You cannot adopt your own pet");
        }

        AdoptionRequest request = new AdoptionRequest();
        request.setPet(pet);
        request.setRequester(requester);
        request.setStatus(Status.PENDING);

        return requestRepository.save(request);
    }

    public void approveRequest(Long requestId, User owner) {
        AdoptionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Pet pet = request.getPet();

        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Only owner can approve");
        }

        pet.setOwner(request.getRequester());
        pet.setStatus("ADOPTED");
        petRepository.save(pet);

        request.setStatus(Status.APPROVED);
        requestRepository.save(request);

        // Diğer istekleri reddet
        List<AdoptionRequest> others =
                requestRepository.findByPetAndStatus(pet, Status.PENDING);

        for (AdoptionRequest r : others) {
            r.setStatus(Status.REJECTED);
            requestRepository.save(r);
        }
    }
}
