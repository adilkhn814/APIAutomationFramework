package com.api.base;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import com.api.core.ServiceLocator;
import com.api.services.interfaces.*;
import com.api.services.business.UserManagementBusinessService;

/**
 * Base Test Class using Service Design Pattern
 * Demonstrates dependency injection and service usage in tests
 */
public class BaseTest {
    
    // Service interfaces - injected via ServiceLocator
    protected IAuthService authService;
    protected IUserProfileService userProfileService;
    protected IConfigurationService configService;
    protected IHttpClientService httpClientService;
    
    // Business services
    protected UserManagementBusinessService userManagementService;
    
    // Service locator for dependency injection
    protected ServiceLocator serviceLocator;
    
    @BeforeClass
    public void setUpServices() {
        // Initialize service locator and inject services
        serviceLocator = ServiceLocator.getInstance();
        
        // Inject core services
        authService = serviceLocator.getService(IAuthService.class);
        userProfileService = serviceLocator.getService(IUserProfileService.class);
        configService = serviceLocator.getService(IConfigurationService.class);
        httpClientService = serviceLocator.getService(IHttpClientService.class);
        
        // Initialize business services
        userManagementService = new UserManagementBusinessService();
        
        System.out.println("Services initialized successfully");
        System.out.println("Base URL: " + configService.getBaseUrl());
        System.out.println("Logging enabled: " + configService.isLoggingEnabled());
    }
    
    @BeforeMethod
    public void setUpTest() {
        // Reset HTTP client before each test to ensure clean state
        httpClientService.reset();
        System.out.println("Test setup completed - HTTP client reset");
    }
    
    @AfterMethod
    public void tearDownTest() {
        // Clean up after each test
        httpClientService.reset();
        System.out.println("Test cleanup completed");
    }
    
    @AfterClass
    public void tearDownServices() {
        // Clean up services if needed
        if (serviceLocator != null) {
            // You can add any cleanup logic here
            System.out.println("Services cleanup completed");
        }
    }
    
    /**
     * Helper method to get a fresh service instance for testing
     * Useful for testing service behavior in isolation
     */
    protected <T> T getService(Class<T> serviceClass) {
        return serviceLocator.getService(serviceClass);
    }
    
    /**
     * Helper method to replace a service with a mock for testing
     * Demonstrates how to use dependency injection for testing
     */
    protected <T> void replaceService(Class<T> serviceInterface, T mockImplementation) {
        serviceLocator.replaceService(serviceInterface, mockImplementation);
        // Re-inject services to get the new implementation
        setUpServices();
    }
    
    /**
     * Helper method to reset all services to their default implementations
     */
    protected void resetServices() {
        serviceLocator.clear();
        setUpServices();
    }
}