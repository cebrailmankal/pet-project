package com.petsystem.pet_project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String name;
    private String species;
    private int age;

    private String status; // AVAILABLE, ADOPTED

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
