package com.api.test;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.models.request.LoginRequest;
import com.api.models.response.LoginResponse;

import io.restassured.response.Response;

@Listeners(com.api.listeners.TestListener.class)
public class LoginTest {

	
	@Test(description = "Verify if login api is working")
	public void loginTest() {

		LoginRequest loginRequest = new LoginRequest("adilkhan814", "Adil@123");
		AuthService authService = new AuthService();
		Response response = authService.login(loginRequest);
		System.out.println(response.asPrettyString());
		LoginResponse loginResponse = response.as(LoginResponse.class);
		System.out.println(response.asPrettyString());
		
		System.out.println(loginResponse.getRoles());
		

	}

}
