package com.petsystem.pet_project.repository;

import com.petsystem.pet_project.model.AdoptionRequest;
import com.petsystem.pet_project.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionRequestRepository
        extends JpaRepository<AdoptionRequest, Long> {

    List<AdoptionRequest> findByPetAndStatus(
            Pet pet,
            AdoptionRequest.Status status
    );
}
