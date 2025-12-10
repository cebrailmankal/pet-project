package model;

import repository.AdoptionRequestCSVWriter;
import repository.PetFileWriter;
import util.CSVFileUpdater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PetOwner extends User {
    private List<Pet> ownedPets = new ArrayList<>();
    public List<Pet> getOwnedPets() {
        return ownedPets;
    }


    public PetOwner(String name, String email, String password) {
        super(name, email, password);
    }

    public void viewMyPets() {
        if (ownedPets.isEmpty()) {
            System.out.println("You have no pets.");
            return;
        }

        System.out.println("Your Pets:");
        for (int i = 0; i < ownedPets.size(); i++) {
            Pet pet = ownedPets.get(i);
            System.out.println("ID: " + pet.getId() +
                    ", Name: " + pet.getName() +
                    ", Species: " + pet.getSpecies() +
                    ", Age: " + pet.getAge() +
                    ", Status: " + pet.getStatus());
        }
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
                System.out.println("- Date: " + hr.getDate() + ", Vet: " + hr.getVetName() + ", Description: " + hr.getDescription());
            }
        } else {
            System.out.println("Pet not found.");
        }
    }
    public void filterPets(String species, Map<Integer, Pet> petMap) {
        System.out.println("Filtered Pets (Species: " + species + "):");
        List<Pet> petList = new ArrayList<>(petMap.values());
        for (int i = 0; i < petList.size(); i++) {
            Pet pet = petList.get(i);
            if (pet.getSpecies().equalsIgnoreCase(species)) {
                System.out.println("ID: " + pet.getId() + ", Name: " + pet.getName());
            }
        }
    }

    public void listRequestsForMyPets(List<AdoptionRequest> requestList) {
        boolean found = false;

        for (int i = 0; i < requestList.size(); i++) {
            AdoptionRequest req = requestList.get(i);
            Pet pet = req.getPet();

            if (ownedPets.contains(pet) && req.getStatus() == AdoptionRequest.Status.PENDING)
            {
                found = true;
                System.out.println("-------------------------");
                System.out.println("Request for Pet: " + pet.getName());
                System.out.println("Pet İD: " + pet.getId());
                System.out.println("Requester: " + req.getRequester().getEmail());
                System.out.println("Status: " + req.getStatus());
                System.out.println("-------------------------");
            }
        }

        if (!found) {
            System.out.println("You have no adoption requests for your pets.");
        }
    }


    public void sendAdoptionRequest(int petId, Map<Integer, Pet> petMap, List<AdoptionRequest> requestList) {
        Pet pet = petMap.get(petId);

        if (pet == null) {
            System.out.println("Pet not found.");
            return;
        }

        if (ownedPets.contains(pet)) {
            System.out.println("You cannot send a request for your own pet.");
            return;
        }

        if (!pet.getStatus().equalsIgnoreCase("available")) {
            System.out.println("Pet is not available for adoption.");
            return;
        }

        // Yeni isteği oluştur ve listeye ekle
        AdoptionRequest request = new AdoptionRequest(pet, this);
        requestList.add(request);

        System.out.println("Adoption request sent successfully for pet ID: " + petId);
        AdoptionRequestCSVWriter.saveAllRequests("repo/adoption_requests.csv", requestList);
    }

    public void approveRequest(Pet pet, PetOwner requester, List<AdoptionRequest> requestList, Map<Integer, Pet> petMap) {
        if (!ownedPets.contains(pet)) {
            System.out.println("You are not the owner of this pet.");
            return;
        }

        boolean found = false;

        for (int i = 0; i < requestList.size(); i++) {
            AdoptionRequest req = requestList.get(i);

            if (req.getPet() == pet && req.getRequester() == requester) {
                if (req.getStatus() == AdoptionRequest.Status.PENDING) {
                    req.setStatus(AdoptionRequest.Status.APPROVED);
                    pet.setStatus("adopted");
                    pet.setOwner(requester);

                    ownedPets.remove(pet);
                    requester.getOwnedPets().add(pet);

                    System.out.println("Adoption request approved. Pet transferred to new owner: " + requester.getName());
                    found = true;
                } else {
                    System.out.println("This request has already been processed.");
                    return;
                }
            }
        }
        // Diğer başvuruları reddet
        for (int i = 0; i < requestList.size(); i++) {
            AdoptionRequest other = requestList.get(i);
            if (other.getPet() == pet && other.getStatus() == AdoptionRequest.Status.PENDING) {
                other.setStatus(AdoptionRequest.Status.REJECTED);
            }
        }

        pet.setOwner(requester);
        // Değişiklikleri CSV'ye yaz
        AdoptionRequestCSVWriter.saveAllRequests("repo/adoption_requests.csv", requestList);

        if (!found) {
            System.out.println("No matching adoption request found.");
            return;
        }

        // Diğer başvuruları reddet
        for (int i = 0; i < requestList.size(); i++) {
            AdoptionRequest other = requestList.get(i);
            if (other.getPet() == pet && other.getStatus() == AdoptionRequest.Status.PENDING) {
                other.setStatus(AdoptionRequest.Status.REJECTED);
            }
        }
        CSVFileUpdater.transferPetOwnershipInUserCSV("repo/user_data.csv", this, requester, pet.getId());

    }

    public void rejectRequest(Pet pet, PetOwner requester, List<AdoptionRequest> requestList)
    {
        if (!ownedPets.contains(pet)) {
            System.out.println("You are not the owner of this pet.");
            return;
        }

        for (int i = 0; i < requestList.size(); i++) {
            AdoptionRequest req = requestList.get(i);

            if (req.getPet() == pet && req.getRequester() == requester) {
                if (req.getStatus() == AdoptionRequest.Status.PENDING) {
                    req.setStatus(AdoptionRequest.Status.REJECTED);
                    System.out.println("Adoption request rejected.");

                    AdoptionRequestCSVWriter.saveAllRequests("repo/adoption_requests.csv", requestList);
                } else {
                    System.out.println("This request has already been processed.");
                }
                return;
            }
        }

        System.out.println("No matching adoption request found.");
    }

    public void addPet(String name, String species, int age, Map<Integer, Pet> petMap, String petCSVPath) {
        Random random = new Random();
        int id;
        do {
            id = 10000 + random.nextInt(90000);
        } while (petMap.containsKey(id));

        Pet newPet = new Pet(id, name, species, age, "available", this);
        petMap.put(id, newPet);
        ownedPets.add(newPet);

        PetFileWriter.appendPetToCSV(petCSVPath, newPet);
        System.out.println("Pet added to System: " + newPet.getName());
    }

    public void editPet(int petId, String newName, String newSpecies, Integer newAge, String newStatus,
                        Map<Integer, Pet> petMap, String petCSVPath) {
        Pet pet = petMap.get(petId);

        if (pet != null && ownedPets.contains(pet)) {
            // 1. CSV'den eski halini sil
            PetFileWriter.deletePetFromCSV(petCSVPath, petId);

            // 2. Güncelle
            if (newName != null) pet.setName(newName);
            if (newSpecies != null) pet.setSpecies(newSpecies);
            if (newAge != null) pet.setAge(newAge);
            if (newStatus != null) pet.setStatus(newStatus);

            // 3. CSV'ye yeniden yaz
            PetFileWriter.appendPetToCSV(petCSVPath, pet);
            System.out.println("Pet updated and changes written to CSV.");
        } else {
            System.out.println("You cannot edit this pet.");
        }
    }

    public void deletePet(int petId, Map<Integer, Pet> petMap, String petCSVPath) {
        Pet pet = petMap.get(petId);
        if (pet != null && ownedPets.contains(pet)) {
            ownedPets.remove(pet);
            petMap.remove(petId);
            PetFileWriter.deletePetFromCSV(petCSVPath, petId);
            System.out.println("Pet removed from system.");
            CSVFileUpdater.removePendingRequestsForPet("repo/adoption_requests.csv", petId);
            CSVFileUpdater.removePetIdFromUserCSV("repo/user_data.csv", this, petId);
            CSVFileUpdater.removeHealthRecordForPet("repo/user_data.csv", this, petId);


        } else {
            System.out.println("You do not own this pet or it does not exist.");
        }
    }


    @Override
    public void login() {
        System.out.println(getName() + " logged in as Pet Owner.");
    }

    @Override
    public void logout() {
        System.out.println(getName() + " logged out.");
    }
}