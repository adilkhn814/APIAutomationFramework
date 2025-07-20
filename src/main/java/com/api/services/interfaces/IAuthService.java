package com.api.services.interfaces;

import com.api.models.request.LoginRequest;
import com.api.models.request.ResetPasswordRequest;
import com.api.models.request.SignUpRequest;
import com.api.models.response.LoginResponse;
import io.restassured.response.Response;

/**
 * Authentication Service Interface
 * Defines contract for authentication operations
 */
public interface IAuthService {
    LoginResponse login(LoginRequest loginRequest);
    Response signUp(SignUpRequest signUpRequest);
    Response forgotPassword(String emailAddress);
    Response resetPassword(ResetPasswordRequest resetPasswordRequest, String token);
    boolean validateToken(String token);
    void invalidateToken(String token);
}