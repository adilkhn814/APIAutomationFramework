package com.api.test;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.models.request.LoginRequest;
import com.api.models.response.LoginResponse;
import com.utility.TestUtility.ConfigUtil;
import com.utility.TestUtility.Constants;
import com.dataprovider.LoginDataprovider;


import io.restassured.response.Response;

@Listeners(com.api.listeners.TestListener.class)
public class LoginTest {

	/**
	 * This test verifies the login functionality of the application. It takes test
	 * data (username, password, email) from a data provider class. Performs login
	 * and asserts the response for correctness.
	 */
	@Test(description = "Verify if login API is working", dataProviderClass = com.dataprovider.LoginDataprovider.class, dataProvider = "Login Data")
	public void loginTest(String email) {

		// Fetch login credentials from config.properties
		String username = ConfigUtil.getUsername();
		String password = ConfigUtil.getPassword();

		// Prepare login request using provided test data (from CSV or DataProvider)
		LoginRequest loginRequest = new LoginRequest.Builder().username(username).password(password).build();

		AuthService authService = new AuthService();

		// Send login request and capture response
		Response response = authService.login(loginRequest);

		// Assert that response status is 200 OK
		Assert.assertEquals(response.statusCode(), Constants.SUCCESS_STATUS_CODE, "Login failed!");

		// Deserialize response into LoginResponse POJO
		LoginResponse loginResponse = response.as(LoginResponse.class);

		// --- Positive Test Case Assertions ---
		Assert.assertEquals(loginResponse.getUsername(), username, "Username mismatch!");
		Assert.assertEquals(loginResponse.getEmail(), email, "Email mismatch!");

		// --- Negative/Validation Test Case Assertions ---
		Assert.assertNotNull(loginResponse.getToken(), "Token is null!");
		Assert.assertFalse(loginResponse.getToken().isEmpty(), "Token is empty!");
		Assert.assertEquals(loginResponse.getType(), Constants.TOKEN_TYPE_BEARER, "Unexpected token type!");
		Assert.assertTrue(loginResponse.getId() > 0, "Invalid user ID!");
		Assert.assertTrue(loginResponse.getRoles().contains(Constants.ROLE_USER), "User role missing!");
	}
}
