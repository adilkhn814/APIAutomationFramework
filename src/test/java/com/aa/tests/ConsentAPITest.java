package com.aa.tests;

import com.aa.base.BaseTest;
import com.aa.pojo.ConsentRequest;
import com.aa.pojo.ConsentResponse;
import com.aa.service.ConsentService;
import com.aa.utils.WireMockManager;
import io.restassured.response.Response;
import org.testng.annotations.*;
import static org.testng.Assert.*;

/**
 * Test class for Consent API
 * Demonstrates the usage of the AA test automation framework
 */
public class ConsentAPITest extends BaseTest {
    
    private ConsentService consentService;
    private WireMockManager wireMockManager;
    
    // Test data
    private static final String TEST_CUSTOMER_ID = "customer@example.com";
    private static final String TEST_FIU_ID = "test-fiu-001";
    private static final String TEST_FIP_ID = "test-fip-001";
    
    @BeforeClass
    public void classSetup() {
        logger.info("Setting up ConsentAPITest class");
        
        // Initialize services
        consentService = new ConsentService();
        wireMockManager = WireMockManager.getInstance();
        
        // Start WireMock server for mocking external dependencies
        wireMockManager.startServer();
        
        test.info("ConsentAPITest class setup completed");
    }
    
    @AfterClass
    public void classTeardown() {
        logger.info("Tearing down ConsentAPITest class");
        
        // Stop WireMock server
        if (wireMockManager != null) {
            wireMockManager.stopServer();
        }
        
        test.info("ConsentAPITest class teardown completed");
    }
    
    @Test(description = "Test basic consent creation with valid data")
    public void testCreateBasicConsent() {
        test.info("Starting test: Create Basic Consent");
        
        try {
            // Create basic consent request
            Response response = consentService.createBasicConsent(TEST_CUSTOMER_ID, TEST_FIU_ID, TEST_FIP_ID);
            
            // Log request and response
            logRequestDetails("/Consent", "Basic consent request");
            this.response = response;
            
            // Assertions
            assertStatusCode(200, "Consent creation should be successful");
            
            // Parse response
            ConsentResponse consentResponse = consentService.parseConsentResponse(response);
            assertNotNull(consentResponse, "Consent response should not be null");
            
            // Validate response structure
            boolean isValid = consentService.validateConsentResponse(consentResponse, TEST_CUSTOMER_ID);
            assertTrue(isValid, "Consent response should be valid");
            
            // Verify consent handle is generated
            assertNotNull(consentResponse.getConsentHandle(), "Consent handle should be generated");
            assertFalse(consentResponse.getConsentHandle().trim().isEmpty(), "Consent handle should not be empty");
            
            // Verify customer information
            assertNotNull(consentResponse.getCustomer(), "Customer information should be present");
            assertEquals(consentResponse.getCustomer().getId(), TEST_CUSTOMER_ID, "Customer ID should match");
            
            test.pass("Basic consent creation test passed successfully");
            logger.info("Basic consent creation test completed successfully");
            
        } catch (Exception e) {
            test.fail("Basic consent creation test failed: " + e.getMessage());
            logger.error("Basic consent creation test failed", e);
            throw e;
        }
    }
    
    @Test(description = "Test comprehensive consent creation with all fields")
    public void testCreateComprehensiveConsent() {
        test.info("Starting test: Create Comprehensive Consent");
        
        try {
            // Build comprehensive consent request
            ConsentRequest consentRequest = consentService.buildComprehensiveConsentRequest(
                    TEST_CUSTOMER_ID,
                    TEST_FIU_ID,
                    TEST_FIP_ID,
                    ConsentService.getCommonConsentTypes(),
                    ConsentService.getCommonFITypes(),
                    ConsentService.PurposeCodes.WEALTH_MANAGEMENT,
                    "Comprehensive wealth management and investment advisory services"
            );
            
            // Create consent
            Response response = consentService.createConsent(consentRequest);
            
            // Log request and response
            logRequestDetails("/Consent", consentRequest.toString());
            this.response = response;
            
            // Assertions
            assertStatusCode(200, "Comprehensive consent creation should be successful");
            
            // Parse and validate response
            ConsentResponse consentResponse = consentService.parseConsentResponse(response);
            assertTrue(consentService.validateConsentResponse(consentResponse, TEST_CUSTOMER_ID),
                      "Comprehensive consent response should be valid");
            
            // Verify transaction ID matches
            assertEquals(consentResponse.getTransactionId(), consentRequest.getTransactionId(),
                        "Transaction ID should match between request and response");
            
            test.pass("Comprehensive consent creation test passed successfully");
            logger.info("Comprehensive consent creation test completed successfully");
            
        } catch (Exception e) {
            test.fail("Comprehensive consent creation test failed: " + e.getMessage());
            logger.error("Comprehensive consent creation test failed", e);
            throw e;
        }
    }
    
    @Test(description = "Test consent creation with invalid customer ID")
    public void testCreateConsentWithInvalidCustomer() {
        test.info("Starting test: Create Consent with Invalid Customer");
        
        try {
            // Create consent with invalid customer ID
            String invalidCustomerId = "";
            Response response = consentService.createBasicConsent(invalidCustomerId, TEST_FIU_ID, TEST_FIP_ID);
            
            // Log request and response
            logRequestDetails("/Consent", "Invalid customer consent request");
            this.response = response;
            
            // Assertions - should fail with 400 Bad Request
            int statusCode = response.getStatusCode();
            assertTrue(statusCode == 400 || statusCode == 422, 
                      "Invalid customer should result in 400 or 422 status code, got: " + statusCode);
            
            test.pass("Invalid customer test passed - correctly rejected invalid request");
            logger.info("Invalid customer consent test completed successfully");
            
        } catch (Exception e) {
            test.fail("Invalid customer consent test failed: " + e.getMessage());
            logger.error("Invalid customer consent test failed", e);
            throw e;
        }
    }
    
    @Test(description = "Test consent creation with missing required fields", 
          dependsOnMethods = "testCreateBasicConsent")
    public void testCreateConsentWithMissingFields() {
        test.info("Starting test: Create Consent with Missing Fields");
        
        try {
            // Build request with missing required fields
            ConsentRequest incompleteRequest = new ConsentRequest();
            incompleteRequest.setVersion("1.0");
            incompleteRequest.setTimestamp(generateTimestamp());
            incompleteRequest.setTransactionId(generateTransactionId());
            // Missing ConsentDetail - should cause validation error
            
            Response response = consentService.createConsent(incompleteRequest);
            
            // Log request and response
            logRequestDetails("/Consent", incompleteRequest.toString());
            this.response = response;
            
            // Should fail with validation error
            int statusCode = response.getStatusCode();
            assertTrue(statusCode >= 400 && statusCode < 500, 
                      "Missing required fields should result in 4xx status code, got: " + statusCode);
            
            test.pass("Missing fields test passed - correctly rejected incomplete request");
            logger.info("Missing fields consent test completed successfully");
            
        } catch (Exception e) {
            test.fail("Missing fields consent test failed: " + e.getMessage());
            logger.error("Missing fields consent test failed", e);
            throw e;
        }
    }
    
    @Test(description = "Test consent response time performance")
    public void testConsentPerformance() {
        test.info("Starting test: Consent API Performance");
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Create consent
            Response response = consentService.createBasicConsent(TEST_CUSTOMER_ID, TEST_FIU_ID, TEST_FIP_ID);
            
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            
            // Log request and response
            logRequestDetails("/Consent", "Performance test consent request");
            this.response = response;
            
            // Performance assertions
            assertStatusCode(200, "Consent creation should be successful");
            
            // Check response time (should be less than 5 seconds)
            long responseTime = response.getTime();
            assertTrue(responseTime < 5000, 
                      "Response time should be less than 5 seconds, actual: " + responseTime + "ms");
            
            // Check total execution time (should be less than 10 seconds)
            assertTrue(totalTime < 10000, 
                      "Total execution time should be less than 10 seconds, actual: " + totalTime + "ms");
            
            test.info("Response time: " + responseTime + "ms, Total time: " + totalTime + "ms");
            test.pass("Performance test passed - response within acceptable limits");
            logger.info("Consent performance test completed successfully");
            
        } catch (Exception e) {
            test.fail("Consent performance test failed: " + e.getMessage());
            logger.error("Consent performance test failed", e);
            throw e;
        }
    }
    
    @Test(description = "Test consent status validation")
    public void testConsentStatusValidation() {
        test.info("Starting test: Consent Status Validation");
        
        try {
            // Create consent
            Response response = consentService.createBasicConsent(TEST_CUSTOMER_ID, TEST_FIU_ID, TEST_FIP_ID);
            
            // Log request and response
            logRequestDetails("/Consent", "Status validation consent request");
            this.response = response;
            
            assertStatusCode(200, "Consent creation should be successful");
            
            // Parse response and check status
            ConsentResponse consentResponse = consentService.parseConsentResponse(response);
            
            // Verify status is one of the expected values
            String status = consentResponse.getConsentStatus();
            assertNotNull(status, "Consent status should not be null");
            
            boolean validStatus = status.equals("PENDING") || status.equals("READY") || 
                                status.equals("ACTIVE") || status.equals("EXPIRED") || 
                                status.equals("DENIED") || status.equals("REVOKED");
            assertTrue(validStatus, "Consent status should be a valid value, got: " + status);
            
            // Test status helper methods
            if (status.equals("READY")) {
                assertTrue(consentService.isConsentReady(consentResponse), "isConsentReady should return true");
            } else if (status.equals("ACTIVE")) {
                assertTrue(consentService.isConsentActive(consentResponse), "isConsentActive should return true");
            }
            
            test.pass("Consent status validation test passed");
            logger.info("Consent status validation test completed successfully");
            
        } catch (Exception e) {
            test.fail("Consent status validation test failed: " + e.getMessage());
            logger.error("Consent status validation test failed", e);
            throw e;
        }
    }
}