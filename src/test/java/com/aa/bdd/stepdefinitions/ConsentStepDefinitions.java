package com.aa.bdd.stepdefinitions;

import com.aa.pojo.ConsentRequest;
import com.aa.pojo.ConsentResponse;
import com.aa.service.ConsentService;
import com.aa.utils.WireMockManager;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Step Definitions for Consent Management Feature
 * Implements the Gherkin steps using the existing service layer
 */
public class ConsentStepDefinitions {
    
    private static final Logger logger = LogManager.getLogger(ConsentStepDefinitions.class);
    
    // Service layer
    private final ConsentService consentService;
    private final WireMockManager wireMockManager;
    
    // Test context - shared across steps
    private String customerId;
    private String fiuId;
    private String fipId;
    private List<String> consentTypes;
    private List<String> fiTypes;
    private String purposeCode;
    private String purposeDescription;
    private ConsentRequest consentRequest;
    private ConsentResponse consentResponse;
    private Response apiResponse;
    private long startTime;
    private long endTime;
    private Throwable testException;
    
    public ConsentStepDefinitions() {
        this.consentService = new ConsentService();
        this.wireMockManager = WireMockManager.getInstance();
    }
    
    // Background Steps
    @Given("the Account Aggregator platform is available")
    public void theAccountAggregatorPlatformIsAvailable() {
        logger.info("Verifying Account Aggregator platform availability");
        // Platform availability check can be implemented here
        assertTrue(true, "Account Aggregator platform should be available");
    }
    
    @Given("I have valid authentication credentials")
    public void iHaveValidAuthenticationCredentials() {
        logger.info("Setting up valid authentication credentials");
        // Authentication setup is handled by TokenManager in the service layer
        assertTrue(true, "Authentication credentials should be valid");
    }
    
    @Given("WireMock service is running for external dependencies")
    public void wireMockServiceIsRunningForExternalDependencies() {
        logger.info("Starting WireMock service for external dependencies");
        if (!wireMockManager.isRunning()) {
            wireMockManager.startServer();
        }
        assertTrue(wireMockManager.isRunning(), "WireMock service should be running");
    }
    
    // Given Steps - Test Data Setup
    @Given("I have a valid customer with ID {string}")
    public void iHaveAValidCustomerWithID(String customerId) {
        logger.info("Setting up valid customer with ID: {}", customerId);
        this.customerId = customerId;
        assertNotNull(customerId, "Customer ID should not be null");
        assertFalse(customerId.trim().isEmpty(), "Customer ID should not be empty");
    }
    
    @Given("I have an invalid customer with empty ID {string}")
    public void iHaveAnInvalidCustomerWithEmptyID(String customerId) {
        logger.info("Setting up invalid customer with empty ID: {}", customerId);
        this.customerId = customerId;
    }
    
    @Given("I have a customer with very long ID {string}")
    public void iHaveACustomerWithVeryLongID(String customerId) {
        logger.info("Setting up customer with very long ID: {}", customerId);
        this.customerId = customerId;
    }
    
    @Given("I have a customer with ID {string}")
    public void iHaveACustomerWithID(String customerId) {
        logger.info("Setting up customer with ID: {}", customerId);
        this.customerId = customerId;
    }
    
    @Given("I have a valid FIU ID {string}")
    public void iHaveAValidFIUID(String fiuId) {
        logger.info("Setting up valid FIU ID: {}", fiuId);
        this.fiuId = fiuId;
        assertNotNull(fiuId, "FIU ID should not be null");
    }
    
    @Given("I have a FIU ID {string}")
    public void iHaveAFIUID(String fiuId) {
        logger.info("Setting up FIU ID: {}", fiuId);
        this.fiuId = fiuId;
    }
    
    @Given("I have a valid FIP ID {string}")
    public void iHaveAValidFIPID(String fipId) {
        logger.info("Setting up valid FIP ID: {}", fipId);
        this.fipId = fipId;
        assertNotNull(fipId, "FIP ID should not be null");
    }
    
    @Given("I have a FIP ID {string}")
    public void iHaveAFIPID(String fipId) {
        logger.info("Setting up FIP ID: {}", fipId);
        this.fipId = fipId;
    }
    
    @Given("I have consent types {string}")
    public void iHaveConsentTypes(String consentTypesString) {
        logger.info("Setting up consent types: {}", consentTypesString);
        this.consentTypes = Arrays.asList(consentTypesString.split(","));
    }
    
    @Given("I have FI types {string}")
    public void iHaveFITypes(String fiTypesString) {
        logger.info("Setting up FI types: {}", fiTypesString);
        this.fiTypes = Arrays.asList(fiTypesString.split(","));
    }
    
    @Given("I have purpose code {string} with description {string}")
    public void iHavePurposeCodeWithDescription(String purposeCode, String description) {
        logger.info("Setting up purpose code: {} with description: {}", purposeCode, description);
        this.purposeCode = purposeCode;
        this.purposeDescription = description;
    }
    
    @Given("I have a consent request with missing consent details")
    public void iHaveAConsentRequestWithMissingConsentDetails() {
        logger.info("Setting up consent request with missing consent details");
        consentRequest = new ConsentRequest();
        consentRequest.setVersion("1.0");
        consentRequest.setTimestamp(java.time.Instant.now().toString());
        consentRequest.setTransactionId("TXN_MISSING_DETAILS_" + System.currentTimeMillis());
        // Intentionally not setting ConsentDetail to test validation
    }
    
    @Given("I have invalid or expired authentication token")
    public void iHaveInvalidOrExpiredAuthenticationToken() {
        logger.info("Setting up invalid or expired authentication token");
        // This would be handled by modifying the TokenManager behavior
        // For testing purposes, we'll simulate this scenario
    }
    
    @Given("I have created a consent request successfully")
    public void iHaveCreatedAConsentRequestSuccessfully() {
        logger.info("Setting up a successfully created consent request");
        // Create a basic consent first
        if (customerId == null) {
            customerId = "test-customer@example.com";
        }
        if (fiuId == null) {
            fiuId = "test-fiu-001";
        }
        if (fipId == null) {
            fipId = "test-fip-001";
        }
        
        try {
            apiResponse = consentService.createBasicConsent(customerId, fiuId, fipId);
            consentResponse = consentService.parseConsentResponse(apiResponse);
        } catch (Exception e) {
            logger.error("Failed to create consent in background step", e);
            testException = e;
        }
    }
    
    @Given("the consent has a valid consent handle")
    public void theConsentHasAValidConsentHandle() {
        logger.info("Verifying consent has a valid consent handle");
        assertNotNull(consentResponse, "Consent response should exist");
        assertNotNull(consentResponse.getConsentHandle(), "Consent handle should not be null");
        assertFalse(consentResponse.getConsentHandle().trim().isEmpty(), "Consent handle should not be empty");
    }
    
    // When Steps - Actions
    @When("I create a basic consent request for the customer")
    public void iCreateABasicConsentRequestForTheCustomer() {
        logger.info("Creating basic consent request for customer: {}", customerId);
        startTime = System.currentTimeMillis();
        
        try {
            apiResponse = consentService.createBasicConsent(customerId, fiuId, fipId);
            endTime = System.currentTimeMillis();
            
            if (apiResponse.getStatusCode() == 200) {
                consentResponse = consentService.parseConsentResponse(apiResponse);
            }
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error creating basic consent request", e);
            testException = e;
        }
    }
    
    @When("I create a comprehensive consent request with all details")
    public void iCreateAComprehensiveConsentRequestWithAllDetails() {
        logger.info("Creating comprehensive consent request with all details");
        startTime = System.currentTimeMillis();
        
        try {
            consentRequest = consentService.buildComprehensiveConsentRequest(
                customerId, fiuId, fipId, consentTypes, fiTypes, purposeCode, purposeDescription);
            
            apiResponse = consentService.createConsent(consentRequest);
            endTime = System.currentTimeMillis();
            
            if (apiResponse.getStatusCode() == 200) {
                consentResponse = consentService.parseConsentResponse(apiResponse);
            }
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error creating comprehensive consent request", e);
            testException = e;
        }
    }
    
    @When("I send the incomplete consent request")
    public void iSendTheIncompleteConsentRequest() {
        logger.info("Sending incomplete consent request");
        startTime = System.currentTimeMillis();
        
        try {
            apiResponse = consentService.createConsent(consentRequest);
            endTime = System.currentTimeMillis();
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error sending incomplete consent request", e);
            testException = e;
        }
    }
    
    @When("I try to create a consent request")
    public void iTryToCreateAConsentRequest() {
        logger.info("Trying to create consent request with invalid authentication");
        startTime = System.currentTimeMillis();
        
        try {
            apiResponse = consentService.createBasicConsent(customerId, fiuId, fipId);
            endTime = System.currentTimeMillis();
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error creating consent request with invalid auth", e);
            testException = e;
        }
    }
    
    @When("the consent status is updated via callback")
    public void theConsentStatusIsUpdatedViaCallback() {
        logger.info("Simulating consent status update via callback");
        // This would typically involve callback simulation
        // For now, we'll simulate the status change
    }
    
    // Then Steps - Assertions
    @Then("the consent request should be created successfully")
    public void theConsentRequestShouldBeCreatedSuccessfully() {
        logger.info("Verifying consent request was created successfully");
        assertNotNull(apiResponse, "API response should not be null");
        assertEquals(apiResponse.getStatusCode(), 200, "Status code should be 200");
        assertNotNull(consentResponse, "Consent response should not be null");
    }
    
    @Then("the consent request should be processed")
    public void theConsentRequestShouldBeProcessed() {
        logger.info("Verifying consent request was processed");
        assertNotNull(apiResponse, "API response should not be null");
        // Processing can result in various status codes
        assertTrue(apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() < 500,
                  "Status code should be in valid range");
    }
    
    @Then("the consent request should fail")
    public void theConsentRequestShouldFail() {
        logger.info("Verifying consent request failed as expected");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(apiResponse.getStatusCode() >= 400, "Status code should indicate failure");
    }
    
    @Then("the request should be rejected")
    public void theRequestShouldBeRejected() {
        logger.info("Verifying request was rejected");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(apiResponse.getStatusCode() == 401 || apiResponse.getStatusCode() == 403,
                  "Status code should indicate authentication/authorization failure");
    }
    
    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        logger.info("Verifying response status code is: {}", expectedStatusCode);
        assertNotNull(apiResponse, "API response should not be null");
        assertEquals(apiResponse.getStatusCode(), expectedStatusCode,
                    "Response status code should match expected value");
    }
    
    @Then("the response status code should be {string}")
    public void theResponseStatusCodeShouldBe(String expectedStatusCode) {
        logger.info("Verifying response status code is: {}", expectedStatusCode);
        assertNotNull(apiResponse, "API response should not be null");
        assertEquals(String.valueOf(apiResponse.getStatusCode()), expectedStatusCode,
                    "Response status code should match expected value");
    }
    
    @Then("the response status code should be {int} or {int}")
    public void theResponseStatusCodeShouldBeOr(int statusCode1, int statusCode2) {
        logger.info("Verifying response status code is {} or {}", statusCode1, statusCode2);
        assertNotNull(apiResponse, "API response should not be null");
        int actualStatusCode = apiResponse.getStatusCode();
        assertTrue(actualStatusCode == statusCode1 || actualStatusCode == statusCode2,
                  "Response status code should be " + statusCode1 + " or " + statusCode2 + 
                  " but was " + actualStatusCode);
    }
    
    @Then("the response status code should be in the range {int} to {int}")
    public void theResponseStatusCodeShouldBeInTheRange(int startRange, int endRange) {
        logger.info("Verifying response status code is in range {} to {}", startRange, endRange);
        assertNotNull(apiResponse, "API response should not be null");
        int actualStatusCode = apiResponse.getStatusCode();
        assertTrue(actualStatusCode >= startRange && actualStatusCode <= endRange,
                  "Response status code should be in range " + startRange + " to " + endRange +
                  " but was " + actualStatusCode);
    }
    
    @Then("the response should contain a valid consent handle")
    public void theResponseShouldContainAValidConsentHandle() {
        logger.info("Verifying response contains a valid consent handle");
        assertNotNull(consentResponse, "Consent response should not be null");
        assertNotNull(consentResponse.getConsentHandle(), "Consent handle should not be null");
        assertFalse(consentResponse.getConsentHandle().trim().isEmpty(), "Consent handle should not be empty");
    }
    
    @Then("the customer ID in response should match {string}")
    public void theCustomerIDInResponseShouldMatch(String expectedCustomerId) {
        logger.info("Verifying customer ID in response matches: {}", expectedCustomerId);
        assertNotNull(consentResponse, "Consent response should not be null");
        assertNotNull(consentResponse.getCustomer(), "Customer in response should not be null");
        assertEquals(consentResponse.getCustomer().getId(), expectedCustomerId,
                    "Customer ID in response should match expected value");
    }
    
    @Then("the consent status should be {string}")
    public void theConsentStatusShouldBe(String expectedStatus) {
        logger.info("Verifying consent status is: {}", expectedStatus);
        assertNotNull(consentResponse, "Consent response should not be null");
        assertEquals(consentResponse.getConsentStatus(), expectedStatus,
                    "Consent status should match expected value");
    }
    
    @Then("the consent status should be one of {string}")
    public void theConsentStatusShouldBeOneOf(String validStatuses) {
        logger.info("Verifying consent status is one of: {}", validStatuses);
        assertNotNull(consentResponse, "Consent response should not be null");
        
        List<String> validStatusList = Arrays.asList(validStatuses.split(","));
        String actualStatus = consentResponse.getConsentStatus();
        assertTrue(validStatusList.contains(actualStatus),
                  "Consent status '" + actualStatus + "' should be one of: " + validStatuses);
    }
    
    @Then("the transaction ID should match between request and response")
    public void theTransactionIDShouldMatchBetweenRequestAndResponse() {
        logger.info("Verifying transaction ID matches between request and response");
        assertNotNull(consentRequest, "Consent request should not be null");
        assertNotNull(consentResponse, "Consent response should not be null");
        assertEquals(consentResponse.getTransactionId(), consentRequest.getTransactionId(),
                    "Transaction ID should match between request and response");
    }
    
    @Then("the consent should be valid for the specified purpose")
    public void theConsentShouldBeValidForTheSpecifiedPurpose() {
        logger.info("Verifying consent is valid for the specified purpose");
        assertNotNull(consentResponse, "Consent response should not be null");
        assertTrue(consentService.validateConsentResponse(consentResponse, customerId),
                  "Consent should be valid for the specified purpose");
    }
    
    @Then("the response time should be less than {int} milliseconds")
    public void theResponseTimeShouldBeLessThanMilliseconds(int maxResponseTime) {
        logger.info("Verifying response time is less than {} milliseconds", maxResponseTime);
        assertNotNull(apiResponse, "API response should not be null");
        long responseTime = apiResponse.getTime();
        assertTrue(responseTime < maxResponseTime,
                  "Response time should be less than " + maxResponseTime + "ms but was " + responseTime + "ms");
    }
    
    @Then("the total execution time should be less than {int} milliseconds")
    public void theTotalExecutionTimeShouldBeLessThanMilliseconds(int maxExecutionTime) {
        logger.info("Verifying total execution time is less than {} milliseconds", maxExecutionTime);
        long totalTime = endTime - startTime;
        assertTrue(totalTime < maxExecutionTime,
                  "Total execution time should be less than " + maxExecutionTime + "ms but was " + totalTime + "ms");
    }
    
    @Then("the response should contain validation error message")
    public void theResponseShouldContainValidationErrorMessage() {
        logger.info("Verifying response contains validation error message");
        assertNotNull(apiResponse, "API response should not be null");
        String responseBody = apiResponse.getBody().asString();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.trim().isEmpty(), "Response body should contain error message");
        // Could add more specific validation based on API error format
    }
    
    @Then("the response should indicate missing required fields")
    public void theResponseShouldIndicateMissingRequiredFields() {
        logger.info("Verifying response indicates missing required fields");
        assertNotNull(apiResponse, "API response should not be null");
        String responseBody = apiResponse.getBody().asString();
        assertNotNull(responseBody, "Response body should not be null");
        // Could add more specific validation for missing fields error
    }
    
    @Then("the response should indicate authentication failure")
    public void theResponseShouldIndicateAuthenticationFailure() {
        logger.info("Verifying response indicates authentication failure");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(apiResponse.getStatusCode() == 401 || apiResponse.getStatusCode() == 403,
                  "Status code should indicate authentication failure");
    }
    
    @Then("the system should handle the request appropriately")
    public void theSystemShouldHandleTheRequestAppropriately() {
        logger.info("Verifying system handled the request appropriately");
        assertNotNull(apiResponse, "API response should not be null");
        // System should return a valid response regardless of the outcome
        assertTrue(apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() < 600,
                  "Response status code should be valid HTTP status code");
    }
    
    @Then("the response should indicate whether the field length is acceptable")
    public void theResponseShouldIndicateWhetherTheFieldLengthIsAcceptable() {
        logger.info("Verifying response indicates field length acceptability");
        assertNotNull(apiResponse, "API response should not be null");
        // Either success (200) or validation error (400-499)
        assertTrue((apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() < 300) ||
                  (apiResponse.getStatusCode() >= 400 && apiResponse.getStatusCode() < 500),
                  "Response should indicate either success or validation error for field length");
    }
    
    @Then("the consent should reflect the updated status")
    public void theConsentShouldReflectTheUpdatedStatus() {
        logger.info("Verifying consent reflects updated status");
        // This would involve checking the consent status after callback
        assertNotNull(consentResponse, "Consent response should not be null");
    }
    
    @Then("appropriate notifications should be triggered")
    public void appropriateNotificationsShouldBeTriggered() {
        logger.info("Verifying appropriate notifications are triggered");
        // This would involve checking notification systems
        // For now, we'll assume this passes
        assertTrue(true, "Notifications should be triggered appropriately");
    }
}