package com.petsystem.pet_project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PETS")
public class Pet {

    //  PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pet_ID")
    private Long id;

    @Column(name = "Pet_Name", nullable = false)
    private String name;

    @Column(name = "Pet_Species", nullable = false)
    private String species;

    @Column(name = "Pet_Age", nullable = false)
    private int age;

    // True=Available, False=Not Available
    @Column(name = "Pet_Status", nullable = false)
    private boolean status;

    @Column(name = "Pet_Image", nullable = false)
    private String image;

    // FK
    @ManyToOne
    @JoinColumn(name = "Pet_Owner")
    private User owner;

    public Pet() {
    }

    public Pet(String name, String species, int age, boolean status, String image, User owner) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.status = status;
        this.image = image;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
}