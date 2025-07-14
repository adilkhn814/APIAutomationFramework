package com.aa.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extent Report Manager following Singleton pattern
 * Handles initialization and configuration of ExtentReports
 */
public class ExtentManager {
    
    private static final Logger logger = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    
    /**
     * Get ExtentReports instance (Singleton)
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }
    
    /**
     * Create ExtentReports instance with configuration
     */
    private static void createInstance() {
        try {
            String reportPath = getReportPath();
            
            // Create directory if it doesn't exist
            File reportFile = new File(reportPath);
            reportFile.getParentFile().mkdirs();
            
            // Initialize Spark Reporter
            sparkReporter = new ExtentSparkReporter(reportPath);
            configureSparkReporter();
            
            // Initialize ExtentReports
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            setSystemInfo();
            
            logger.info("ExtentReports initialized with report path: {}", reportPath);
            
        } catch (Exception e) {
            logger.error("Failed to initialize ExtentReports: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize ExtentReports", e);
        }
    }
    
    /**
     * Configure Spark Reporter settings
     */
    private static void configureSparkReporter() {
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Account Aggregator API Test Report");
        sparkReporter.config().setReportName("AA API Automation Test Results");
        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        
        // Add custom CSS for better appearance
        sparkReporter.config().setCss(getCustomCSS());
        
        // Add custom JavaScript if needed
        sparkReporter.config().setJs(getCustomJS());
    }
    
    /**
     * Set system information in the report
     */
    private static void setSystemInfo() {
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("OS Version", System.getProperty("os.version"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
        
        // Add configuration info
        extent.setSystemInfo("Base URL", ConfigManager.getProperty("base.url", "Not Set"));
        extent.setSystemInfo("Environment", ConfigManager.getProperty("environment", "TEST"));
        extent.setSystemInfo("Browser", "API Testing (RestAssured)");
        
        // Add build info if available
        String buildNumber = System.getProperty("build.number");
        if (buildNumber != null) {
            extent.setSystemInfo("Build Number", buildNumber);
        }
        
        String gitCommit = System.getProperty("git.commit");
        if (gitCommit != null) {
            extent.setSystemInfo("Git Commit", gitCommit);
        }
    }
    
    /**
     * Generate report file path with timestamp
     */
    private static String getReportPath() {
        String baseReportPath = ConfigManager.getProperty("extent.report.path", "reports/extent-report.html");
        
        // Add timestamp to filename
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);
        
        // Insert timestamp before file extension
        String fileName = baseReportPath.substring(0, baseReportPath.lastIndexOf('.'));
        String extension = baseReportPath.substring(baseReportPath.lastIndexOf('.'));
        
        return fileName + "_" + timestamp + extension;
    }
    
    /**
     * Custom CSS for report styling
     */
    private static String getCustomCSS() {
        return """
            .test-content {
                background-color: #f8f9fa;
                border-radius: 5px;
                padding: 10px;
                margin: 5px 0;
            }
            
            .step-details {
                font-family: 'Courier New', monospace;
                background-color: #f4f4f4;
                border: 1px solid #ddd;
                border-radius: 3px;
                padding: 8px;
                margin: 5px 0;
            }
            
            .pass { color: #28a745; }
            .fail { color: #dc3545; }
            .info { color: #17a2b8; }
            .warning { color: #ffc107; }
            """;
    }
    
    /**
     * Custom JavaScript for report functionality
     */
    private static String getCustomJS() {
        return """
            // Auto-refresh functionality for CI/CD environments
            if (window.location.search.includes('autorefresh=true')) {
                setTimeout(function() {
                    window.location.reload();
                }, 30000); // Refresh every 30 seconds
            }
            
            // Add print functionality
            function printReport() {
                window.print();
            }
            """;
    }
    
    /**
     * Flush the extent report
     */
    public static void flush() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed successfully");
        }
    }
    
    /**
     * Get the current report file path
     */
    public static String getReportFilePath() {
        if (sparkReporter != null) {
            return sparkReporter.config().getFilePath();
        }
        return null;
    }
}