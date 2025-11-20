package com.eventix.user_service.controller;

import com.eventix.user_service.dto.UserRegistrationDTO;
import com.eventix.user_service.exception.RateLimitExceededException;
import com.eventix.user_service.model.User;
import com.eventix.user_service.service.UserService;
import com.eventix.user_service.exception.NotFoundException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Duration;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Rate limiting: 10 registrations per hour per IP
    private final Bucket registrationBucket = Bucket.builder()
        .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1))))
        .build();

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO dto, HttpServletRequest request) {
        if (!registrationBucket.tryConsume(1)) {
            throw new RateLimitExceededException("Too many registration attempts. Please try again later.");
        }
        
        User user = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId).orElseThrow(
            () -> new NotFoundException("User not found with id: " + userId)
        );
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRegistrationDTO dto) {
        User user = userService.updateUser(userId, dto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.ok("User deactivated successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser().orElseThrow(
            () -> new NotFoundException("Current user not found")
        );
        return ResponseEntity.ok(user);
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validateUser(@PathVariable Long userId) {
        boolean exists = userService.getUserById(userId).isPresent();
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<java.util.Map<String, String>> getUserDetails(@PathVariable Long userId) {
        User user = userService.getUserById(userId).orElseThrow(
            () -> new NotFoundException("User not found with id: " + userId)
        );
        java.util.Map<String, String> details = new java.util.HashMap<>();
        details.put("id", user.getId().toString());
        details.put("name", user.getName());
        details.put("email", user.getEmail());
        return ResponseEntity.ok(details);
    }
}
