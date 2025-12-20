package com.petsystem.pet_project.model;

import jakarta.persistence.*;


@Entity
@Table(name = "ADOPTION_REQUESTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"Request_PetID", "Request_OwnerID"})})
public class AdoptionRequest {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Request_ID")
    private Long id;

    // FK
    @ManyToOne
    @JoinColumn(name = "Request_PetID", nullable = false)
    private Pet pet;

    // FK
    @ManyToOne
    @JoinColumn(name = "Request_OwnerID", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "Request_Status", nullable = false)
    private Status status;
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    public AdoptionRequest() {
    }

    public AdoptionRequest(Pet pet, User requester, Status status) {
        this.pet = pet;
        this.requester = requester;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }
    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public User getRequester() {
        return requester;
    }
    public void setRequester(User requester) {
        this.requester = requester;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
