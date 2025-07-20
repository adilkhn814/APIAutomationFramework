package com.api.services.business;

import com.api.core.ServiceLocator;
import com.api.services.interfaces.IAuthService;
import com.api.services.interfaces.IConfigurationService;
import com.api.services.interfaces.IUserProfileService;
import com.api.models.request.LoginRequest;
import com.api.models.request.SignUpRequest;
import com.api.models.request.UpdateProfileRequest;
import com.api.models.response.LoginResponse;
import com.api.models.response.GetProfileResponse;
import io.restassured.response.Response;

/**
 * User Management Business Service
 * Composes multiple services to provide high-level business operations
 * This demonstrates the service design pattern by orchestrating multiple services
 */
public class UserManagementBusinessService {
    
    private final IAuthService authService;
    private final IUserProfileService userProfileService;
    private final IConfigurationService configService;
    
    public UserManagementBusinessService() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        this.authService = serviceLocator.getService(IAuthService.class);
        this.userProfileService = serviceLocator.getService(IUserProfileService.class);
        this.configService = serviceLocator.getService(IConfigurationService.class);
    }
    
    /**
     * Complete user registration workflow
     * Demonstrates service composition - sign up user and get their profile
     */
    public UserRegistrationResult registerAndSetupUser(SignUpRequest signUpRequest) {
        try {
            // Step 1: Sign up the user
            Response signUpResponse = authService.signUp(signUpRequest);
            
            if (signUpResponse.getStatusCode() != 201) {
                throw new RuntimeException("User registration failed");
            }
            
            // Step 2: Login with the new credentials
            LoginRequest loginRequest = new LoginRequest.Builder()
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .build();
                
            LoginResponse loginResponse = authService.login(loginRequest);
            
            // Step 3: Get user profile
            GetProfileResponse profileResponse = userProfileService.getProfile(loginResponse.getToken());
            
            return new UserRegistrationResult(signUpResponse, loginResponse, profileResponse);
            
        } catch (Exception e) {
            throw new RuntimeException("User registration workflow failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Login and get user profile in one operation
     * Demonstrates service composition for common workflow
     */
    public UserLoginResult loginAndGetProfile(String username, String password) {
        try {
            // Use config defaults if not provided
            if (username == null) username = configService.getUsername();
            if (password == null) password = configService.getPassword();
            
            LoginRequest loginRequest = new LoginRequest.Builder()
                .username(username)
                .password(password)
                .build();
                
            LoginResponse loginResponse = authService.login(loginRequest);
            GetProfileResponse profileResponse = userProfileService.getProfile(loginResponse.getToken());
            
            return new UserLoginResult(loginResponse, profileResponse);
            
        } catch (Exception e) {
            throw new RuntimeException("Login and profile retrieval failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update user profile with validation and retry logic
     * Demonstrates business logic with error handling
     */
    public Response updateUserProfile(UpdateProfileRequest updateRequest, String token, boolean usePartialUpdate) {
        try {
            // Validate the profile data first
            userProfileService.validateProfileData(updateRequest);
            
            // Perform the update based on the strategy
            Response response;
            if (usePartialUpdate) {
                response = userProfileService.partialUpdateProfile(updateRequest, token);
            } else {
                response = userProfileService.updateProfile(updateRequest, token);
            }
            
            // If update was successful, verify by getting the updated profile
            if (response.getStatusCode() == 200) {
                GetProfileResponse updatedProfile = userProfileService.getProfile(token);
                System.out.println("Profile updated successfully. New profile: " + updatedProfile.toString());
            }
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Profile update workflow failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Logout user and invalidate token
     * Demonstrates cleanup operations
     */
    public void logoutUser(String token) {
        try {
            authService.invalidateToken(token);
            System.out.println("User logged out successfully");
        } catch (Exception e) {
            System.err.println("Logout failed: " + e.getMessage());
        }
    }
    
    // Result classes for complex operations
    public static class UserRegistrationResult {
        private final Response signUpResponse;
        private final LoginResponse loginResponse;
        private final GetProfileResponse profileResponse;
        
        public UserRegistrationResult(Response signUpResponse, LoginResponse loginResponse, GetProfileResponse profileResponse) {
            this.signUpResponse = signUpResponse;
            this.loginResponse = loginResponse;
            this.profileResponse = profileResponse;
        }
        
        public Response getSignUpResponse() { return signUpResponse; }
        public LoginResponse getLoginResponse() { return loginResponse; }
        public GetProfileResponse getProfileResponse() { return profileResponse; }
    }
    
    public static class UserLoginResult {
        private final LoginResponse loginResponse;
        private final GetProfileResponse profileResponse;
        
        public UserLoginResult(LoginResponse loginResponse, GetProfileResponse profileResponse) {
            this.loginResponse = loginResponse;
            this.profileResponse = profileResponse;
        }
        
        public LoginResponse getLoginResponse() { return loginResponse; }
        public GetProfileResponse getProfileResponse() { return profileResponse; }
    }
}