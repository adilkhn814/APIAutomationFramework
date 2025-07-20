package com.api.services.interfaces;

import com.api.models.request.UpdateProfileRequest;
import com.api.models.response.GetProfileResponse;
import io.restassured.response.Response;

/**
 * User Profile Service Interface
 * Defines contract for user profile management operations
 */
public interface IUserProfileService {
    GetProfileResponse getProfile(String token);
    Response updateProfile(UpdateProfileRequest updateRequest, String token);
    Response partialUpdateProfile(UpdateProfileRequest updateRequest, String token);
    boolean validateProfileData(UpdateProfileRequest updateRequest);
}