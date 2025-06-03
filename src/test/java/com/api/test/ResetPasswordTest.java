package com.api.test;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.models.request.LoginRequest;
import com.api.models.request.ResetPasswordRequest;
import com.api.models.response.LoginResponse;

import io.restassured.response.Response;

@Listeners(com.api.listeners.TestListener.class)
public class ResetPasswordTest {

	@Test(description = "Verify if Reset API is working fine")
	public void resetPasswordTest() {

		LoginRequest loginRequest = new LoginRequest("adilkhan814", "Adil@123");
		AuthService authService = new AuthService();
		Response response = authService.login(loginRequest);
		LoginResponse loginResponse = response.as(LoginResponse.class);
		System.out.println("******************");
		System.out.println(loginResponse.getToken());
		Assert.assertEquals(response.getStatusCode(), 200);

		ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(loginResponse.getToken(), "Password@33",
				"Password@33");

		response = authService.resetPassword(resetPasswordRequest, loginResponse.getToken());
		System.out.println("******************");
		System.out.println(response.asPrettyString());
		Assert.assertEquals(response.getStatusCode(), 400);

	}

}
