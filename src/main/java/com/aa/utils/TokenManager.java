package com.aa.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Token Manager following Singleton pattern
 * Handles authentication token generation, caching, and refresh
 */
public class TokenManager {
    
    private static final Logger logger = LogManager.getLogger(TokenManager.class);
    private static TokenManager instance;
    private static final ReentrantLock lock = new ReentrantLock();
    
    private String authToken;
    private LocalDateTime tokenExpiry;
    private final long TOKEN_BUFFER_MINUTES = 5; // Refresh token 5 minutes before expiry
    
    private TokenManager() {
        // Private constructor for Singleton
    }
    
    /**
     * Get TokenManager instance (Singleton)
     */
    public static TokenManager getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new TokenManager();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }
    
    /**
     * Get authentication token (generates new one if expired)
     */
    public String getAuthToken() {
        if (isTokenExpired()) {
            lock.lock();
            try {
                // Double-check locking pattern
                if (isTokenExpired()) {
                    generateAuthToken();
                }
            } finally {
                lock.unlock();
            }
        }
        return authToken;
    }
    
    /**
     * Check if current token is expired or about to expire
     */
    private boolean isTokenExpired() {
        if (authToken == null || tokenExpiry == null) {
            return true;
        }
        
        LocalDateTime bufferTime = LocalDateTime.now().plus(TOKEN_BUFFER_MINUTES, ChronoUnit.MINUTES);
        return bufferTime.isAfter(tokenExpiry);
    }
    
    /**
     * Generate new authentication token
     */
    private void generateAuthToken() {
        try {
            logger.info("Generating new authentication token...");
            
            String authUrl = ConfigManager.getProperty("auth.url", "https://auth.account-aggregator.com");
            String clientId = VaultManager.getInstance().getSecret("auth.client.id");
            String clientSecret = VaultManager.getInstance().getSecret("auth.client.secret");
            
            if (clientId == null || clientSecret == null) {
                logger.warn("Client credentials not found, using mock token for testing");
                generateMockToken();
                return;
            }
            
            // Prepare token request
            String tokenEndpoint = authUrl + "/oauth/token";
            String requestBody = buildTokenRequestBody(clientId, clientSecret);
            
            Response response = RestAssured.given()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body(requestBody)
                    .post(tokenEndpoint);
            
            if (response.getStatusCode() == 200) {
                String responseBody = response.getBody().asString();
                parseTokenResponse(responseBody);
                logger.info("Authentication token generated successfully");
            } else {
                logger.error("Failed to generate token. Status: {}, Response: {}", 
                           response.getStatusCode(), response.getBody().asString());
                generateMockToken(); // Fallback to mock token
            }
            
        } catch (Exception e) {
            logger.error("Error generating authentication token: {}", e.getMessage(), e);
            generateMockToken(); // Fallback to mock token
        }
    }
    
    /**
     * Build token request body
     */
    private String buildTokenRequestBody(String clientId, String clientSecret) {
        return String.format("grant_type=client_credentials&client_id=%s&client_secret=%s&scope=aa_api",
                           clientId, clientSecret);
    }
    
    /**
     * Parse token response and extract token details
     */
    private void parseTokenResponse(String responseBody) {
        try {
            // Simple JSON parsing for token (could use Gson for complex scenarios)
            // Expected response: {"access_token":"token","token_type":"Bearer","expires_in":3600}
            
            if (responseBody.contains("access_token")) {
                // Extract access token
                String tokenStart = "\"access_token\":\"";
                int startIndex = responseBody.indexOf(tokenStart) + tokenStart.length();
                int endIndex = responseBody.indexOf("\"", startIndex);
                authToken = responseBody.substring(startIndex, endIndex);
                
                // Extract expires_in (default to 1 hour if not found)
                int expiresIn = 3600; // Default 1 hour
                String expiresStart = "\"expires_in\":";
                int expiresStartIndex = responseBody.indexOf(expiresStart);
                if (expiresStartIndex != -1) {
                    expiresStartIndex += expiresStart.length();
                    int expiresEndIndex = responseBody.indexOf(",", expiresStartIndex);
                    if (expiresEndIndex == -1) {
                        expiresEndIndex = responseBody.indexOf("}", expiresStartIndex);
                    }
                    try {
                        expiresIn = Integer.parseInt(responseBody.substring(expiresStartIndex, expiresEndIndex).trim());
                    } catch (NumberFormatException e) {
                        logger.warn("Could not parse expires_in, using default 3600 seconds");
                    }
                }
                
                // Set token expiry
                tokenExpiry = LocalDateTime.now().plus(expiresIn, ChronoUnit.SECONDS);
                logger.debug("Token will expire at: {}", tokenExpiry);
                
            } else {
                throw new RuntimeException("Invalid token response format");
            }
            
        } catch (Exception e) {
            logger.error("Error parsing token response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse token response", e);
        }
    }
    
    /**
     * Generate mock token for testing when real authentication is not available
     */
    private void generateMockToken() {
        logger.info("Generating mock authentication token for testing");
        authToken = "mock_token_" + System.currentTimeMillis();
        tokenExpiry = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
    }
    
    /**
     * Manually set token (useful for testing)
     */
    public void setAuthToken(String token, LocalDateTime expiry) {
        this.authToken = token;
        this.tokenExpiry = expiry;
        logger.debug("Authentication token set manually");
    }
    
    /**
     * Force token refresh
     */
    public void refreshToken() {
        logger.info("Forcing token refresh...");
        authToken = null;
        tokenExpiry = null;
        getAuthToken();
    }
    
    /**
     * Clear cached token
     */
    public void clearToken() {
        authToken = null;
        tokenExpiry = null;
        logger.debug("Authentication token cleared");
    }
    
    /**
     * Get token expiry time
     */
    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }
    
    /**
     * Check if token is valid (not null and not expired)
     */
    public boolean isTokenValid() {
        return authToken != null && !isTokenExpired();
    }
}