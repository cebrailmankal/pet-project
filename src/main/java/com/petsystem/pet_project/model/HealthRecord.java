package com.petsystem.pet_project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String diagnosis; // Teşhis
    private String treatment; // Tedavi
    private String notes;     // Notlar
    private String vetName;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}