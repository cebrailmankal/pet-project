package model;

public class AdoptionRequest {
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    private Pet pet;
    private PetOwner requester;
    private Status status;

    public AdoptionRequest(Pet pet, PetOwner requester) {
        this.pet = pet;
        this.requester = requester;
        this.status = Status.PENDING;
    }

    public Pet getPet() {
        return pet;
    }

    public PetOwner getRequester() {
        return  requester;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "model.AdoptionRequest{" +
                ", pet=" + pet.getName() +
                ", requester=" + requester.getName() +
                ", statusss=" + status +
                '}';
    }
}
