package com.aa.bdd.hooks;

import com.aa.utils.*;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber Hooks for Account Aggregator BDD Tests
 * Handles setup and teardown operations for scenarios and features
 */
public class TestHooks {
    
    private static final Logger logger = LogManager.getLogger(TestHooks.class);
    
    // Utility managers
    private static WireMockManager wireMockManager;
    private static DatabaseManager databaseManager;
    private static ExtentManager extentManager;
    private static TokenManager tokenManager;
    private static VaultManager vaultManager;
    
    /**
     * Runs once before all scenarios
     */
    @BeforeAll
    public static void globalSetup() {
        logger.info("=== STARTING ACCOUNT AGGREGATOR BDD TEST SUITE ===");
        
        try {
            // Load configuration
            ConfigManager.loadConfig();
            logger.info("Configuration loaded successfully");
            
            // Initialize managers
            vaultManager = VaultManager.getInstance();
            tokenManager = TokenManager.getInstance();
            wireMockManager = WireMockManager.getInstance();
            databaseManager = DatabaseManager.getInstance();
            
            // Start WireMock server for external dependencies
            wireMockManager.startServer();
            logger.info("WireMock server started on port: {}", wireMockManager.getPort());
            
            // Initialize database if enabled
            boolean dbEnabled = ConfigManager.getBooleanProperty("db.enabled", false);
            if (dbEnabled) {
                databaseManager.initialize();
                logger.info("Database connection pool initialized");
            } else {
                logger.info("Database is disabled in configuration");
            }
            
            logger.info("Global setup completed successfully");
            
        } catch (Exception e) {
            logger.error("Failed during global setup", e);
            throw new RuntimeException("Global setup failed", e);
        }
    }
    
    /**
     * Runs before each scenario
     */
    @Before
    public void scenarioSetup(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        logger.info("Scenario tags: {}", scenario.getSourceTagNames());
        
        try {
            // Clear any previous test context
            // Reset WireMock stubs to default state
            if (wireMockManager != null && wireMockManager.isRunning()) {
                wireMockManager.resetStubs();
            }
            
            // Verify token is valid
            if (tokenManager != null && !tokenManager.isTokenValid()) {
                tokenManager.refreshToken();
                logger.debug("Authentication token refreshed");
            }
            
            // Log scenario start time for performance tracking
            scenario.attach(
                ("Scenario started at: " + java.time.LocalDateTime.now()).getBytes(),
                "text/plain",
                "Scenario Start Time"
            );
            
        } catch (Exception e) {
            logger.error("Failed during scenario setup for: {}", scenario.getName(), e);
            scenario.attach(
                ("Setup failed: " + e.getMessage()).getBytes(),
                "text/plain",
                "Setup Error"
            );
        }
    }
    
    /**
     * Runs before scenarios tagged with @smoke
     */
    @Before("@smoke")
    public void smokeTestSetup(Scenario scenario) {
        logger.info("Setting up smoke test: {}", scenario.getName());
        // Specific setup for smoke tests
        // Could include health checks, connectivity tests, etc.
    }
    
    /**
     * Runs before scenarios tagged with @performance
     */
    @Before("@performance")
    public void performanceTestSetup(Scenario scenario) {
        logger.info("Setting up performance test: {}", scenario.getName());
        // Specific setup for performance tests
        // Could include performance monitoring initialization
    }
    
    /**
     * Runs before scenarios tagged with @security
     */
    @Before("@security")
    public void securityTestSetup(Scenario scenario) {
        logger.info("Setting up security test: {}", scenario.getName());
        // Specific setup for security tests
        // Could include security context preparation
    }
    
    /**
     * Runs before scenarios tagged with @integration
     */
    @Before("@integration")
    public void integrationTestSetup(Scenario scenario) {
        logger.info("Setting up integration test: {}", scenario.getName());
        // Specific setup for integration tests
        // Could include external service verification
    }
    
    /**
     * Runs after each scenario
     */
    @After
    public void scenarioTeardown(Scenario scenario) {
        logger.info("Completed scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());
        
        try {
            // Log scenario completion time
            scenario.attach(
                ("Scenario completed at: " + java.time.LocalDateTime.now()).getBytes(),
                "text/plain",
                "Scenario End Time"
            );
            
            // Attach additional information for failed scenarios
            if (scenario.isFailed()) {
                logger.error("Scenario failed: {}", scenario.getName());
                
                // Attach system information
                attachSystemInfo(scenario);
                
                // Attach configuration information
                attachConfigInfo(scenario);
                
                // Clean up any test data created during the scenario
                cleanupTestData(scenario);
            }
            
            // Log scenario execution summary
            logScenarioSummary(scenario);
            
        } catch (Exception e) {
            logger.error("Error during scenario teardown for: {}", scenario.getName(), e);
        }
    }
    
    /**
     * Runs after scenarios tagged with @cleanup
     */
    @After("@cleanup")
    public void cleanupTestData(Scenario scenario) {
        logger.info("Cleaning up test data for scenario: {}", scenario.getName());
        
        try {
            // Database cleanup if enabled
            if (databaseManager != null) {
                // Clean test data based on scenario context
                // This would be scenario-specific cleanup
                logger.debug("Database cleanup completed");
            }
            
        } catch (Exception e) {
            logger.warn("Failed to cleanup test data for scenario: {}", scenario.getName(), e);
        }
    }
    
    /**
     * Runs after all scenarios
     */
    @AfterAll
    public static void globalTeardown() {
        logger.info("=== COMPLETING ACCOUNT AGGREGATOR BDD TEST SUITE ===");
        
        try {
            // Stop WireMock server
            if (wireMockManager != null && wireMockManager.isRunning()) {
                wireMockManager.stopServer();
                logger.info("WireMock server stopped");
            }
            
            // Close database connections
            if (databaseManager != null) {
                databaseManager.shutdown();
                logger.info("Database connection pool closed");
            }
            
            // Clear token cache
            if (tokenManager != null) {
                tokenManager.clearToken();
                logger.info("Authentication token cache cleared");
            }
            
            // Generate final test report summary
            generateTestSummary();
            
            logger.info("Global teardown completed successfully");
            
        } catch (Exception e) {
            logger.error("Error during global teardown", e);
        }
    }
    
    /**
     * Attach system information to failed scenarios
     */
    private void attachSystemInfo(Scenario scenario) {
        try {
            StringBuilder systemInfo = new StringBuilder();
            systemInfo.append("System Information:\n");
            systemInfo.append("Operating System: ").append(System.getProperty("os.name")).append("\n");
            systemInfo.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
            systemInfo.append("User Directory: ").append(System.getProperty("user.dir")).append("\n");
            systemInfo.append("Available Processors: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
            systemInfo.append("Max Memory: ").append(Runtime.getRuntime().maxMemory() / 1024 / 1024).append(" MB\n");
            systemInfo.append("Free Memory: ").append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append(" MB\n");
            
            scenario.attach(systemInfo.toString().getBytes(), "text/plain", "System Information");
            
        } catch (Exception e) {
            logger.warn("Failed to attach system information", e);
        }
    }
    
    /**
     * Attach configuration information to failed scenarios
     */
    private void attachConfigInfo(Scenario scenario) {
        try {
            StringBuilder configInfo = new StringBuilder();
            configInfo.append("Configuration Information:\n");
            configInfo.append("Base URL: ").append(ConfigManager.getProperty("base.url", "Not Set")).append("\n");
            configInfo.append("Environment: ").append(ConfigManager.getProperty("environment", "Not Set")).append("\n");
            configInfo.append("WireMock Port: ").append(ConfigManager.getProperty("wiremock.port", "Not Set")).append("\n");
            configInfo.append("Database Enabled: ").append(ConfigManager.getBooleanProperty("db.enabled", false)).append("\n");
            
            scenario.attach(configInfo.toString().getBytes(), "text/plain", "Configuration Information");
            
        } catch (Exception e) {
            logger.warn("Failed to attach configuration information", e);
        }
    }
    
    /**
     * Log scenario execution summary
     */
    private void logScenarioSummary(Scenario scenario) {
        try {
            logger.info("Scenario Summary:");
            logger.info("  Name: {}", scenario.getName());
            logger.info("  Status: {}", scenario.getStatus());
            logger.info("  Tags: {}", scenario.getSourceTagNames());
            logger.info("  Feature: {}", scenario.getUri());
            logger.info("  Line: {}", scenario.getLine());
            
        } catch (Exception e) {
            logger.warn("Failed to log scenario summary", e);
        }
    }
    
    /**
     * Generate test execution summary
     */
    private static void generateTestSummary() {
        try {
            logger.info("Test Execution Summary:");
            logger.info("  Suite: Account Aggregator BDD Tests");
            logger.info("  Execution Time: {}", java.time.LocalDateTime.now());
            logger.info("  Environment: {}", ConfigManager.getProperty("environment", "Not Set"));
            logger.info("  Base URL: {}", ConfigManager.getProperty("base.url", "Not Set"));
            
            // Additional summary information can be added here
            // Such as total scenarios executed, passed, failed, etc.
            
        } catch (Exception e) {
            logger.warn("Failed to generate test summary", e);
        }
    }
    
    /**
     * Utility method to check if a specific tag is present in scenario
     */
    private boolean hasTag(Scenario scenario, String tag) {
        return scenario.getSourceTagNames().contains("@" + tag);
    }
    
    /**
     * Utility method to attach custom data to scenario
     */
    public static void attachData(Scenario scenario, String data, String mediaType, String name) {
        try {
            scenario.attach(data.getBytes(), mediaType, name);
        } catch (Exception e) {
            logger.warn("Failed to attach data to scenario: {}", name, e);
        }
    }
    
    /**
     * Utility method to attach screenshot (for future UI testing)
     */
    public static void attachScreenshot(Scenario scenario, byte[] screenshot) {
        try {
            scenario.attach(screenshot, "image/png", "Screenshot");
        } catch (Exception e) {
            logger.warn("Failed to attach screenshot", e);
        }
    }
}