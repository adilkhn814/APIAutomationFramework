package com.api.services.impl;

import com.api.services.interfaces.IConfigurationService;
import com.api.services.interfaces.IHttpClientService;
import com.api.filters.LoggingFilter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

/**
 * HTTP Client Service Implementation
 * Centralized HTTP operations with enhanced configuration and error handling
 */
public class HttpClientServiceImpl implements IHttpClientService {
    
    private final IConfigurationService configService;
    private RequestSpecification requestSpecification;
    private final Map<String, String> headers = new ConcurrentHashMap<>();
    
    public HttpClientServiceImpl(IConfigurationService configService) {
        this.configService = configService;
        initializeRestAssured();
        createNewRequestSpecification();
    }
    
    private void initializeRestAssured() {
        if (configService.isLoggingEnabled()) {
            RestAssured.filters(new LoggingFilter());
        }
        
        RestAssured.config = RestAssured.config()
            .httpClient(RestAssured.config().getHttpClientConfig()
                .setParam("http.connection.timeout", configService.getRequestTimeout())
                .setParam("http.socket.timeout", configService.getRequestTimeout()));
    }
    
    private void createNewRequestSpecification() {
        requestSpecification = given()
            .baseUri(configService.getBaseUrl())
            .contentType(ContentType.JSON)
            .relaxedHTTPSValidation();
            
        // Apply all stored headers
        headers.forEach((key, value) -> requestSpecification.header(key, value));
    }
    
    @Override
    public Response get(String endpoint) {
        return requestSpecification.get(endpoint);
    }
    
    @Override
    public Response post(String endpoint, Object payload) {
        return requestSpecification.body(payload).post(endpoint);
    }
    
    @Override
    public Response put(String endpoint, Object payload) {
        return requestSpecification.body(payload).put(endpoint);
    }
    
    @Override
    public Response patch(String endpoint, Object payload) {
        return requestSpecification.body(payload).patch(endpoint);
    }
    
    @Override
    public Response delete(String endpoint) {
        return requestSpecification.delete(endpoint);
    }
    
    @Override
    public RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }
    
    @Override
    public void setAuthToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            setHeader("Authorization", "Bearer " + token);
        }
    }
    
    @Override
    public void setHeader(String key, String value) {
        headers.put(key, value);
        requestSpecification = requestSpecification.header(key, value);
    }
    
    @Override
    public void removeHeader(String key) {
        headers.remove(key);
        // Recreate request specification without the removed header
        createNewRequestSpecification();
    }
    
    @Override
    public void reset() {
        headers.clear();
        createNewRequestSpecification();
    }
}