package com.api.test;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.base.UserProfileManagementService;
import com.api.models.request.LoginRequest;
import com.api.models.request.ProfileRequest;
import com.api.models.response.LoginResponse;
import com.api.models.response.UserProfileResponse;

import io.restassured.response.Response;

@Listeners(com.api.listeners.TestListener.class)
public class UpdateProfileTest {

	@Test(description = "Verify if update profile api is working fine or not")
	public void updateProfileTest() {
		AuthService authService = new AuthService();
		Response response = authService.login(new LoginRequest("adilkhan814", "Adil@123"));
		LoginResponse loginresponse = response.as(LoginResponse.class);
		System.out.println(response.asPrettyString());
		
		System.out.println("-------------------------------------------");

		UserProfileManagementService userProfileManagementService = new UserProfileManagementService();
		response = userProfileManagementService.getProfile(loginresponse.getToken());
		System.out.println(loginresponse.getToken());
		System.out.println(response.asPrettyString());
		response.as(UserProfileResponse.class);

		System.out.println("-------------------------------------------"); 
		ProfileRequest profileRequest = new ProfileRequest.Builder().firstName("Arman").lastName("Mantri")
				.email("armankhn916@gmail.com").mobileNumber("0000000814").build();
		response = userProfileManagementService.updateProfile(profileRequest,loginresponse.getToken());
		
		

		System.out.println(response.asPrettyString());

	}

}
