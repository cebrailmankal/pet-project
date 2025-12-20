package com.petsystem.pet_project.repository;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {

    // VIEW REQUESTS
    List<AdoptionRequest> findByPet_OwnerAndStatus(User owner, AdoptionRequest.Status status);

    // CLEANUP
    List<AdoptionRequest> findByPetAndStatus(Pet pet, AdoptionRequest.Status status);

    // SENT REQUESTS
    List<AdoptionRequest> findByRequester(User requester);
}