package com.petsystem.pet_project.controller;

import model.HealthRecord;
import model.Pet;
import model.Veterinarian;

import java.util.*;

public class VetController {

    public static void handle(Veterinarian vet, Map<Integer, Pet> petMap) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- domain.Veterinarian Menu ---");
            System.out.println("1. View All Pets");
            System.out.println("2. View Pet Details");
            System.out.println("3. Add Health Record to Pet");
            System.out.println("4. Update Latest Health Record of Pet");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // temizleme

            switch (choice) {
                case 1:
                    vet.viewPets(petMap);
                    break;

                case 2:
                    System.out.print("Enter Pet ID: ");
                    int viewId = scanner.nextInt();
                    scanner.nextLine();
                    vet.viewPetDetails(viewId, petMap);
                    break;

                case 3:
                    System.out.print("Enter Pet ID: ");
                    int addId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Date (e.g., 2025-06-16): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter Description: ");
                    String desc = scanner.nextLine();

                    HealthRecord newRecord = new HealthRecord(date, desc, vet.getName());
                    vet.addHealthRecord(addId, newRecord, petMap);
                    break;

                case 4:
                    System.out.print("Enter Pet ID: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Description: ");
                    String newDesc = scanner.nextLine();
                    System.out.print("Enter New Date: ");
                    String newDate = scanner.nextLine();

                    vet.updateLatestHealthRecord(updateId, newDesc, newDate, petMap);
                    break;

                case 0:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);
    }
}
