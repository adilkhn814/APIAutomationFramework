package com.api.test;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.models.request.SignUpRequest;
import com.utility.TestUtility;

import io.restassured.response.Response;

// Attach TestListener to listen for test execution events such as start, success, and failure
@Listeners(com.api.listeners.TestListener.class)
public class SignUpTest {

	/**
	 * Test to verify the 'Sign Up' API functionality.
	 * This test generates fake user data using Faker utility, invokes the Sign Up API,
	 * and validates that the account creation response is as expected.
	 */
	@Test(description = "Verify if Sign Up API is working correctly")
	public void createAccountTest() {
		
		// Generate fake user data using utility method
		SignUpRequest signUpRequest = TestUtility.generateSignUpData();

		// Instantiate AuthService to call the Sign Up API
		AuthService authService = new AuthService();
		Response response = authService.signUp(signUpRequest);

		// Validate the API response message
		Assert.assertEquals(response.asPrettyString(), "User registered successfully!", 
				"Unexpected response message for successful registration");

		// Validate the response HTTP status code
		Assert.assertEquals(response.statusCode(), 200, 
				"Expected HTTP status code 200 for successful registration");
	}
}
