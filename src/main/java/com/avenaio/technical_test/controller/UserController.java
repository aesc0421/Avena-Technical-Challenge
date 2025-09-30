package com.avenaio.technical_test.controller;

import com.avenaio.technical_test.model.HabitDate;
import com.avenaio.technical_test.model.User;
import com.avenaio.technical_test.service.HabitDateService;
import com.avenaio.technical_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Controller for user registration and authentication
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HabitDateService habitDateService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        try {
            User user = new User();
            user.setUsername(request.get("username"));
            user.setFirstName(request.get("firstName"));
            user.setLastName(request.get("lastName"));
            user.setEmail(request.get("email"));
            
            String password = request.get("password");
            
            User savedUser = userService.registerUser(user, password);
            
            // Don't return password in response
            savedUser.setPassword(null);
            
            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "user", savedUser
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Authenticate a user
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        
        Optional<User> user = userService.authenticateUser(email, password);
        
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setPassword(null); // Don't return password
            
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "user", foundUser
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
    }

    /**
     * Change user password
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");
            
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User ID is required"));
            }
            
            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid old password"));
            }
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/create-day")
    public ResponseEntity<?> createDay(@RequestBody HabitDate date) {
        try {
            habitDateService.updateHabitDate(date);

            return ResponseEntity.ok(Map.of("message", "Day created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}