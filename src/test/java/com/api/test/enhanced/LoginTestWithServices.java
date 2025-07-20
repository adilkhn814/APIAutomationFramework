package com.api.test.enhanced;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import com.api.base.BaseTest;
import com.api.models.request.LoginRequest;
import com.api.models.response.LoginResponse;
import com.api.services.business.UserManagementBusinessService;
import com.utility.TestUtility.Constants;

/**
 * Enhanced Login Test using Service Design Pattern
 * Demonstrates how to use the new service architecture
 */
@Listeners(com.api.listeners.TestListener.class)
public class LoginTestWithServices extends BaseTest {
    
    @Test(description = "Verify login using service design pattern")
    public void testLoginWithServicePattern() {
        // Using injected services from BaseTest
        String username = configService.getUsername();
        String password = configService.getPassword();
        
        LoginRequest loginRequest = new LoginRequest.Builder()
            .username(username)
            .password(password)
            .build();
        
        // Use the authentication service
        LoginResponse loginResponse = authService.login(loginRequest);
        
        // Assertions
        Assert.assertNotNull(loginResponse, "Login response should not be null");
        Assert.assertEquals(loginResponse.getUsername(), username, "Username should match");
        Assert.assertNotNull(loginResponse.getToken(), "Token should not be null");
        Assert.assertFalse(loginResponse.getToken().isEmpty(), "Token should not be empty");
        Assert.assertEquals(loginResponse.getType(), Constants.TOKEN_TYPE_BEARER, "Token type should be Bearer");
        Assert.assertTrue(loginResponse.getId() > 0, "User ID should be positive");
        Assert.assertTrue(loginResponse.getRoles().contains(Constants.ROLE_USER), "Should have USER role");
        
        // Validate token using auth service
        boolean isValid = authService.validateToken(loginResponse.getToken());
        Assert.assertTrue(isValid, "Token should be valid");
        
        System.out.println("Login test completed successfully using service design pattern");
    }
    
    @Test(description = "Test complete login and profile workflow using business service")
    public void testLoginAndProfileWorkflow() {
        // Using business service for complex workflow
        UserManagementBusinessService.UserLoginResult result = 
            userManagementService.loginAndGetProfile(null, null); // Uses config defaults
        
        // Verify login response
        LoginResponse loginResponse = result.getLoginResponse();
        Assert.assertNotNull(loginResponse, "Login response should not be null");
        Assert.assertNotNull(loginResponse.getToken(), "Token should not be null");
        
        // Verify profile response  
        Assert.assertNotNull(result.getProfileResponse(), "Profile response should not be null");
        Assert.assertEquals(result.getProfileResponse().getUsername(), 
                          loginResponse.getUsername(), "Profile username should match login username");
        
        System.out.println("Complete workflow test completed successfully");
    }
    
    @Test(description = "Test login with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        LoginRequest invalidLoginRequest = new LoginRequest.Builder()
            .username("invalid_user")
            .password("invalid_password")
            .build();
        
        // This should throw an exception due to failed login
        try {
            authService.login(invalidLoginRequest);
            Assert.fail("Login should have failed with invalid credentials");
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("Login failed"), 
                            "Exception should indicate login failure");
            System.out.println("Invalid credentials test passed: " + e.getMessage());
        }
    }
    
    @Test(description = "Test configuration service")
    public void testConfigurationService() {
        // Test configuration service functionality
        Assert.assertNotNull(configService.getBaseUrl(), "Base URL should not be null");
        Assert.assertNotNull(configService.getUsername(), "Username should not be null");
        Assert.assertNotNull(configService.getPassword(), "Password should not be null");
        Assert.assertTrue(configService.getRequestTimeout() > 0, "Timeout should be positive");
        
        // Test dynamic property setting
        configService.setProperty("test.property", "test.value");
        Assert.assertEquals(configService.getProperty("test.property"), "test.value", 
                          "Dynamic property should be set correctly");
        
        System.out.println("Configuration service test completed successfully");
    }
}