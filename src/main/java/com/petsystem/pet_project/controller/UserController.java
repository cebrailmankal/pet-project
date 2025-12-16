package com.petsystem.pet_project.controller;

import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/ping")
    public String ping() {
        return "users controller alive";
    }
}
