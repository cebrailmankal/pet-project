package com.petsystem.pet_project.controller;

import model.Admin;
import model.Pet;
import model.User;

import java.util.*;

public class AdminController {

    public static void handle(Admin admin, List<User> allUsers, Map<Integer, Pet> petMap,
                              String userCSVPath, String petCSVPath) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- domain.Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Search domain.User by Email");
            System.out.println("3. Remove a PetOwner");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // dummy

            switch (choice) {
                case 1:
                    admin.viewUsers(allUsers);
                    break;

                case 2:
                    System.out.print("Enter email to search: ");
                    String searchEmail = scanner.nextLine();
                    admin.searchUserDetails(searchEmail, allUsers);
                    break;

                case 3:
                    System.out.print("Enter email of PetOwner to remove: ");
                    String removeEmail = scanner.nextLine();
                    admin.removeUser(removeEmail, allUsers, petMap, userCSVPath, petCSVPath);
                    break;

                case 0:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid option.");
            }

        } while (choice != 0);
    }
}
