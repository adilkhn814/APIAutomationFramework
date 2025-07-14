package com.aa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Vault Manager for credential and secret management
 * Dummy implementation following Singleton pattern
 * In production, this would integrate with actual secret management systems like HashiCorp Vault, AWS Secrets Manager, etc.
 */
public class VaultManager {
    
    private static final Logger logger = LogManager.getLogger(VaultManager.class);
    private static VaultManager instance;
    private static final ReentrantLock lock = new ReentrantLock();
    
    // In-memory secret store (for demonstration purposes only)
    private final Map<String, String> secretStore;
    
    private VaultManager() {
        secretStore = new HashMap<>();
        initializeDefaultSecrets();
    }
    
    /**
     * Get VaultManager instance (Singleton)
     */
    public static VaultManager getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new VaultManager();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }
    
    /**
     * Initialize default secrets for testing
     */
    private void initializeDefaultSecrets() {
        // Authentication credentials
        secretStore.put("auth.client.id", "aa_test_client_id");
        secretStore.put("auth.client.secret", "aa_test_client_secret_12345");
        
        // Database credentials
        secretStore.put("db.username", "aa_test_user");
        secretStore.put("db.password", "aa_test_password");
        secretStore.put("db.url", "jdbc:mysql://localhost:3306/aa_test_db");
        
        // API Keys
        secretStore.put("api.key.external.service", "external_api_key_12345");
        secretStore.put("api.key.fiu", "fiu_api_key_67890");
        secretStore.put("api.key.fip", "fip_api_key_abcde");
        
        // Encryption keys
        secretStore.put("encryption.key.data", "data_encryption_key_xyz123");
        secretStore.put("encryption.key.session", "session_encryption_key_abc456");
        
        // Third-party service credentials
        secretStore.put("wiremock.admin.token", "wiremock_admin_token_test");
        secretStore.put("reporting.service.key", "reporting_service_key_test");
        
        logger.info("Default secrets initialized in VaultManager");
    }
    
    /**
     * Get secret by key
     */
    public String getSecret(String key) {
        if (key == null || key.trim().isEmpty()) {
            logger.warn("Attempted to retrieve secret with null or empty key");
            return null;
        }
        
        // First try to get from environment variables
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null) {
            logger.debug("Secret {} retrieved from environment variable", key);
            return envValue;
        }
        
        // Then try system properties
        String propValue = System.getProperty(key);
        if (propValue != null) {
            logger.debug("Secret {} retrieved from system properties", key);
            return propValue;
        }
        
        // Finally, check the in-memory store
        String value = secretStore.get(key);
        if (value != null) {
            logger.debug("Secret {} retrieved from vault store", key);
            return value;
        }
        
        logger.warn("Secret not found for key: {}", key);
        return null;
    }
    
    /**
     * Store secret (for testing purposes)
     */
    public void storeSecret(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            logger.warn("Attempted to store secret with null or empty key");
            return;
        }
        
        if (value == null) {
            logger.warn("Attempted to store null value for key: {}", key);
            return;
        }
        
        secretStore.put(key, value);
        logger.debug("Secret stored for key: {}", key);
    }
    
    /**
     * Remove secret from store
     */
    public void removeSecret(String key) {
        if (key != null) {
            secretStore.remove(key);
            logger.debug("Secret removed for key: {}", key);
        }
    }
    
    /**
     * Check if secret exists
     */
    public boolean hasSecret(String key) {
        return getSecret(key) != null;
    }
    
    /**
     * Get database connection URL with credentials
     */
    public String getDatabaseConnectionUrl() {
        String baseUrl = getSecret("db.url");
        String username = getSecret("db.username");
        String password = getSecret("db.password");
        
        if (baseUrl == null) {
            return null;
        }
        
        // Construct full connection URL with credentials if available
        if (username != null && password != null) {
            if (baseUrl.contains("?")) {
                return baseUrl + "&user=" + username + "&password=" + password;
            } else {
                return baseUrl + "?user=" + username + "&password=" + password;
            }
        }
        
        return baseUrl;
    }
    
    /**
     * Get API headers with authentication
     */
    public Map<String, String> getApiHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        
        // Add API key if available
        String apiKey = getSecret("api.key.external.service");
        if (apiKey != null) {
            headers.put("X-API-Key", apiKey);
        }
        
        return headers;
    }
    
    /**
     * Rotate secret (dummy implementation)
     */
    public boolean rotateSecret(String key) {
        try {
            String currentSecret = getSecret(key);
            if (currentSecret == null) {
                logger.warn("Cannot rotate non-existent secret: {}", key);
                return false;
            }
            
            // Generate new secret (simple implementation for demo)
            String newSecret = generateNewSecret(key);
            storeSecret(key, newSecret);
            
            logger.info("Secret rotated successfully for key: {}", key);
            return true;
            
        } catch (Exception e) {
            logger.error("Failed to rotate secret for key {}: {}", key, e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate new secret (dummy implementation)
     */
    private String generateNewSecret(String key) {
        // Simple secret generation for demo purposes
        long timestamp = System.currentTimeMillis();
        return key.replace(".", "_") + "_rotated_" + timestamp;
    }
    
    /**
     * Clear all secrets (for testing cleanup)
     */
    public void clearAllSecrets() {
        secretStore.clear();
        logger.info("All secrets cleared from vault");
    }
    
    /**
     * Get all secret keys (for debugging - without values)
     */
    public String[] getAllSecretKeys() {
        return secretStore.keySet().toArray(new String[0]);
    }
    
    /**
     * Health check for vault connectivity (dummy implementation)
     */
    public boolean isVaultHealthy() {
        try {
            // Simulate vault health check
            logger.debug("Performing vault health check...");
            return !secretStore.isEmpty();
        } catch (Exception e) {
            logger.error("Vault health check failed: {}", e.getMessage());
            return false;
        }
    }
}