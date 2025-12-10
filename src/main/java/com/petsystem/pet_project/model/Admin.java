package com.petsystem.pet_project.model;

import util.CSVFileUpdater;

import java.util.List;
import java.util.Map;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void login() {
        System.out.println(getName() + " logged in as domain.Admin.");
    }

    @Override
    public void logout() {
        System.out.println(getName() + " logged out.");
    }

    public void viewUsers(List<User> allUsers) {
        System.out.println("All Registered Users:");
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            System.out.println("- Name: " + user.getName() + ", Email: " + user.getEmail());
        }
    }

    public void searchUserDetails(String email, List<User> allUsers) {
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            if (user.getEmail().equalsIgnoreCase(email)) {
                System.out.println("domain.User found:");
                System.out.println("Name: " + user.getName());
                System.out.println("Email: " + user.getEmail());

                if (user instanceof PetOwner) {
                    System.out.println("Type: domain.PetOwner");
                    List<Pet> pets = ((PetOwner) user).getOwnedPets();
                    System.out.println("Owned Pets: " + (pets.isEmpty() ? "None" : ""));
                    for (int j = 0; j < pets.size(); j++) {
                        Pet p = pets.get(j);
                        System.out.println("  - domain.Pet ID: " + p.getId() + ", Name: " + p.getName());
                    }
                } else if (user instanceof Veterinarian) {
                    System.out.println("Type: domain.Veterinarian");
                } else if (user instanceof Admin) {
                    System.out.println("Type: domain.Admin");
                }
            }
        }

        System.out.println("domain.User with email " + email + " not found.");
    }




    public void removeUser(String email, List<User> allUsers, Map<Integer, Pet> petMap,
                           String userCSVPath, String petCSVPath) {

        User target = null;

        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            if (user.getEmail().equalsIgnoreCase(email)) {
                target = user;
                break;
            }
        }

        if (target == null) {
            System.out.println("No user with email " + email + " found.");
            return;
        }

        if (!(target instanceof PetOwner)) {
            System.out.println("You are not allowed to remove non-domain.PetOwner users.");
            return;
        }

        allUsers.remove(target);
        PetOwner owner = (PetOwner) target;

        CSVFileUpdater.removeUserAndTheirPets(userCSVPath, petCSVPath, owner, petMap);
    }
}
