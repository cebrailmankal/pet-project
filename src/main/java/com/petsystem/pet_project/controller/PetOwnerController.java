package com.petsystem.pet_project.controller;

import model.AdoptionRequest;
import model.Pet;
import model.PetOwner;

import java.util.*;

public class PetOwnerController {

    public static void handle(PetOwner owner,
                              Map<Integer, Pet> petMap,
                              List<AdoptionRequest> requestList,
                              String petCSVPath) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Pet Owner Menu ---");
            System.out.println("1. View All Pets");
            System.out.println("2. Filter Pets by Species");
            System.out.println("3. View Pet Details (after viewing all)");
            System.out.println("4. Send Adoption Request");
            System.out.println("5. Manage Incoming Requests");
            System.out.println("6. Manage My Pets (Add/Edit/Delete)");
            System.out.println("7. View My Pets");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    owner.viewPets(petMap);
                    break;

                case 2:
                    System.out.print("Enter species to filter: ");
                    String species = scanner.nextLine();
                    owner.filterPets(species, petMap);
                    break;

                case 3:
                    System.out.print("Enter Pet ID to view details: ");
                    int viewId = scanner.nextInt();
                    scanner.nextLine();
                    owner.viewPetDetails(viewId, petMap);
                    break;

                case 4:
                    System.out.print("Enter Pet ID to adopt: ");
                    int adoptId = scanner.nextInt();
                    scanner.nextLine();
                    owner.sendAdoptionRequest(adoptId, petMap, requestList);
                    break;

                case 5:
                    handleRequestManagement(owner, requestList, petMap);
                    break;

                case 6:
                    handlePetManagement(owner, petMap, petCSVPath, scanner);
                    break;
                case 7:
                    owner.viewMyPets();
                    break;

                case 0:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid option.");
            }

        } while (choice != 0);
    }

    private static void handleRequestManagement(PetOwner owner, List<AdoptionRequest> requestList, Map<Integer, Pet> petMap) {
        Scanner scanner = new Scanner(System.in);

        owner.listRequestsForMyPets(requestList);

        System.out.println("Do you want to approve/reject a request? (yes/no)");
        String answer = scanner.nextLine();
        if (!answer.equalsIgnoreCase("yes")) return;

        System.out.print("Enter Pet ID: ");
        int petId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Requester's Email: ");
        String email = scanner.nextLine();

        Pet pet = petMap.get(petId);
        PetOwner requester = null;

        for (AdoptionRequest req : requestList) {
            if (req.getPet().getId() == petId &&
                    req.getRequester().getEmail().equalsIgnoreCase(email)) {
                requester = req.getRequester();
                break;
            }
        }

        if (requester == null) {
            System.out.println("Request not found.");
            return;
        }

        System.out.print("Approve or Reject? (a/r): ");
        String decision = scanner.nextLine();
        if (decision.equalsIgnoreCase("a")) {
            owner.approveRequest(pet, requester, requestList, petMap);
        } else if (decision.equalsIgnoreCase("r")) {
            owner.rejectRequest(pet, requester, requestList);
        } else {
            System.out.println("Invalid decision.");
        }
    }

    private static void handlePetManagement(PetOwner owner, Map<Integer, Pet> petMap, String petCSVPath, Scanner scanner) {
        int choice;
        do {
            System.out.println("\n--- Manage My Pets ---");
            System.out.println("1. Add New Pet");
            System.out.println("2. Edit Existing Pet");
            System.out.println("3. Delete Pet");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Pet Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Species: ");
                    String species = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    owner.addPet(name, species, age, petMap, petCSVPath);
                    break;

                case 2:
                    System.out.print("Pet ID to edit: ");
                    int editId = scanner.nextInt();
                    scanner.nextLine(); // newline karakterini tüket

                    Pet petToEdit = petMap.get(editId);
                    if (petToEdit == null || !owner.getOwnedPets().contains(petToEdit)) {
                        System.out.println("You do not own this pet or pet does not exist.");
                        break;
                    }

                    // Kullanıcıdan yeni bilgileri al
                    System.out.print("New Name (or leave empty): ");
                    String newName = scanner.nextLine();
                    System.out.print("New Species (or leave empty): ");
                    String newSpecies = scanner.nextLine();
                    System.out.print("New Age (or -1 to skip): ");
                    int newAge = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("New Status (or leave empty): ");
                    String newStatus = scanner.nextLine();

                    owner.editPet(
                            editId,
                            newName.isEmpty() ? null : newName,
                            newSpecies.isEmpty() ? null : newSpecies,
                            newAge < 0 ? null : newAge,
                            newStatus.isEmpty() ? null : newStatus,
                            petMap,
                            petCSVPath
                    );
                   break;

                case 3:
                    System.out.print("Pet ID to delete: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();
                    owner.deletePet(deleteId, petMap, petCSVPath);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }
}
