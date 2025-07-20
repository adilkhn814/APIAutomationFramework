package com.api.services.impl;

import com.api.services.interfaces.IValidationService;
import com.api.models.request.LoginRequest;
import com.api.models.request.SignUpRequest;
import com.api.models.request.UpdateProfileRequest;

import java.util.regex.Pattern;

/**
 * Validation Service Implementation
 * Centralized validation logic with detailed error reporting
 */
public class ValidationServiceImpl implements IValidationService {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 50;
    
    @Override
    public boolean validateLoginRequest(LoginRequest loginRequest) {
        return validateLoginRequestWithDetails(loginRequest).isValid();
    }
    
    @Override
    public boolean validateSignUpRequest(SignUpRequest signUpRequest) {
        return validateSignUpRequestWithDetails(signUpRequest).isValid();
    }
    
    @Override
    public boolean validateUpdateProfileRequest(UpdateProfileRequest updateRequest) {
        return validateUpdateProfileRequestWithDetails(updateRequest).isValid();
    }
    
    @Override
    public boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    @Override
    public boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
    
    @Override
    public boolean validatePassword(String password) {
        return password != null && 
               password.length() >= MIN_PASSWORD_LENGTH && 
               password.length() <= MAX_PASSWORD_LENGTH;
    }
    
    @Override
    public boolean validateUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    @Override
    public ValidationResult validateLoginRequestWithDetails(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return ValidationResult.failure("Login request cannot be null");
        }
        
        if (!validateUsername(loginRequest.getUsername())) {
            return ValidationResult.failure("Invalid username format. Must be 3-20 characters, alphanumeric and underscore only");
        }
        
        if (!validatePassword(loginRequest.getPassword())) {
            return ValidationResult.failure("Invalid password. Must be " + MIN_PASSWORD_LENGTH + "-" + MAX_PASSWORD_LENGTH + " characters");
        }
        
        return ValidationResult.success();
    }
    
    @Override
    public ValidationResult validateSignUpRequestWithDetails(SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            return ValidationResult.failure("SignUp request cannot be null");
        }
        
        if (!validateUsername(signUpRequest.getUsername())) {
            return ValidationResult.failure("Invalid username format. Must be 3-20 characters, alphanumeric and underscore only");
        }
        
        if (!validatePassword(signUpRequest.getPassword())) {
            return ValidationResult.failure("Invalid password. Must be " + MIN_PASSWORD_LENGTH + "-" + MAX_PASSWORD_LENGTH + " characters");
        }
        
        if (!validateEmail(signUpRequest.getEmail())) {
            return ValidationResult.failure("Invalid email format");
        }
        
        // Add more validations for other fields as needed
        
        return ValidationResult.success();
    }
    
    @Override
    public ValidationResult validateUpdateProfileRequestWithDetails(UpdateProfileRequest updateRequest) {
        if (updateRequest == null) {
            return ValidationResult.failure("Update profile request cannot be null");
        }
        
        // Validate email if provided
        if (updateRequest.getEmail() != null && !validateEmail(updateRequest.getEmail())) {
            return ValidationResult.failure("Invalid email format");
        }
        
        // Validate phone number if provided
        if (updateRequest.getPhoneNumber() != null && !validatePhoneNumber(updateRequest.getPhoneNumber())) {
            return ValidationResult.failure("Invalid phone number format");
        }
        
        // Add more field validations as needed
        
        return ValidationResult.success();
    }
}