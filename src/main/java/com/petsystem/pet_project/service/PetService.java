package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.model.User.Role;
import com.petsystem.pet_project.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet addPet(Pet pet, User currentUser) {
        if (currentUser.getRole() != Role.OWNER) {
            throw new RuntimeException("Only PET OWNER can add pets");
        }
        pet.setOwner(currentUser);
        pet.setStatus("AVAILABLE");
        return petRepository.save(pet);
    }

    public List<Pet> getMyPets(User currentUser) {
        if (currentUser.getRole() != Role.OWNER) {
            throw new RuntimeException("Only PET OWNER can view their pets");
        }
        return petRepository.findByOwner(currentUser);
    }

    public List<Pet> getAvailablePets() {
        return petRepository.findByStatus("AVAILABLE");
    }

    public void deletePet(Long petId, User currentUser) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (currentUser.getRole() != Role.OWNER) {
            throw new RuntimeException("Only PET OWNER can delete pets");
        }

        if (!pet.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own pets");
        }

        petRepository.delete(pet);
    }
}
