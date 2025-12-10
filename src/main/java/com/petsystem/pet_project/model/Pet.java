package model;

import java.util.ArrayList;
import java.util.List;

public class Pet {
    private int id;
    private String name;
    private String species;
    private int age;
    private String status;
    private PetOwner owner;
    private List<HealthRecord> healthRecords;

    public Pet(int id, String name, String species, int age,String status, PetOwner owner) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.status = status;
        this.owner = owner;
        this.healthRecords = new ArrayList<>();
    }
    public void addHealthRecord(HealthRecord record) {
        this.healthRecords.add(record);
    }

    public int getId() {
        return id;
    }

    public String getSpecies() {
        return species;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<HealthRecord> getHealthRecords() {
        return healthRecords;
    }

    public PetOwner getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setSpecies(String newSpecies) {
        this.species=newSpecies;
    }

    public void setAge(Integer newAge) {
        this.age = newAge;
    }

    public void setStatus(String newStatus) {
        this.status=newStatus;
    }

    public void setOwner(PetOwner requester) {
        this.owner = requester;

    }
}