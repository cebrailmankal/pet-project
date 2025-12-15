package com.petsystem.pet_project.repository;

import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwner(User owner);

    List<Pet> findByStatus(String status);
}
