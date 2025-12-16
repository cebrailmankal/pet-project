package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.Role;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        // mail'e göre rol belirleme
        if (user.getEmail().endsWith("@petowner.com")) {
            user.setRole(Role.PET_OWNER);
        } else if (user.getEmail().endsWith("@vet.com")) {
            user.setRole(Role.VET);
        } else {
            user.setRole(Role.ADMIN);
        }

        return userRepository.save(user);
    }
}