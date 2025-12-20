package com.petsystem.pet_project.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "HEALTH_RECORDS")
public class HealthRecord {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Record_ID")
    private Long id;

    @Column(name = "Record_Date", nullable = false)
    private LocalDate date;

    @Column(name = "Record_Description", columnDefinition = "TEXT")
    private String description;

    // FK
    @ManyToOne
    @JoinColumn(name = "Record_PetID", nullable = false)
    private Pet pet;

    // FK
    @ManyToOne
    @JoinColumn(name = "Record_VetID", nullable = false)
    private User vet;

    public HealthRecord() {
    }

    public HealthRecord(LocalDate date, String description, Pet pet, User vet) {
        this.date = date;
        this.description = description;
        this.pet = pet;
        this.vet = vet;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Pet getPet() {
        return pet;
    }
    public void setPet(Pet pet) {
        this.pet = pet;
    }
    public User getVet() {
        return vet;
    }
    public void setVet(User vet) {
        this.vet = vet;
    }
}