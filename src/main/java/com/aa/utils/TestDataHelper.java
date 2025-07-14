package com.aa.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class for managing external test data
 * Provides easy access to test data from JSON files
 */
public class TestDataHelper {
    
    private static final Logger logger = LogManager.getLogger(TestDataHelper.class);
    private static final Gson gson = new Gson();
    private static final Map<String, JsonObject> dataCache = new ConcurrentHashMap<>();
    
    /**
     * Load test data from JSON file
     */
    public static JsonObject loadTestData(String fileName) {
        try {
            if (dataCache.containsKey(fileName)) {
                return dataCache.get(fileName);
            }
            
            String resourcePath = "/testdata/" + fileName;
            InputStream inputStream = TestDataHelper.class.getResourceAsStream(resourcePath);
            
            if (inputStream == null) {
                logger.error("Test data file not found: {}", resourcePath);
                throw new RuntimeException("Test data file not found: " + resourcePath);
            }
            
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            
            dataCache.put(fileName, data);
            logger.info("Loaded test data from: {}", resourcePath);
            return data;
            
        } catch (Exception e) {
            logger.error("Failed to load test data from: {}", fileName, e);
            throw new RuntimeException("Failed to load test data", e);
        }
    }
    
    /**
     * Get consent test data
     */
    public static JsonObject getConsentTestData() {
        JsonObject data = loadTestData("consent-test-data.json");
        return data.getAsJsonObject("consentTestData");
    }
    
    /**
     * Get basic consent data
     */
    public static JsonObject getBasicConsentData() {
        return getConsentTestData().getAsJsonObject("basicConsent");
    }
    
    /**
     * Get comprehensive consent data
     */
    public static JsonObject getComprehensiveConsentData() {
        return getConsentTestData().getAsJsonObject("comprehensiveConsent");
    }
    
    /**
     * Get invalid scenarios data
     */
    public static JsonObject getInvalidScenariosData() {
        return getConsentTestData().getAsJsonObject("invalidScenarios");
    }
    
    /**
     * Get performance thresholds
     */
    public static JsonObject getPerformanceThresholds() {
        return getConsentTestData().getAsJsonObject("performanceThresholds");
    }
    
    /**
     * Get customer data by ID
     */
    public static JsonObject getCustomerById(String customerId) {
        JsonObject consentData = getConsentTestData();
        var customers = consentData.getAsJsonArray("customers");
        
        for (int i = 0; i < customers.size(); i++) {
            JsonObject customer = customers.get(i).getAsJsonObject();
            if (customerId.equals(customer.get("id").getAsString())) {
                return customer;
            }
        }
        
        logger.warn("Customer not found: {}", customerId);
        return null;
    }
    
    /**
     * Get FIU data by ID
     */
    public static JsonObject getFiuById(String fiuId) {
        JsonObject consentData = getConsentTestData();
        var fius = consentData.getAsJsonArray("fius");
        
        for (int i = 0; i < fius.size(); i++) {
            JsonObject fiu = fius.get(i).getAsJsonObject();
            if (fiuId.equals(fiu.get("id").getAsString())) {
                return fiu;
            }
        }
        
        logger.warn("FIU not found: {}", fiuId);
        return null;
    }
    
    /**
     * Get FIP data by ID
     */
    public static JsonObject getFipById(String fipId) {
        JsonObject consentData = getConsentTestData();
        var fips = consentData.getAsJsonArray("fips");
        
        for (int i = 0; i < fips.size(); i++) {
            JsonObject fip = fips.get(i).getAsJsonObject();
            if (fipId.equals(fip.get("id").getAsString())) {
                return fip;
            }
        }
        
        logger.warn("FIP not found: {}", fipId);
        return null;
    }
    
    /**
     * Get all valid consent statuses
     */
    public static List<String> getValidStatuses() {
        JsonObject consentData = getConsentTestData();
        var statusArray = consentData.getAsJsonArray("validStatuses");
        return gson.fromJson(statusArray, List.class);
    }
    
    /**
     * Get available consent types
     */
    public static List<String> getAvailableConsentTypes() {
        JsonObject consentData = getConsentTestData();
        var consentTypes = consentData.getAsJsonObject("consentTypes");
        var available = consentTypes.getAsJsonArray("available");
        return gson.fromJson(available, List.class);
    }
    
    /**
     * Get available FI types
     */
    public static List<String> getAvailableFiTypes() {
        JsonObject consentData = getConsentTestData();
        var fiTypes = consentData.getAsJsonObject("fiTypes");
        var available = fiTypes.getAsJsonArray("available");
        return gson.fromJson(available, List.class);
    }
    
    /**
     * Get purpose by code
     */
    public static JsonObject getPurposeByCode(String purposeCode) {
        JsonObject consentData = getConsentTestData();
        var purposes = consentData.getAsJsonArray("purposes");
        
        for (int i = 0; i < purposes.size(); i++) {
            JsonObject purpose = purposes.get(i).getAsJsonObject();
            if (purposeCode.equals(purpose.get("code").getAsString())) {
                return purpose;
            }
        }
        
        logger.warn("Purpose not found for code: {}", purposeCode);
        return null;
    }
    
    /**
     * Generate unique test identifier
     */
    public static String generateTestId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + 
               Thread.currentThread().getId();
    }
    
    /**
     * Generate unique customer ID for testing
     */
    public static String generateTestCustomerId() {
        return generateTestId("test_customer") + "@example.com";
    }
    
    /**
     * Generate unique transaction ID
     */
    public static String generateTransactionId() {
        return "TXN_" + generateTestId("test");
    }
    
    /**
     * Generate unique consent handle
     */
    public static String generateConsentHandle() {
        return "CONSENT_" + generateTestId("test");
    }
    
    /**
     * Generate unique session ID
     */
    public static String generateSessionId() {
        return "SESSION_" + generateTestId("test");
    }
    
    /**
     * Get test data as string value
     */
    public static String getStringValue(JsonObject object, String key, String defaultValue) {
        try {
            if (object != null && object.has(key)) {
                return object.get(key).getAsString();
            }
            return defaultValue;
        } catch (Exception e) {
            logger.warn("Error getting string value for key: {}", key, e);
            return defaultValue;
        }
    }
    
    /**
     * Get test data as integer value
     */
    public static int getIntValue(JsonObject object, String key, int defaultValue) {
        try {
            if (object != null && object.has(key)) {
                return object.get(key).getAsInt();
            }
            return defaultValue;
        } catch (Exception e) {
            logger.warn("Error getting int value for key: {}", key, e);
            return defaultValue;
        }
    }
    
    /**
     * Get test data as boolean value
     */
    public static boolean getBooleanValue(JsonObject object, String key, boolean defaultValue) {
        try {
            if (object != null && object.has(key)) {
                return object.get(key).getAsBoolean();
            }
            return defaultValue;
        } catch (Exception e) {
            logger.warn("Error getting boolean value for key: {}", key, e);
            return defaultValue;
        }
    }
    
    /**
     * Clear test data cache
     */
    public static void clearCache() {
        dataCache.clear();
        logger.info("Test data cache cleared");
    }
    
    /**
     * Validate required test data fields
     */
    public static boolean validateTestData(JsonObject data, String... requiredFields) {
        if (data == null) {
            logger.error("Test data is null");
            return false;
        }
        
        for (String field : requiredFields) {
            if (!data.has(field)) {
                logger.error("Required field missing: {}", field);
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get formatted test data summary
     */
    public static String getTestDataSummary() {
        try {
            JsonObject consentData = getConsentTestData();
            StringBuilder summary = new StringBuilder();
            summary.append("Test Data Summary:\n");
            summary.append("- Customers: ").append(consentData.getAsJsonArray("customers").size()).append("\n");
            summary.append("- FIUs: ").append(consentData.getAsJsonArray("fius").size()).append("\n");
            summary.append("- FIPs: ").append(consentData.getAsJsonArray("fips").size()).append("\n");
            summary.append("- Valid Statuses: ").append(consentData.getAsJsonArray("validStatuses").size()).append("\n");
            summary.append("- Consent Types: ").append(consentData.getAsJsonObject("consentTypes").getAsJsonArray("available").size()).append("\n");
            summary.append("- FI Types: ").append(consentData.getAsJsonObject("fiTypes").getAsJsonArray("available").size()).append("\n");
            summary.append("- Purposes: ").append(consentData.getAsJsonArray("purposes").size()).append("\n");
            return summary.toString();
        } catch (Exception e) {
            logger.error("Error generating test data summary", e);
            return "Error generating test data summary: " + e.getMessage();
        }
    }
}