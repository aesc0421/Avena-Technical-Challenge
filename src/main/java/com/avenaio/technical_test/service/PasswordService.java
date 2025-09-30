package com.avenaio.technical_test.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for password encryption and validation
 */
@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Encrypts a plain text password
     * @param plainPassword The plain text password
     * @return The encrypted password
     */
    public String encryptPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * Validates a plain text password against an encrypted password
     * @param plainPassword The plain text password to validate
     * @param encryptedPassword The encrypted password to compare against
     * @return true if passwords match, false otherwise
     */
    public boolean validatePassword(String plainPassword, String encryptedPassword) {
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }

    /**
     * Checks if a password meets minimum requirements
     * @param password The password to validate
     * @return true if password meets requirements, false otherwise
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUpperCase && hasLowerCase && hasDigit;
    }
}