package com.eventix.user_service.controller;

import com.eventix.user_service.dto.UserRegistrationDTO;
import com.eventix.user_service.model.User;
import com.eventix.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody UserRegistrationDTO dto) {
        return userService.registerUser(dto);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUserById(userId).orElse(null);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody UserRegistrationDTO dto) {
        return userService.updateUser(userId, dto);
    }

    @DeleteMapping("/{userId}")
    public void deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
    }

    @GetMapping("/me")
    public User getCurrentUser() {
        return userService.getCurrentUser().orElse(null);
    }
}
