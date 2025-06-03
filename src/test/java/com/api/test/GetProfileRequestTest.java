package com.api.test;

import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.base.UserProfileManagementService;
import com.api.models.request.LoginRequest;
import com.api.models.response.LoginResponse;
import com.api.models.response.UserProfileResponse;

import io.restassured.response.Response;

public class GetProfileRequestTest {

	@Test(description = "Verify if get profile api is working fine")
	public void getProfileInfoTest() {

		AuthService authService = new AuthService();
		Response response = authService.login(new LoginRequest("adilkhan814", "Adil@123"));
		LoginResponse loginResponse = response.as(LoginResponse.class);
		
		
		UserProfileManagementService userProfileManagementService = new UserProfileManagementService();
		response = userProfileManagementService.getProfile(loginResponse.getToken());
		UserProfileResponse  userProfileresponse =response.as(UserProfileResponse.class);
		System.out.println(userProfileresponse.getId());
	}

}
