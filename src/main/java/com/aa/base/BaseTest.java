package com.aa.base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aa.utils.ConfigManager;
import com.aa.utils.ExtentManager;
import com.aa.utils.TokenManager;
import java.io.File;
import java.lang.reflect.Method;

/**
 * Base Test class providing common functionality for all API tests
 * Handles setup, teardown, logging, and reporting
 */
public class BaseTest {
    
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static ExtentReports extent;
    protected static ExtentTest test;
    protected RequestSpecification requestSpec;
    protected Response response;
    
    // Test data
    protected String baseURI;
    protected String authToken;
    
    @BeforeSuite
    public void suiteSetup() {
        logger.info("Starting Account Aggregator Test Suite");
        
        // Initialize Extent Reports
        extent = ExtentManager.getInstance();
        
        // Load configuration
        ConfigManager.loadConfig();
        baseURI = ConfigManager.getProperty("base.url", "https://api.account-aggregator.com");
        
        // Set RestAssured base URI
        RestAssured.baseURI = baseURI;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        logger.info("Base URI set to: {}", baseURI);
    }
    
    @BeforeMethod
    public void testSetup(Method method) {
        // Create test in extent report
        test = extent.createTest(method.getName());
        
        // Get authentication token
        authToken = TokenManager.getInstance().getAuthToken();
        
        // Initialize request specification
        requestSpec = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .header("Accept", "application/json");
        
        logger.info("Starting test: {}", method.getName());
        test.info("Test started: " + method.getName());
    }
    
    @AfterMethod
    public void testTeardown() {
        if (response != null) {
            logResponseDetails();
        }
        logger.info("Test completed");
    }
    
    @AfterSuite
    public void suiteTeardown() {
        if (extent != null) {
            extent.flush();
        }
        logger.info("Account Aggregator Test Suite completed");
    }
    
    /**
     * Log request and response details for debugging and reporting
     */
    protected void logRequestDetails(String endpoint, String requestBody) {
        logger.info("API Endpoint: {}", endpoint);
        logger.info("Request Body: {}", requestBody);
        
        test.info("API Endpoint: " + endpoint);
        test.info("Request Body: <pre>" + requestBody + "</pre>");
    }
    
    /**
     * Log response details
     */
    protected void logResponseDetails() {
        if (response != null) {
            int statusCode = response.getStatusCode();
            String responseBody = response.getBody().asString();
            
            logger.info("Response Status Code: {}", statusCode);
            logger.info("Response Body: {}", responseBody);
            
            test.info("Response Status Code: " + statusCode);
            test.info("Response Body: <pre>" + responseBody + "</pre>");
            test.info("Response Time: " + response.getTime() + " ms");
        }
    }
    
    /**
     * Generate unique transaction ID for requests
     */
    protected String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId();
    }
    
    /**
     * Generate timestamp in ISO format
     */
    protected String generateTimestamp() {
        return java.time.Instant.now().toString();
    }
    
    /**
     * Common assertion method with logging
     */
    protected void assertStatusCode(int expectedStatusCode, String message) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode == expectedStatusCode) {
            logger.info("Status code assertion passed: Expected {}, Actual {}", expectedStatusCode, actualStatusCode);
            test.pass(message + " - Status code: " + actualStatusCode);
        } else {
            logger.error("Status code assertion failed: Expected {}, Actual {}", expectedStatusCode, actualStatusCode);
            test.fail(message + " - Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
            throw new AssertionError("Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
        }
    }
}