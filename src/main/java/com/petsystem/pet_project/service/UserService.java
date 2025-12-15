package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.model.Role;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getByMail(String mail) {
        return userRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can delete users");
        }
        userRepository.deleteById(id);
    }
}
