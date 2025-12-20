package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.Role;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // User
    public User register(String name, String email, String password) {
        if (!email.endsWith("@petowner.com") &&
                !email.endsWith("@vet.com") &&
                !email.endsWith("@admin.com")) {
            throw new  IllegalArgumentException("Invalid email.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("This e-mail already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        if (user.getEmail().endsWith("@petowner.com")) {user.setRole(Role.PET_OWNER);}
        else if (user.getEmail().endsWith("@vet.com")) {user.setRole(Role.VET);}
        else if (user.getEmail().endsWith("@admin.com")) {user.setRole(Role.ADMIN);}

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new RuntimeException("Invalid e-mail or password.");
        }
    }

    public User getUserInfo(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}