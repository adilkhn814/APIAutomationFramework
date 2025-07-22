package com.api.base;

import com.api.models.request.ConsentRequest;
import io.restassured.response.Response;

public class ConsentService extends BaseService {
    
    private static final String BASE_PATH = "/api/consent/";
    
    public Response createConsent(ConsentRequest consentRequest) {
        return postRequest(consentRequest, BASE_PATH + "create");
    }
    
    public void setAuthToken(String token) {
        super.setAuthToken(token);
    }
}
