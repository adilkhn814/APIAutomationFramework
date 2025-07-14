package com.aa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager to handle application properties
 * Follows Singleton pattern for consistent configuration access
 */
public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static Properties properties = new Properties();
    private static boolean isLoaded = false;
    
    private static final String CONFIG_FILE = "src/test/resources/config/config.properties";
    
    /**
     * Load configuration from properties file
     */
    public static void loadConfig() {
        if (!isLoaded) {
            try {
                // Try to load from file system first
                try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                    properties.load(fis);
                    logger.info("Configuration loaded from: {}", CONFIG_FILE);
                } catch (IOException e) {
                    // Fallback to classpath
                    try (InputStream is = ConfigManager.class.getClassLoader()
                            .getResourceAsStream("config/config.properties")) {
                        if (is != null) {
                            properties.load(is);
                            logger.info("Configuration loaded from classpath");
                        } else {
                            logger.warn("Configuration file not found, using default values");
                            setDefaultProperties();
                        }
                    }
                }
                
                // Override with system properties if available
                overrideWithSystemProperties();
                
                isLoaded = true;
                logger.info("Configuration loading completed");
                
            } catch (IOException e) {
                logger.error("Error loading configuration: {}", e.getMessage());
                setDefaultProperties();
            }
        }
    }
    
    /**
     * Get property value by key
     */
    public static String getProperty(String key) {
        if (!isLoaded) {
            loadConfig();
        }
        return properties.getProperty(key);
    }
    
    /**
     * Get property value by key with default value
     */
    public static String getProperty(String key, String defaultValue) {
        if (!isLoaded) {
            loadConfig();
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get property as integer
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get property as boolean
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    /**
     * Set default properties if configuration file is not found
     */
    private static void setDefaultProperties() {
        properties.setProperty("base.url", "https://api.account-aggregator.com");
        properties.setProperty("auth.url", "https://auth.account-aggregator.com");
        properties.setProperty("timeout.connection", "10000");
        properties.setProperty("timeout.read", "30000");
        properties.setProperty("retry.count", "3");
        properties.setProperty("wiremock.port", "8089");
        properties.setProperty("db.enabled", "false");
        properties.setProperty("extent.report.path", "reports/extent-report.html");
        
        logger.info("Default configuration properties set");
    }
    
    /**
     * Override properties with system properties (for CI/CD environments)
     */
    private static void overrideWithSystemProperties() {
        // Common system property keys that can override config
        String[] systemKeys = {
            "base.url", "auth.url", "environment", "db.url", "db.username", 
            "db.password", "auth.client.id", "auth.client.secret"
        };
        
        for (String key : systemKeys) {
            String systemValue = System.getProperty(key);
            if (systemValue != null) {
                properties.setProperty(key, systemValue);
                logger.debug("Property {} overridden by system property: {}", key, systemValue);
            }
        }
        
        // Also check environment variables
        String env = System.getenv("TEST_ENVIRONMENT");
        if (env != null) {
            properties.setProperty("environment", env);
            logger.info("Environment set from TEST_ENVIRONMENT: {}", env);
        }
    }
    
    /**
     * Get all properties (for debugging)
     */
    public static Properties getAllProperties() {
        if (!isLoaded) {
            loadConfig();
        }
        return new Properties(properties);
    }
    
    /**
     * Reload configuration
     */
    public static void reloadConfig() {
        isLoaded = false;
        properties.clear();
        loadConfig();
    }
}