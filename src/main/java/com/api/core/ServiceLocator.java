package com.api.core;

import com.api.services.interfaces.*;
import com.api.services.impl.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service Locator for Dependency Injection
 * Manages service instances and provides dependency injection
 */
public class ServiceLocator {
    
    private static ServiceLocator instance;
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();
    
    private ServiceLocator() {
        initializeServices();
    }
    
    public static synchronized ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }
    
    private void initializeServices() {
        // Initialize core services
        IConfigurationService configService = new ConfigurationServiceImpl();
        IHttpClientService httpClientService = new HttpClientServiceImpl(configService);
        IValidationService validationService = new ValidationServiceImpl();
        
        // Register services
        registerService(IConfigurationService.class, configService);
        registerService(IHttpClientService.class, httpClientService);
        registerService(IValidationService.class, validationService);
        registerService(IAuthService.class, new AuthServiceImpl(httpClientService));
        registerService(IUserProfileService.class, new UserProfileServiceImpl(httpClientService));
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        T service = (T) services.get(serviceClass);
        if (service == null) {
            throw new RuntimeException("Service not found: " + serviceClass.getSimpleName());
        }
        return service;
    }
    
    public <T> void registerService(Class<T> serviceInterface, T serviceImplementation) {
        services.put(serviceInterface, serviceImplementation);
    }
    
    public <T> void replaceService(Class<T> serviceInterface, T serviceImplementation) {
        services.put(serviceInterface, serviceImplementation);
    }
    
    public void clear() {
        services.clear();
        initializeServices();
    }
}