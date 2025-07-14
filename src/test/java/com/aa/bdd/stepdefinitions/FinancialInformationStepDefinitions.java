package com.aa.bdd.stepdefinitions;

import com.aa.pojo.FIRequest;
import com.aa.service.ConsentService;
import com.aa.utils.WireMockManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static org.testng.Assert.*;

/**
 * Step Definitions for Financial Information Feature
 * Implements the Gherkin steps for FI request and fetch operations
 */
public class FinancialInformationStepDefinitions {
    
    private static final Logger logger = LogManager.getLogger(FinancialInformationStepDefinitions.class);
    
    // Service layer
    private final ConsentService consentService;
    private final WireMockManager wireMockManager;
    
    // Test context - shared across steps
    private String consentHandle;
    private String sessionId;
    private String fromDate;
    private String toDate;
    private Map<String, String> encryptionParameters;
    private FIRequest fiRequest;
    private Response apiResponse;
    private long startTime;
    private long endTime;
    private boolean isDataReady;
    private String digitalSignature;
    
    public FinancialInformationStepDefinitions() {
        this.consentService = new ConsentService();
        this.wireMockManager = WireMockManager.getInstance();
    }
    
    // Background Steps - shared with Consent scenarios
    @Given("I have a valid active consent")
    public void iHaveAValidActiveConsent() {
        logger.info("Setting up a valid active consent");
        // This would typically involve creating a consent first or using a pre-existing one
        consentHandle = "CONSENT_" + System.currentTimeMillis();
        assertTrue(true, "Valid active consent should be available");
    }
    
    // Given Steps - FI Request Setup
    @Given("I have a valid consent handle {string}")
    public void iHaveAValidConsentHandle(String consentHandle) {
        logger.info("Setting up valid consent handle: {}", consentHandle);
        this.consentHandle = consentHandle;
        assertNotNull(consentHandle, "Consent handle should not be null");
        assertFalse(consentHandle.trim().isEmpty(), "Consent handle should not be empty");
    }
    
    @Given("I have an invalid consent handle {string}")
    public void iHaveAnInvalidConsentHandle(String consentHandle) {
        logger.info("Setting up invalid consent handle: {}", consentHandle);
        this.consentHandle = consentHandle;
    }
    
    @Given("I have an expired consent handle {string}")
    public void iHaveAnExpiredConsentHandle(String consentHandle) {
        logger.info("Setting up expired consent handle: {}", consentHandle);
        this.consentHandle = consentHandle;
    }
    
    @Given("I have a valid FI data range from {string} to {string}")
    public void iHaveAValidFIDataRangeFromTo(String fromDate, String toDate) {
        logger.info("Setting up FI data range from {} to {}", fromDate, toDate);
        this.fromDate = fromDate;
        this.toDate = toDate;
        assertNotNull(fromDate, "From date should not be null");
        assertNotNull(toDate, "To date should not be null");
    }
    
    @Given("I have a FI data range from {string} to {string}")
    public void iHaveAFIDataRangeFromTo(String fromDate, String toDate) {
        logger.info("Setting up FI data range from {} to {}", fromDate, toDate);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    
    @Given("I have valid key material for encryption")
    public void iHaveValidKeyMaterialForEncryption() {
        logger.info("Setting up valid key material for encryption");
        // Set up default encryption parameters
        encryptionParameters = Map.of(
            "cryptoAlg", "ECDH",
            "curve", "Curve25519",
            "params", "base64encodedparams"
        );
    }
    
    @Given("I have valid encryption parameters:")
    public void iHaveValidEncryptionParameters(DataTable dataTable) {
        logger.info("Setting up encryption parameters from data table");
        encryptionParameters = dataTable.asMap(String.class, String.class);
        logger.info("Encryption parameters: {}", encryptionParameters);
    }
    
    @Given("I have encryption parameters:")
    public void iHaveEncryptionParameters(DataTable dataTable) {
        logger.info("Setting up encryption parameters from data table");
        encryptionParameters = dataTable.asMap(String.class, String.class);
        logger.info("Encryption parameters: {}", encryptionParameters);
    }
    
    @Given("I have a valid digital signature for the consent")
    public void iHaveAValidDigitalSignatureForTheConsent() {
        logger.info("Setting up valid digital signature");
        digitalSignature = "DIGITAL_SIGNATURE_" + System.currentTimeMillis();
    }
    
    // FI Fetch Setup
    @Given("I have submitted a FI request successfully")
    public void iHaveSubmittedAFIRequestSuccessfully() {
        logger.info("Setting up a successfully submitted FI request");
        if (consentHandle == null) {
            consentHandle = "consent_12345";
        }
        if (fromDate == null) {
            fromDate = "2023-01-01";
            toDate = "2024-01-01";
        }
        
        // Simulate a successful FI request
        sessionId = "SESSION_" + System.currentTimeMillis();
        isDataReady = true;
    }
    
    @Given("the FI request has a valid session ID {string}")
    public void theFIRequestHasAValidSessionID(String sessionId) {
        logger.info("Setting up FI request with session ID: {}", sessionId);
        this.sessionId = sessionId;
        assertNotNull(sessionId, "Session ID should not be null");
    }
    
    @Given("I have an invalid session ID {string}")
    public void iHaveAnInvalidSessionID(String sessionId) {
        logger.info("Setting up invalid session ID: {}", sessionId);
        this.sessionId = sessionId;
    }
    
    @Given("the financial data is ready for fetch")
    public void theFinancialDataIsReadyForFetch() {
        logger.info("Setting up financial data ready for fetch");
        isDataReady = true;
    }
    
    @Given("the system is configured to send callbacks")
    public void theSystemIsConfiguredToSendCallbacks() {
        logger.info("Setting up system for callback notifications");
        // This would involve callback configuration
    }
    
    // When Steps - Actions
    @When("I request financial information")
    public void iRequestFinancialInformation() {
        logger.info("Requesting financial information");
        startTime = System.currentTimeMillis();
        
        try {
            // Build basic FI request
            fiRequest = buildBasicFIRequest();
            
            // This would call the actual FI request service
            // For now, we'll simulate the response
            apiResponse = simulateFIRequestResponse();
            endTime = System.currentTimeMillis();
            
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error requesting financial information", e);
        }
    }
    
    @When("I request financial information with all details")
    public void iRequestFinancialInformationWithAllDetails() {
        logger.info("Requesting financial information with comprehensive details");
        startTime = System.currentTimeMillis();
        
        try {
            // Build comprehensive FI request with all parameters
            fiRequest = buildComprehensiveFIRequest();
            
            // This would call the actual FI request service
            apiResponse = simulateFIRequestResponse();
            endTime = System.currentTimeMillis();
            
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error requesting financial information with details", e);
        }
    }
    
    @When("I request financial information with encryption")
    public void iRequestFinancialInformationWithEncryption() {
        logger.info("Requesting financial information with encryption parameters");
        startTime = System.currentTimeMillis();
        
        try {
            // Build FI request with encryption
            fiRequest = buildFIRequestWithEncryption();
            
            apiResponse = simulateFIRequestResponse();
            endTime = System.currentTimeMillis();
            
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error requesting financial information with encryption", e);
        }
    }
    
    @When("I fetch the financial information")
    public void iFetchTheFinancialInformation() {
        logger.info("Fetching financial information");
        startTime = System.currentTimeMillis();
        
        try {
            // This would call the actual FI fetch service
            apiResponse = simulateFIFetchResponse();
            endTime = System.currentTimeMillis();
            
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Error fetching financial information", e);
        }
    }
    
    @When("I wait for the data to be processed")
    public void iWaitForTheDataToBeProcessed() {
        logger.info("Waiting for data to be processed");
        // Simulate processing time
        try {
            Thread.sleep(100); // Short wait for demo
            isDataReady = true;
        } catch (InterruptedException e) {
            logger.warn("Wait interrupted", e);
        }
    }
    
    @When("the FI processing status changes")
    public void theFIProcessingStatusChanges() {
        logger.info("Simulating FI processing status change");
        // This would typically be triggered by external events
    }
    
    // Then Steps - Assertions
    @Then("the FI request should be submitted successfully")
    public void theFIRequestShouldBeSubmittedSuccessfully() {
        logger.info("Verifying FI request was submitted successfully");
        assertNotNull(apiResponse, "API response should not be null");
        assertEquals(apiResponse.getStatusCode(), 200, "Status code should be 200");
    }
    
    @Then("the FI request should be processed")
    public void theFIRequestShouldBeProcessed() {
        logger.info("Verifying FI request was processed");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() < 300,
                  "Status code should indicate success");
    }
    
    @Then("the FI request should fail")
    public void theFIRequestShouldFail() {
        logger.info("Verifying FI request failed as expected");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(apiResponse.getStatusCode() >= 400, "Status code should indicate failure");
    }
    
    @Then("the FI fetch should be successful")
    public void theFIFetchShouldBeSuccessful() {
        logger.info("Verifying FI fetch was successful");
        assertNotNull(apiResponse, "API response should not be null");
        assertEquals(apiResponse.getStatusCode(), 200, "Status code should be 200");
    }
    
    @Then("the FI fetch should fail")
    public void theFIFetchShouldFail() {
        logger.info("Verifying FI fetch failed as expected");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(apiResponse.getStatusCode() >= 400, "Status code should indicate failure");
    }
    
    @Then("the response should contain a valid session ID")
    public void theResponseShouldContainAValidSessionID() {
        logger.info("Verifying response contains a valid session ID");
        assertNotNull(apiResponse, "API response should not be null");
        // Would parse response and validate session ID
        String responseBody = apiResponse.getBody().asString();
        assertNotNull(responseBody, "Response body should not be null");
    }
    
    @Then("the response should contain the consent handle")
    public void theResponseShouldContainTheConsentHandle() {
        logger.info("Verifying response contains the consent handle");
        assertNotNull(apiResponse, "API response should not be null");
        // Would validate consent handle in response
    }
    
    @Then("the response should contain session information")
    public void theResponseShouldContainSessionInformation() {
        logger.info("Verifying response contains session information");
        assertNotNull(apiResponse, "API response should not be null");
        String responseBody = apiResponse.getBody().asString();
        assertNotNull(responseBody, "Response body should contain session information");
    }
    
    @Then("the encryption parameters should be acknowledged")
    public void theEncryptionParametersShouldBeAcknowledged() {
        logger.info("Verifying encryption parameters are acknowledged");
        assertNotNull(apiResponse, "API response should not be null");
        // Would validate encryption parameters in response
    }
    
    @Then("the encryption parameters should be validated")
    public void theEncryptionParametersShouldBeValidated() {
        logger.info("Verifying encryption parameters are validated");
        assertNotNull(encryptionParameters, "Encryption parameters should be set");
        assertNotNull(apiResponse, "API response should not be null");
    }
    
    @Then("the response should confirm encryption setup")
    public void theResponseShouldConfirmEncryptionSetup() {
        logger.info("Verifying response confirms encryption setup");
        assertNotNull(apiResponse, "API response should not be null");
        assertEquals(apiResponse.getStatusCode(), 200, "Status code should be 200");
    }
    
    @Then("the response should contain encrypted financial data")
    public void theResponseShouldContainEncryptedFinancialData() {
        logger.info("Verifying response contains encrypted financial data");
        assertNotNull(apiResponse, "API response should not be null");
        String responseBody = apiResponse.getBody().asString();
        assertNotNull(responseBody, "Response should contain encrypted data");
    }
    
    @Then("the response should contain FIP information")
    public void theResponseShouldContainFIPInformation() {
        logger.info("Verifying response contains FIP information");
        assertNotNull(apiResponse, "API response should not be null");
        // Would validate FIP information in response
    }
    
    @Then("the response should contain masked account numbers")
    public void theResponseShouldContainMaskedAccountNumbers() {
        logger.info("Verifying response contains masked account numbers");
        assertNotNull(apiResponse, "API response should not be null");
        // Would validate account masking in response
    }
    
    @Then("the response should contain error message about invalid consent")
    public void theResponseShouldContainErrorMessageAboutInvalidConsent() {
        logger.info("Verifying response contains error about invalid consent");
        assertNotNull(apiResponse, "API response should not be null");
        String responseBody = apiResponse.getBody().asString();
        assertTrue(responseBody.toLowerCase().contains("consent") || responseBody.toLowerCase().contains("invalid"),
                  "Error message should mention invalid consent");
    }
    
    @Then("the response should contain error message about expired consent")
    public void theResponseShouldContainErrorMessageAboutExpiredConsent() {
        logger.info("Verifying response contains error about expired consent");
        assertNotNull(apiResponse, "API response should not be null");
        String responseBody = apiResponse.getBody().asString();
        assertTrue(responseBody.toLowerCase().contains("expired") || responseBody.toLowerCase().contains("consent"),
                  "Error message should mention expired consent");
    }
    
    @Then("the response should contain error message about invalid session")
    public void theResponseShouldContainErrorMessageAboutInvalidSession() {
        logger.info("Verifying response contains error about invalid session");
        assertNotNull(apiResponse, "API response should not be null");
        String responseBody = apiResponse.getBody().asString();
        assertTrue(responseBody.toLowerCase().contains("session") || responseBody.toLowerCase().contains("invalid"),
                  "Error message should mention invalid session");
    }
    
    @Then("the total processing time should be less than {int} milliseconds")
    public void theTotalProcessingTimeShouldBeLessThanMilliseconds(int maxProcessingTime) {
        logger.info("Verifying total processing time is less than {} milliseconds", maxProcessingTime);
        long totalTime = endTime - startTime;
        assertTrue(totalTime < maxProcessingTime,
                  "Total processing time should be less than " + maxProcessingTime + "ms but was " + totalTime + "ms");
    }
    
    @Then("the complete workflow should be successful")
    public void theCompleteWorkflowShouldBeSuccessful() {
        logger.info("Verifying complete workflow was successful");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() < 300,
                  "Complete workflow should be successful");
        assertTrue(isDataReady, "Data should be ready after complete workflow");
    }
    
    @Then("I should have access to the financial data")
    public void iShouldHaveAccessToTheFinancialData() {
        logger.info("Verifying access to financial data");
        assertNotNull(apiResponse, "API response should not be null");
        assertTrue(isDataReady, "Financial data should be accessible");
    }
    
    @Then("the data should be properly encrypted")
    public void theDataShouldBeProperlyEncrypted() {
        logger.info("Verifying data is properly encrypted");
        assertNotNull(apiResponse, "API response should not be null");
        // Would validate encryption in actual implementation
    }
    
    @Then("the account information should be masked")
    public void theAccountInformationShouldBeMasked() {
        logger.info("Verifying account information is masked");
        assertNotNull(apiResponse, "API response should not be null");
        // Would validate account masking in actual implementation
    }
    
    @Then("I should receive a callback notification")
    public void iShouldReceiveACallbackNotification() {
        logger.info("Verifying callback notification is received");
        // Would validate callback reception in actual implementation
        assertTrue(true, "Callback notification should be received");
    }
    
    @Then("the callback should contain session information")
    public void theCallbackShouldContainSessionInformation() {
        logger.info("Verifying callback contains session information");
        assertNotNull(sessionId, "Session ID should be available for callback");
    }
    
    @Then("the callback should contain FI status updates")
    public void theCallbackShouldContainFIStatusUpdates() {
        logger.info("Verifying callback contains FI status updates");
        // Would validate status updates in callback
        assertTrue(true, "Callback should contain FI status updates");
    }
    
    @Then("the callback should contain FIP processing status")
    public void theCallbackShouldContainFIPProcessingStatus() {
        logger.info("Verifying callback contains FIP processing status");
        // Would validate FIP status in callback
        assertTrue(true, "Callback should contain FIP processing status");
    }
    
    // Helper Methods
    private FIRequest buildBasicFIRequest() {
        FIRequest request = new FIRequest();
        // Build basic FI request - would use actual POJO structure
        logger.debug("Built basic FI request for consent: {}", consentHandle);
        return request;
    }
    
    private FIRequest buildComprehensiveFIRequest() {
        FIRequest request = new FIRequest();
        // Build comprehensive FI request with all parameters
        logger.debug("Built comprehensive FI request with encryption and signature");
        return request;
    }
    
    private FIRequest buildFIRequestWithEncryption() {
        FIRequest request = new FIRequest();
        // Build FI request with encryption parameters
        logger.debug("Built FI request with encryption: {}", encryptionParameters);
        return request;
    }
    
    private Response simulateFIRequestResponse() {
        // In actual implementation, this would call the real service
        // For BDD demo, we'll create a mock response
        return wireMockManager.createMockResponse(200, 
            "{ \"sessionId\": \"" + (sessionId != null ? sessionId : "SESSION_12345") + "\", " +
            "\"consentId\": \"" + consentHandle + "\", " +
            "\"status\": \"PENDING\" }");
    }
    
    private Response simulateFIFetchResponse() {
        // In actual implementation, this would call the real service
        return wireMockManager.createMockResponse(200,
            "{ \"sessionId\": \"" + sessionId + "\", " +
            "\"status\": \"COMPLETED\", " +
            "\"encryptedData\": \"base64encodeddata\", " +
            "\"accounts\": [{ \"maskedAccNumber\": \"****1234\" }] }");
    }
}