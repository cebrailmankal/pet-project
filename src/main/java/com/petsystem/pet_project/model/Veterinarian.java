package com.petsystem.pet_project.model;

import repository.HealthRecordFileWriter;

import java.util.*;

public class Veterinarian extends User {
    private List<Pet> assignedPets = new ArrayList<>();

    public Veterinarian(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void login() {
        System.out.println(getName() + " logged in as domain.Veterinarian.");
    }

    @Override
    public void logout() {
        System.out.println(getName() + " logged out.");
    }

    public void viewPets(Map<Integer, Pet> petMap) {
        System.out.println("All Pets:");
        List<Pet> petList = new ArrayList<>(petMap.values());
        for (int i = 0; i < petList.size(); i++) {
            Pet pet = petList.get(i);
            System.out.println("ID: " + pet.getId() + ", Name: " + pet.getName());
        }
    }


    public void viewPetDetails(int petId, Map<Integer, Pet> petMap) {
        Pet pet = petMap.get(petId);
        if (pet != null) {
            System.out.println("ID: " + pet.getId());
            System.out.println("Name: " + pet.getName());
            System.out.println("Species: " + pet.getSpecies());
            System.out.println("Age: " + pet.getAge());
            System.out.println("Status: " + pet.getStatus());

            System.out.println("Health Records:");
            List<HealthRecord> records = pet.getHealthRecords();
            for (int i = 0; i < records.size(); i++) {
                HealthRecord hr = records.get(i);
                System.out.println("- Date: " + hr.getDate() +
                        ", Vet: " + hr.getVetName() +
                        ", Description: " + hr.getDescription());
            }
        } else {
            System.out.println("No pet found with ID " + petId);
        }
    }


    public void addHealthRecord(int petId, HealthRecord record, Map<Integer, Pet> petMap) {
        Pet pet = petMap.get(petId);
        if (pet != null) {
            pet.addHealthRecord(record);
            HealthRecordFileWriter.addRecordToCSV("repo/health_record.csv", petId, record);
            System.out.println("Health record added to pet ID " + petId);
        } else {
            System.out.println("domain.Pet not found.");
        }
    }

    public void updateLatestHealthRecord(int petId, String newDesc, String newDate, Map<Integer, Pet> petMap) {
        Pet pet = petMap.get(petId);
        if (pet != null) {
            List<HealthRecord> records = pet.getHealthRecords();
            if (!records.isEmpty()) {
                HealthRecord latestRecord = records.get(records.size() - 1);
                latestRecord.setDescription(newDesc);
                latestRecord.setDate(newDate);
                HealthRecordFileWriter.updateLatestRecordInCSV("health_records.csv", petId, latestRecord);
                System.out.println("Latest health record updated for pet ID " + petId);
            } else {
                System.out.println("This pet has no health records to update.");
            }
        } else {
            System.out.println("domain.Pet not found.");
        }
    }
}
