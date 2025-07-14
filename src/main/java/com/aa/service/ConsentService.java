package com.aa.service;

import com.aa.pojo.ConsentRequest;
import com.aa.pojo.ConsentResponse;
import com.aa.utils.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Service class for Consent API operations
 * Handles all consent-related API calls following Factory/Service pattern
 */
public class ConsentService {
    
    private static final Logger logger = LogManager.getLogger(ConsentService.class);
    private final Gson gson;
    private static final String CONSENT_ENDPOINT = "/Consent";
    
    public ConsentService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();
    }
    
    /**
     * Create a new consent request
     */
    public Response createConsent(ConsentRequest consentRequest) {
        logger.info("Creating consent request for transaction ID: {}", consentRequest.getTransactionId());
        
        try {
            String requestBody = gson.toJson(consentRequest);
            logger.debug("Consent request payload: {}", requestBody);
            
            RequestSpecification requestSpec = RestAssured.given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + TokenManager.getInstance().getAuthToken())
                    .header("Accept", "application/json")
                    .body(requestBody);
            
            Response response = requestSpec.post(CONSENT_ENDPOINT);
            
            logger.info("Consent API response - Status: {}, Time: {} ms", 
                       response.getStatusCode(), response.getTime());
            logger.debug("Consent API response body: {}", response.getBody().asString());
            
            return response;
            
        } catch (Exception e) {
            logger.error("Error creating consent request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create consent request", e);
        }
    }
    
    /**
     * Create consent with minimal required data (for quick testing)
     */
    public Response createBasicConsent(String customerId, String fiuId, String fipId) {
        ConsentRequest consentRequest = buildBasicConsentRequest(customerId, fiuId, fipId);
        return createConsent(consentRequest);
    }
    
    /**
     * Parse consent response to POJO
     */
    public ConsentResponse parseConsentResponse(Response response) {
        try {
            String responseBody = response.getBody().asString();
            ConsentResponse consentResponse = gson.fromJson(responseBody, ConsentResponse.class);
            logger.debug("Parsed consent response: {}", consentResponse);
            return consentResponse;
        } catch (Exception e) {
            logger.error("Error parsing consent response: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse consent response", e);
        }
    }
    
    /**
     * Build a basic consent request with default values
     */
    public ConsentRequest buildBasicConsentRequest(String customerId, String fiuId, String fipId) {
        String timestamp = generateTimestamp();
        String transactionId = generateTransactionId();
        
        // Create consent detail
        ConsentRequest.ConsentDetail consentDetail = new ConsentRequest.ConsentDetail();
        consentDetail.setConsentStart(timestamp);
        consentDetail.setConsentExpiry(generateExpiryTimestamp(30)); // 30 days from now
        consentDetail.setConsentMode("STORE");
        consentDetail.setFetchType("PERIODIC");
        consentDetail.setConsentTypes(Arrays.asList("PROFILE", "SUMMARY", "TRANSACTIONS"));
        consentDetail.setFiTypes(Arrays.asList("DEPOSIT"));
        
        // Set data consumer (FIU)
        ConsentRequest.DataConsumer dataConsumer = new ConsentRequest.DataConsumer(fiuId, "FIU");
        consentDetail.setDataConsumer(dataConsumer);
        
        // Set data provider (FIP)
        ConsentRequest.DataProvider dataProvider = new ConsentRequest.DataProvider(fipId, "FIP");
        consentDetail.setDataProvider(dataProvider);
        
        // Set customer
        ConsentRequest.Identifier identifier = new ConsentRequest.Identifier("MOBILE", "+91-9876543210");
        ConsentRequest.Customer customer = new ConsentRequest.Customer(customerId, Arrays.asList(identifier));
        consentDetail.setCustomer(customer);
        
        // Set purpose
        ConsentRequest.Category category = new ConsentRequest.Category("PERSONAL");
        ConsentRequest.Purpose purpose = new ConsentRequest.Purpose(
                "101", 
                "https://api.rebit.org.in/aa/purpose/101.xml",
                "Wealth management service",
                category
        );
        consentDetail.setPurpose(purpose);
        
        // Set FI data range (last 12 months)
        String fromDate = generatePastTimestamp(365); // 1 year ago
        String toDate = generatePastTimestamp(0);     // today
        ConsentRequest.FIDataRange fiDataRange = new ConsentRequest.FIDataRange(fromDate, toDate);
        consentDetail.setFiDataRange(fiDataRange);
        
        // Set data life (6 months)
        ConsentRequest.DataLife dataLife = new ConsentRequest.DataLife("MONTH", 6);
        consentDetail.setDataLife(dataLife);
        
        // Set frequency (once per day)
        ConsentRequest.Frequency frequency = new ConsentRequest.Frequency("DAY", 1);
        consentDetail.setFrequency(frequency);
        
        // Create main request
        ConsentRequest consentRequest = new ConsentRequest("1.0", timestamp, transactionId, consentDetail);
        
        logger.debug("Built basic consent request: {}", consentRequest);
        return consentRequest;
    }
    
    /**
     * Build comprehensive consent request with all options
     */
    public ConsentRequest buildComprehensiveConsentRequest(String customerId, String fiuId, String fipId,
                                                          List<String> consentTypes, List<String> fiTypes,
                                                          String purposeCode, String purposeText) {
        String timestamp = generateTimestamp();
        String transactionId = generateTransactionId();
        
        ConsentRequest.ConsentDetail consentDetail = new ConsentRequest.ConsentDetail();
        consentDetail.setConsentStart(timestamp);
        consentDetail.setConsentExpiry(generateExpiryTimestamp(90)); // 90 days
        consentDetail.setConsentMode("STORE");
        consentDetail.setFetchType("ONETIME");
        consentDetail.setConsentTypes(consentTypes);
        consentDetail.setFiTypes(fiTypes);
        
        // Set entities
        consentDetail.setDataConsumer(new ConsentRequest.DataConsumer(fiuId, "FIU"));
        consentDetail.setDataProvider(new ConsentRequest.DataProvider(fipId, "FIP"));
        
        // Set customer with multiple identifiers
        List<ConsentRequest.Identifier> identifiers = Arrays.asList(
                new ConsentRequest.Identifier("MOBILE", "+91-9876543210"),
                new ConsentRequest.Identifier("EMAIL", "customer@example.com"),
                new ConsentRequest.Identifier("PAN", "ABCDE1234F")
        );
        ConsentRequest.Customer customer = new ConsentRequest.Customer(customerId, identifiers);
        consentDetail.setCustomer(customer);
        
        // Set purpose
        ConsentRequest.Category category = new ConsentRequest.Category("PERSONAL");
        ConsentRequest.Purpose purpose = new ConsentRequest.Purpose(
                purposeCode,
                "https://api.rebit.org.in/aa/purpose/" + purposeCode + ".xml",
                purposeText,
                category
        );
        consentDetail.setPurpose(purpose);
        
        // Set data range and other settings
        consentDetail.setFiDataRange(new ConsentRequest.FIDataRange(
                generatePastTimestamp(730), // 2 years ago
                generatePastTimestamp(0)    // today
        ));
        consentDetail.setDataLife(new ConsentRequest.DataLife("MONTH", 12));
        consentDetail.setFrequency(new ConsentRequest.Frequency("HOUR", 1));
        
        ConsentRequest consentRequest = new ConsentRequest("1.0", timestamp, transactionId, consentDetail);
        
        logger.debug("Built comprehensive consent request: {}", consentRequest);
        return consentRequest;
    }
    
    /**
     * Validate consent response
     */
    public boolean validateConsentResponse(ConsentResponse response, String expectedCustomerId) {
        if (response == null) {
            logger.warn("Consent response is null");
            return false;
        }
        
        // Basic validations
        if (response.getVersion() == null || !response.getVersion().equals("1.0")) {
            logger.warn("Invalid version in consent response: {}", response.getVersion());
            return false;
        }
        
        if (response.getConsentHandle() == null || response.getConsentHandle().trim().isEmpty()) {
            logger.warn("Consent handle is null or empty");
            return false;
        }
        
        if (response.getCustomer() == null || !expectedCustomerId.equals(response.getCustomer().getId())) {
            logger.warn("Customer ID mismatch. Expected: {}, Actual: {}", 
                       expectedCustomerId, response.getCustomer() != null ? response.getCustomer().getId() : "null");
            return false;
        }
        
        if (response.getConsentStatus() == null) {
            logger.warn("Consent status is null");
            return false;
        }
        
        logger.info("Consent response validation passed");
        return true;
    }
    
    /**
     * Check if consent is in READY status
     */
    public boolean isConsentReady(ConsentResponse response) {
        return response != null && "READY".equals(response.getConsentStatus());
    }
    
    /**
     * Check if consent is in ACTIVE status
     */
    public boolean isConsentActive(ConsentResponse response) {
        return response != null && "ACTIVE".equals(response.getConsentStatus());
    }
    
    /**
     * Generate ISO timestamp
     */
    private String generateTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }
    
    /**
     * Generate expiry timestamp (days from now)
     */
    private String generateExpiryTimestamp(int daysFromNow) {
        return LocalDateTime.now().plusDays(daysFromNow)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }
    
    /**
     * Generate past timestamp (days ago)
     */
    private String generatePastTimestamp(int daysAgo) {
        return LocalDateTime.now().minusDays(daysAgo)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }
    
    /**
     * Generate unique transaction ID
     */
    private String generateTransactionId() {
        return "TXN_CONSENT_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId();
    }
    
    /**
     * Get common consent types
     */
    public static List<String> getCommonConsentTypes() {
        return Arrays.asList("PROFILE", "SUMMARY", "TRANSACTIONS");
    }
    
    /**
     * Get common FI types
     */
    public static List<String> getCommonFITypes() {
        return Arrays.asList("DEPOSIT", "TERM_DEPOSIT", "SIP", "CP", "GOVT_SECURITIES");
    }
    
    /**
     * Get test purpose codes
     */
    public static class PurposeCodes {
        public static final String WEALTH_MANAGEMENT = "101";
        public static final String CUSTOMER_ACQUISITION = "102";
        public static final String CREDIT_UNDERWRITING = "103";
        public static final String INVESTMENT_ADVISORY = "104";
        public static final String PERSONAL_FINANCE = "105";
    }
}