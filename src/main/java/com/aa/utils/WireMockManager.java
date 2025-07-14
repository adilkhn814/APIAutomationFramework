package com.aa.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * WireMock Manager following Singleton pattern
 * Handles mocking of external services and dependencies
 */
public class WireMockManager {
    
    private static final Logger logger = LogManager.getLogger(WireMockManager.class);
    private static WireMockManager instance;
    private static final ReentrantLock lock = new ReentrantLock();
    
    private WireMockServer wireMockServer;
    private boolean isRunning = false;
    private int port;
    
    private WireMockManager() {
        port = ConfigManager.getIntProperty("wiremock.port", 8089);
    }
    
    /**
     * Get WireMockManager instance (Singleton)
     */
    public static WireMockManager getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new WireMockManager();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }
    
    /**
     * Start WireMock server
     */
    public void startServer() {
        if (!isRunning) {
            lock.lock();
            try {
                if (!isRunning) {
                    configureAndStartServer();
                    setupDefaultStubs();
                    isRunning = true;
                }
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * Configure and start WireMock server
     */
    private void configureAndStartServer() {
        try {
            WireMockConfiguration config = WireMockConfiguration.options()
                    .port(port)
                    .enableBrowserProxying(false)
                    .disableRequestJournal();
            
            wireMockServer = new WireMockServer(config);
            wireMockServer.start();
            
            // Configure WireMock client
            WireMock.configureFor("localhost", port);
            
            logger.info("WireMock server started on port: {}", port);
            
        } catch (Exception e) {
            logger.error("Failed to start WireMock server: {}", e.getMessage(), e);
            throw new RuntimeException("WireMock server startup failed", e);
        }
    }
    
    /**
     * Setup default stub mappings for common AA services
     */
    private void setupDefaultStubs() {
        setupAuthServiceStubs();
        setupFIUServiceStubs();
        setupFIPServiceStubs();
        setupCallbackStubs();
        
        logger.info("Default WireMock stubs configured");
    }
    
    /**
     * Setup authentication service stubs
     */
    private void setupAuthServiceStubs() {
        // OAuth token endpoint
        stubFor(post(urlEqualTo("/oauth/token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "access_token": "mock_access_token_12345",
                                    "token_type": "Bearer",
                                    "expires_in": 3600,
                                    "scope": "aa_api"
                                }
                                """)));
        
        // Token validation endpoint
        stubFor(get(urlEqualTo("/oauth/validate"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "valid": true,
                                    "expires_in": 3600,
                                    "scope": "aa_api"
                                }
                                """)));
    }
    
    /**
     * Setup FIU (Financial Information User) service stubs
     */
    private void setupFIUServiceStubs() {
        // FI Request endpoint
        stubFor(post(urlEqualTo("/FI/request"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "ver": "1.0",
                                    "timestamp": "2024-01-15T10:30:00Z",
                                    "txnid": "mock_txn_12345",
                                    "ConsentHandle": "mock_consent_handle_12345"
                                }
                                """)));
        
        // FI Fetch endpoint
        stubFor(post(urlEqualTo("/FI/fetch"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "ver": "1.0",
                                    "timestamp": "2024-01-15T10:30:00Z",
                                    "txnid": "mock_txn_12345",
                                    "FI": [{
                                        "fipID": "mock_fip_001",
                                        "data": "encrypted_financial_data",
                                        "linkRefNumber": "mock_link_ref_123",
                                        "maskedAccNumber": "XXXX1234"
                                    }]
                                }
                                """)));
    }
    
    /**
     * Setup FIP (Financial Information Provider) service stubs
     */
    private void setupFIPServiceStubs() {
        // Account discovery endpoint
        stubFor(post(urlEqualTo("/Accounts/Discover"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "ver": "1.0",
                                    "timestamp": "2024-01-15T10:30:00Z",
                                    "txnid": "mock_discovery_txn_123",
                                    "Customer": {
                                        "id": "customer@email.com"
                                    },
                                    "Accounts": [{
                                        "linkRefNumber": "mock_link_ref_123",
                                        "maskedAccNumber": "XXXX1234",
                                        "AccType": "SAVINGS"
                                    }]
                                }
                                """)));
        
        // Account linking endpoint
        stubFor(post(urlEqualTo("/Accounts/Link"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "ver": "1.0",
                                    "timestamp": "2024-01-15T10:30:00Z",
                                    "txnid": "mock_link_txn_123",
                                    "Customer": {
                                        "id": "customer@email.com"
                                    },
                                    "RefNumber": "mock_ref_number_123"
                                }
                                """)));
    }
    
    /**
     * Setup callback stubs
     */
    private void setupCallbackStubs() {
        // Consent callback
        stubFor(post(urlEqualTo("/callback/Consent"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "ver": "1.0",
                                    "timestamp": "2024-01-15T10:30:00Z",
                                    "txnid": "mock_consent_callback_123",
                                    "ConsentStatus": "READY"
                                }
                                """)));
        
        // FI Data callback
        stubFor(post(urlEqualTo("/callback/FIData"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "ver": "1.0",
                                    "timestamp": "2024-01-15T10:30:00Z",
                                    "txnid": "mock_fi_callback_123",
                                    "sessionId": "mock_session_123",
                                    "FIStatusResponse": {
                                        "sessionStatus": "COMPLETED",
                                        "FIStatusNotification": [{
                                            "fipID": "mock_fip_001",
                                            "Accounts": [{
                                                "linkRefNumber": "mock_link_ref_123",
                                                "FIStatus": "READY"
                                            }]
                                        }]
                                    }
                                }
                                """)));
    }
    
    /**
     * Add custom stub mapping
     */
    public void addStub(String method, String url, int statusCode, String responseBody) {
        addStub(method, url, statusCode, responseBody, "application/json");
    }
    
    /**
     * Add custom stub mapping with content type
     */
    public void addStub(String method, String url, int statusCode, String responseBody, String contentType) {
        try {
            switch (method.toUpperCase()) {
                case "GET":
                    stubFor(get(urlEqualTo(url))
                            .willReturn(aResponse()
                                    .withStatus(statusCode)
                                    .withHeader("Content-Type", contentType)
                                    .withBody(responseBody)));
                    break;
                case "POST":
                    stubFor(post(urlEqualTo(url))
                            .willReturn(aResponse()
                                    .withStatus(statusCode)
                                    .withHeader("Content-Type", contentType)
                                    .withBody(responseBody)));
                    break;
                case "PUT":
                    stubFor(put(urlEqualTo(url))
                            .willReturn(aResponse()
                                    .withStatus(statusCode)
                                    .withHeader("Content-Type", contentType)
                                    .withBody(responseBody)));
                    break;
                case "DELETE":
                    stubFor(delete(urlEqualTo(url))
                            .willReturn(aResponse()
                                    .withStatus(statusCode)
                                    .withHeader("Content-Type", contentType)
                                    .withBody(responseBody)));
                    break;
                default:
                    logger.warn("Unsupported HTTP method: {}", method);
            }
            
            logger.debug("Custom stub added: {} {} -> {}", method, url, statusCode);
            
        } catch (Exception e) {
            logger.error("Error adding custom stub: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Remove all stubs
     */
    public void resetStubs() {
        if (isRunning) {
            WireMock.reset();
            setupDefaultStubs();
            logger.info("WireMock stubs reset and defaults reloaded");
        }
    }
    
    /**
     * Stop WireMock server
     */
    public void stopServer() {
        if (isRunning && wireMockServer != null) {
            lock.lock();
            try {
                wireMockServer.stop();
                isRunning = false;
                logger.info("WireMock server stopped");
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * Get WireMock server port
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Get WireMock base URL
     */
    public String getBaseUrl() {
        return "http://localhost:" + port;
    }
    
    /**
     * Check if WireMock server is running
     */
    public boolean isRunning() {
        return isRunning && wireMockServer != null && wireMockServer.isRunning();
    }
    
    /**
     * Get WireMock admin URL
     */
    public String getAdminUrl() {
        return getBaseUrl() + "/__admin";
    }
    
    /**
     * Health check for WireMock server
     */
    public boolean healthCheck() {
        try {
            if (!isRunning()) {
                return false;
            }
            
            // Try to access admin endpoint
            String adminToken = VaultManager.getInstance().getSecret("wiremock.admin.token");
            // In a real implementation, you would make an HTTP request to the admin endpoint
            
            return true;
            
        } catch (Exception e) {
            logger.error("WireMock health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Create mock response for BDD testing
     */
    public io.restassured.response.Response createMockResponse(int statusCode, String responseBody) {
        try {
            logger.debug("Creating mock response with status: {} and body: {}", statusCode, responseBody);
            return new MockResponse(statusCode, responseBody);
        } catch (Exception e) {
            logger.error("Error creating mock response", e);
            throw new RuntimeException("Failed to create mock response", e);
        }
    }
    
    /**
     * Simple mock response implementation for BDD testing
     */
    private static class MockResponse implements io.restassured.response.Response {
        private final int statusCode;
        private final String body;
        private final long responseTime = 100; // Simulate 100ms response time
        
        public MockResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
        
        @Override public int getStatusCode() { return statusCode; }
        @Override public long getTime() { return responseTime; }
        @Override public io.restassured.response.ResponseBody getBody() { return new MockResponseBody(body); }
        @Override public String asString() { return body; }
        @Override public String print() { return body; }
        
        // Simplified implementations for other required methods
        @Override public String getStatusLine() { return "HTTP/1.1 " + statusCode; }
        @Override public io.restassured.response.Headers getHeaders() { return new io.restassured.response.Headers(); }
        @Override public String getHeader(String name) { return null; }
        @Override public java.util.Map<String, String> getCookies() { return new java.util.HashMap<>(); }
        @Override public String getCookie(String name) { return null; }
        @Override public io.restassured.response.ResponseOptions<?> then() { return this; }
        @Override public String asPrettyString() { return body; }
        @Override public byte[] asByteArray() { return body.getBytes(); }
        @Override public java.io.InputStream asInputStream() { return new java.io.ByteArrayInputStream(body.getBytes()); }
        @Override public <T> T as(Class<T> cls) { throw new UnsupportedOperationException(); }
        @Override public <T> T as(Class<T> cls, io.restassured.mapper.ObjectMapperType mapperType) { throw new UnsupportedOperationException(); }
        @Override public <T> T as(io.restassured.common.mapper.TypeRef<T> typeRef) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.json.JsonPath jsonPath() { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.json.JsonPath jsonPath(io.restassured.path.json.config.JsonPathConfig config) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath xmlPath() { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath xmlPath(io.restassured.path.xml.config.XmlPathConfig config) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath xmlPath(io.restassured.path.xml.XmlPath.CompatibilityMode compatibilityMode) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath htmlPath() { throw new UnsupportedOperationException(); }
        @Override public <T> T path(String path, String... arguments) { throw new UnsupportedOperationException(); }
        @Override public String prettyPrint() { return body; }
        @Override public io.restassured.response.Response peek() { return this; }
        @Override public io.restassured.response.Response prettyPeek() { return this; }
        @Override public io.restassured.response.ValidatableResponse then() { throw new UnsupportedOperationException(); }
        @Override public io.restassured.response.ResponseBody body() { return getBody(); }
        @Override public io.restassured.response.Headers headers() { return getHeaders(); }
        @Override public java.util.Map<String, String> cookies() { return getCookies(); }
        @Override public int statusCode() { return statusCode; }
        @Override public String statusLine() { return getStatusLine(); }
        @Override public String cookie(String name) { return getCookie(name); }
        @Override public String header(String name) { return getHeader(name); }
        @Override public long time() { return responseTime; }
        @Override public long timeIn(java.util.concurrent.TimeUnit timeUnit) { return responseTime; }
        @Override public String getContentType() { return "application/json"; }
        @Override public io.restassured.http.ContentType contentType() { return io.restassured.http.ContentType.JSON; }
        @Override public String getSessionId() { return null; }
        @Override public String sessionId() { return null; }
        @Override public io.restassured.response.DetailedCookies getDetailedCookies() { return new io.restassured.response.DetailedCookies(); }
        @Override public io.restassured.response.DetailedCookies detailedCookies() { return getDetailedCookies(); }
        @Override public io.restassured.response.DetailedCookie getDetailedCookie(String name) { return null; }
        @Override public io.restassured.response.DetailedCookie detailedCookie(String name) { return null; }
    }
    
    /**
     * Mock response body implementation
     */
    private static class MockResponseBody implements io.restassured.response.ResponseBody {
        private final String body;
        
        public MockResponseBody(String body) {
            this.body = body;
        }
        
        @Override public String asString() { return body; }
        @Override public String asPrettyString() { return body; }
        @Override public byte[] asByteArray() { return body.getBytes(); }
        @Override public java.io.InputStream asInputStream() { return new java.io.ByteArrayInputStream(body.getBytes()); }
        @Override public <T> T as(Class<T> cls) { throw new UnsupportedOperationException(); }
        @Override public <T> T as(Class<T> cls, io.restassured.mapper.ObjectMapperType mapperType) { throw new UnsupportedOperationException(); }
        @Override public <T> T as(io.restassured.common.mapper.TypeRef<T> typeRef) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.json.JsonPath jsonPath() { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.json.JsonPath jsonPath(io.restassured.path.json.config.JsonPathConfig config) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath xmlPath() { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath xmlPath(io.restassured.path.xml.config.XmlPathConfig config) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath xmlPath(io.restassured.path.xml.XmlPath.CompatibilityMode compatibilityMode) { throw new UnsupportedOperationException(); }
        @Override public io.restassured.path.xml.XmlPath htmlPath() { throw new UnsupportedOperationException(); }
        @Override public <T> T path(String path, String... arguments) { throw new UnsupportedOperationException(); }
        @Override public String print() { return body; }
        @Override public String prettyPrint() { return body; }
    }
}