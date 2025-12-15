package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.Role;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public Role resolveRoleFromMail(String mail) {

        if (mail.endsWith("@admin.com")) {
            return Role.ADMIN;
        }

        if (mail.endsWith("@petowner.com")) {
            return Role.OWNER;
        }

        if (mail.endsWith("@vet.com")) {
            return Role.VET;
        }

        throw new RuntimeException("Invalid mail domain");
    }
}
