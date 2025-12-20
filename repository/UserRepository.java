package com.petsystem.pet_project.repository;

import com.petsystem.pet_project.model.Role;
import com.petsystem.pet_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // VIEW USER WITH MAIL
    Optional<User> findByEmail(String email);

    // LOGIN
    Optional<User> findByEmailAndPassword(String email, String password);

    // VIEW ALL USERS WHERE ROLE
    List<User> findByRole(Role role);
}