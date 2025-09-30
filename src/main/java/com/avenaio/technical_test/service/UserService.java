package com.avenaio.technical_test.service;

import com.avenaio.technical_test.model.User;
import com.avenaio.technical_test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for user management including password operations
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    /**
     * Registers a new user with encrypted password
     * @param user The user to register
     * @param plainPassword The plain text password
     * @return The saved user with encrypted password
     * @throws IllegalArgumentException if password is invalid or user already exists
     */
    public User registerUser(User user, String plainPassword) {
        // Validate password
        if (!passwordService.isValidPassword(plainPassword)) {
            throw new IllegalArgumentException("Password must be at least 8 characters with uppercase, lowercase, and digit");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with username already exists");
        }

        // Encrypt password and save user
        user.setPassword(passwordService.encryptPassword(plainPassword));
        return userRepository.save(user);
    }

    /**
     * Authenticates a user with email and password
     * @param email The user's email
     * @param plainPassword The plain text password
     * @return The user if authentication successful, empty if not
     */
    public Optional<User> authenticateUser(String email, String plainPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordService.validatePassword(plainPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }

    /**
     * Changes a user's password
     * @param userId The user's ID
     * @param oldPassword The current password
     * @param newPassword The new password
     * @return true if password changed successfully, false if old password is incorrect
     * @throws IllegalArgumentException if new password is invalid
     */
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        
        // Validate old password
        if (!passwordService.validatePassword(oldPassword, user.getPassword())) {
            return false;
        }

        // Validate new password
        if (!passwordService.isValidPassword(newPassword)) {
            throw new IllegalArgumentException("New password must be at least 8 characters with uppercase, lowercase, and digit");
        }

        // Update password
        user.setPassword(passwordService.encryptPassword(newPassword));
        userRepository.save(user);
        return true;
    }

    /**
     * Finds a user by email
     * @param email The user's email
     * @return The user if found, empty if not
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds a user by username
     * @param username The user's username
     * @return The user if found, empty if not
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by ID
     * @param id The user's ID
     * @return The user if found, empty if not
     */
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
}