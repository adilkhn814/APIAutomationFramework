package com.api.services.interfaces;

import com.api.models.request.LoginRequest;
import com.api.models.request.SignUpRequest;
import com.api.models.request.UpdateProfileRequest;

/**
 * Validation Service Interface
 * Centralized validation logic following service design pattern
 */
public interface IValidationService {
    boolean validateLoginRequest(LoginRequest loginRequest);
    boolean validateSignUpRequest(SignUpRequest signUpRequest);
    boolean validateUpdateProfileRequest(UpdateProfileRequest updateRequest);
    boolean validateEmail(String email);
    boolean validatePhoneNumber(String phoneNumber);
    boolean validatePassword(String password);
    boolean validateUsername(String username);
    
    // Validation with detailed error messages
    ValidationResult validateLoginRequestWithDetails(LoginRequest loginRequest);
    ValidationResult validateSignUpRequestWithDetails(SignUpRequest signUpRequest);
    ValidationResult validateUpdateProfileRequestWithDetails(UpdateProfileRequest updateRequest);
    
    // Validation result class
    class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult failure(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }
    }
}