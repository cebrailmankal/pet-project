package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.Role;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.PetRepository;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository,
                      UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public Pet addPet(Pet pet, String mail) {

        User owner = userRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (owner.getRole() != Role.PET_OWNER) {
            throw new RuntimeException("Only OWNER can add pets");
        }

        pet.setOwner(owner);
        pet.setStatus("AVAILABLE");

        return petRepository.save(pet);
    }

    public List<Pet> getAvailablePets() {
        return petRepository.findByStatus("AVAILABLE");
    }

    public List<Pet> getMyPets(String mail) {

        User owner = userRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return petRepository.findByOwner(owner);
    }

    public void deletePet(Long petId, String mail) {

        User owner = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You can only delete your own pets");
        }

        petRepository.delete(pet);
    }
}
