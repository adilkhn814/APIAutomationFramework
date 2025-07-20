package com.api.services.interfaces;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * HTTP Client Service Interface
 * Provides abstraction for HTTP operations
 */
public interface IHttpClientService {
    Response get(String endpoint);
    Response post(String endpoint, Object payload);
    Response put(String endpoint, Object payload);
    Response patch(String endpoint, Object payload);
    Response delete(String endpoint);
    
    RequestSpecification getRequestSpecification();
    void setAuthToken(String token);
    void setHeader(String key, String value);
    void removeHeader(String key);
    void reset();
}