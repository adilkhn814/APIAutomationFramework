package com.api.test;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.models.request.SignUpRequest;
import com.utility.TestUtility;

import io.restassured.response.Response;

// Attach a custom TestNG listener to log test execution details
@Listeners(com.api.listeners.TestListener.class)
public class ForgotPasswordTest {

	// Test to verify if the forgot password API functions correctly
	@Test(description = "Verify if forgot password API is working correctly")
	public void forgotPassword() {
		// Expected message from the API response when the email exists or not
		final String expectedMessage = "If your email exists in our system, you will receive reset instructions.";

		// Generate test user data using Faker utility
		SignUpRequest signUpRequest = TestUtility.generateSignUpData();

		// Initialize the AuthService to access authentication-related APIs
		AuthService authService = new AuthService();

		// Invoke the forgot password API using the generated email
		Response response = authService.forgotPassword(signUpRequest.getEmail());

		// Assert that the response status code is 200 (Success)
		Assert.assertEquals(response.statusCode(), 200, "Unexpected status code");

		// Extract and assert the actual message from the response body
		String actualMessage = response.jsonPath().getString("message");
		Assert.assertEquals(actualMessage, expectedMessage, "Forgot password response message mismatch!");
	}
}
