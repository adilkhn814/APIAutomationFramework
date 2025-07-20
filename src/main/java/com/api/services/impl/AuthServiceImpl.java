package com.api.services.impl;

import com.api.services.interfaces.IAuthService;
import com.api.services.interfaces.IHttpClientService;
import com.api.models.request.LoginRequest;
import com.api.models.request.ResetPasswordRequest;
import com.api.models.request.SignUpRequest;
import com.api.models.response.LoginResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Authentication Service Implementation
 * Handles all authentication-related operations with token management
 */
public class AuthServiceImpl implements IAuthService {
    
    private static final String AUTH_BASE_PATH = "/api/auth/";
    private final IHttpClientService httpClientService;
    private final Set<String> validTokens = ConcurrentHashMap.newKeySet();
    
    public AuthServiceImpl(IHttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        validateLoginRequest(loginRequest);
        
        Response response = httpClientService.post(AUTH_BASE_PATH + "login", loginRequest);
        
        if (response.getStatusCode() == 200) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            // Store valid token for validation purposes
            if (loginResponse.getToken() != null) {
                validTokens.add(loginResponse.getToken());
            }
            return loginResponse;
        } else {
            throw new RuntimeException("Login failed with status: " + response.getStatusCode() 
                + ", Response: " + response.getBody().asString());
        }
    }
    
    @Override
    public Response signUp(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);
        return httpClientService.post(AUTH_BASE_PATH + "signup", signUpRequest);
    }
    
    @Override
    public Response forgotPassword(String emailAddress) {
        validateEmail(emailAddress);
        
        Map<String, String> payload = new HashMap<>();
        payload.put("email", emailAddress);
        return httpClientService.post(AUTH_BASE_PATH + "forgot-password", payload);
    }
    
    @Override
    public Response resetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        validateResetPasswordRequest(resetPasswordRequest);
        validateToken(token);
        
        // Set auth token for this request
        httpClientService.setAuthToken(token);
        
        try {
            return httpClientService.post(AUTH_BASE_PATH + "reset-password", resetPasswordRequest);
        } finally {
            // Reset the client to remove the token after this operation
            httpClientService.reset();
        }
    }
    
    @Override
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        return validTokens.contains(token);
    }
    
    @Override
    public void invalidateToken(String token) {
        validTokens.remove(token);
    }
    
    private void validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new IllegalArgumentException("Login request cannot be null");
        }
        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
    
    private void validateSignUpRequest(SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            throw new IllegalArgumentException("SignUp request cannot be null");
        }
        // Add more validation as needed
    }
    
    private void validateResetPasswordRequest(ResetPasswordRequest resetPasswordRequest) {
        if (resetPasswordRequest == null) {
            throw new IllegalArgumentException("Reset password request cannot be null");
        }
        // Add more validation as needed
    }
    
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}