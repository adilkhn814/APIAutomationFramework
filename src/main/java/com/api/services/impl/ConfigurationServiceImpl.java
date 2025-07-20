package com.api.services.impl;

import com.api.services.interfaces.IConfigurationService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Configuration Service Implementation
 * Handles all configuration management with caching and dynamic updates
 */
public class ConfigurationServiceImpl implements IConfigurationService {
    
    private final Properties properties = new Properties();
    private final Map<String, String> dynamicProperties = new ConcurrentHashMap<>();
    
    public ConfigurationServiceImpl() {
        loadProperties();
    }
    
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }
    
    @Override
    public String getProperty(String key) {
        // Check dynamic properties first (for runtime overrides)
        String value = dynamicProperties.get(key);
        if (value != null) {
            return value;
        }
        
        // Check system properties
        value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        
        // Check loaded properties file
        return properties.getProperty(key);
    }
    
    @Override
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    @Override
    public String getBaseUrl() {
        return getProperty("base.url", "http://64.227.160.186:8080");
    }
    
    @Override
    public String getUsername() {
        return getProperty("username", "testuser");
    }
    
    @Override
    public String getPassword() {
        return getProperty("password", "testpass");
    }
    
    @Override
    public int getRequestTimeout() {
        String timeout = getProperty("request.timeout", "30000");
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            return 30000; // Default 30 seconds
        }
    }
    
    @Override
    public boolean isLoggingEnabled() {
        String logging = getProperty("logging.enabled", "true");
        return Boolean.parseBoolean(logging);
    }
    
    @Override
    public void setProperty(String key, String value) {
        dynamicProperties.put(key, value);
    }
}