package com.api.services.interfaces;

/**
 * Configuration Service Interface
 * Provides abstraction for configuration management
 */
public interface IConfigurationService {
    String getProperty(String key);
    String getProperty(String key, String defaultValue);
    String getBaseUrl();
    String getUsername();
    String getPassword();
    int getRequestTimeout();
    boolean isLoggingEnabled();
    void setProperty(String key, String value);
}