package com.api.services.impl;

import com.api.services.interfaces.IUserProfileService;
import com.api.services.interfaces.IHttpClientService;
import com.api.models.request.UpdateProfileRequest;
import com.api.models.response.GetProfileResponse;
import io.restassured.response.Response;

/**
 * User Profile Service Implementation
 * Handles user profile management with validation and error handling
 */
public class UserProfileServiceImpl implements IUserProfileService {
    
    private static final String USERS_BASE_PATH = "/api/users/";
    private final IHttpClientService httpClientService;
    
    public UserProfileServiceImpl(IHttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }
    
    @Override
    public GetProfileResponse getProfile(String token) {
        validateToken(token);
        
        // Set auth token for this request
        httpClientService.setAuthToken(token);
        
        try {
            Response response = httpClientService.get(USERS_BASE_PATH + "profile");
            
            if (response.getStatusCode() == 200) {
                return response.as(GetProfileResponse.class);
            } else {
                throw new RuntimeException("Get profile failed with status: " + response.getStatusCode() 
                    + ", Response: " + response.getBody().asString());
            }
        } finally {
            // Reset the client to remove the token after this operation
            httpClientService.reset();
        }
    }
    
    @Override
    public Response updateProfile(UpdateProfileRequest updateRequest, String token) {
        validateToken(token);
        validateProfileData(updateRequest);
        
        // Set auth token for this request
        httpClientService.setAuthToken(token);
        
        try {
            return httpClientService.put(USERS_BASE_PATH + "profile", updateRequest);
        } finally {
            // Reset the client to remove the token after this operation
            httpClientService.reset();
        }
    }
    
    @Override
    public Response partialUpdateProfile(UpdateProfileRequest updateRequest, String token) {
        validateToken(token);
        validateProfileData(updateRequest);
        
        // Set auth token for this request
        httpClientService.setAuthToken(token);
        
        try {
            return httpClientService.patch(USERS_BASE_PATH + "profile", updateRequest);
        } finally {
            // Reset the client to remove the token after this operation
            httpClientService.reset();
        }
    }
    
    @Override
    public boolean validateProfileData(UpdateProfileRequest updateRequest) {
        if (updateRequest == null) {
            throw new IllegalArgumentException("Update profile request cannot be null");
        }
        
        // Add specific validation rules
        if (updateRequest.getEmail() != null && !isValidEmail(updateRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (updateRequest.getPhoneNumber() != null && !isValidPhoneNumber(updateRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        
        return true;
    }
    
    private void validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Authentication token cannot be null or empty");
        }
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\+?[1-9]\\d{1,14}$");
    }
}