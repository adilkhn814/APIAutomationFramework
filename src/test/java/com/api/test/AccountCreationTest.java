package com.api.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.models.request.SignUpRequest;

import io.restassured.response.Response;

public class AccountCreationTest {
	
	@Test(description = "Verify if Sig Up API is working")
	public void createAccountTest() {
		
		SignUpRequest signUpRequest = new SignUpRequest.Builder()
		.email("adil81an@gmail.com")
		.firstName("adi916814")
		.lastName("lastadi")
		.username("adittuser@33")
		.password("adil@w23")
		.mobileNumber("0000898980").build();

		AuthService authService = new AuthService();
		Response response = authService.signUp(signUpRequest);
		Assert.assertEquals(response.asPrettyString(), "User registered successfully!");
		Assert.assertEquals(response.statusCode(), 200);
		
		
	}

}
