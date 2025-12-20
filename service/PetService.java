package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.HealthRecord;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.Role;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.HealthRecordRepository;
import com.petsystem.pet_project.repository.PetRepository;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final HealthRecordRepository healthRecordRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository, HealthRecordRepository healthRecordRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    // Pet
    public Pet addPet(String name, String species, int age, String image, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("No user found with email: " + ownerEmail));
        if (owner.getRole() != Role.PET_OWNER) {
            throw new RuntimeException("Only pet owners can add pets.");
        }

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setAge(age);
        pet.setOwner(owner);
        pet.setStatus(true);
        if (image.isEmpty()) {image = "default.png";}
        pet.setImage(image);

        return petRepository.save(pet);
    }

    public Pet updatePet(Long petId, String name, String species, int age, String image, String ownerEmail) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("No pet found with id: " + petId));
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow(() -> new RuntimeException("No user found with email: " + ownerEmail));

        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You don't have permission to update this pet.");
        }

        pet.setName(name);
        pet.setSpecies(species);
        pet.setAge(age);

        if (image != null && !image.isEmpty()) {
            pet.setImage(image);
        }

        return petRepository.save(pet);
    }

    public void deletePet(Long petId, String ownerEmail) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("No pet found with id: " + petId));
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow(() -> new RuntimeException("No user found with email: " + ownerEmail));

        if (pet.getOwner().getId().equals(owner.getId())) {
            petRepository.delete(pet);
        }
        else {
            throw new RuntimeException("You don't have permission to delete this pet.");
        }
    }

    public List<Pet> getAllAvailablePets() {
        return petRepository.findByStatus(true);
    }

    public List<Pet> getMyPets(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow(() -> new RuntimeException("No user found with email: " + ownerEmail));
        return petRepository.findByOwner(owner);
    }

    public List<Pet> filterBySpecies(String species) {
        return petRepository.findBySpecies(species);
    }

    public Pet getPetDetails(Long petId) {
        return petRepository.findById(petId).orElseThrow(() -> new RuntimeException("No pet found with id: " + petId));
    }

    // Health Record
    public HealthRecord addHealthRecord(Long petId, String description, String vetEmail) {
        User vet = userRepository.findByEmail(vetEmail).orElseThrow(() -> new RuntimeException("No user found with email: " + vetEmail));

        if (vet.getRole() != Role.VET) {
            throw new RuntimeException("Only vets can make changes on health records.");
        }

        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("No pet found with id: " + petId));

        HealthRecord record = new HealthRecord();
        record.setPet(pet);
        record.setVet(vet);
        record.setDescription(description);
        record.setDate(LocalDate.now());

        return healthRecordRepository.save(record);
    }

    public HealthRecord updateLatestRecord(Long petId, String newDescription, String vetEmail) {
        User vet = userRepository.findByEmail(vetEmail).orElseThrow(() -> new RuntimeException("No user found with email: " + vetEmail));
        if (vet.getRole() != Role.VET) throw new RuntimeException("Only vets can make changes on health records.");

        HealthRecord latestRecord = healthRecordRepository.findFirstByPetIdOrderByDateDesc(petId)
                .orElseThrow(() -> new RuntimeException("There is no health records for " + petId));

        latestRecord.setDescription(newDescription);
        latestRecord.setDate(LocalDate.now());

        return healthRecordRepository.save(latestRecord);
    }

    public List<HealthRecord> getPetHealthHistory(Long petId) {
        return healthRecordRepository.findByPetId(petId);
    }

    public List<HealthRecord> getRecordsByVet(String vetEmail) {
        User vet = userRepository.findByEmail(vetEmail).orElseThrow(() -> new RuntimeException("No user found with email: " + vetEmail));
        return healthRecordRepository.findByVet(vet);
    }
}