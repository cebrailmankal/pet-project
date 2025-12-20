package com.petsystem.pet_project.repository;

import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // VIEW MY PETS
    List<Pet> findByOwner(User owner);

    // FILTER PETS
    List<Pet> findBySpecies(String species);

    // STATUS FILTER
    List<Pet> findByStatus(boolean status);
}