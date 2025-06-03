package com.api.test;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.base.UserProfileManagementService;
import com.api.models.request.LoginRequest;
import com.api.models.response.LoginResponse;
import com.utility.TestUtility.ConfigUtil;
import com.api.models.response.GetProfileResponse;
import com.dataprovider.GetProfileDataprovider;


import io.restassured.response.Response;

// Attach TestListener to this test class to listen for test events (start, success, failure, etc.)
@Listeners(com.api.listeners.TestListener.class)
public class GetProfileTest {

	/**
	 * Test method to verify if the 'Get Profile' API works correctly. Uses data
	 * from 'User Data' data provider in the GetProfileDataprovider class.
	 * 
	 * @param username     Username to login and verify profile info
	 * @param password     Password for login
	 * @param email        Expected email in profile response
	 * @param firstName    Expected first name in profile response
	 * @param lastName     Expected last name in profile response
	 * @param mobileNumber Expected mobile number in profile response
	 */
	@Test(description = "Verify if get profile api is working fine", dataProviderClass = com.dataprovider.GetProfileDataprovider.class, dataProvider = "User Data")
	public void getProfileInfoTest(String email, String firstName, String lastName,
			String mobileNumber) {
		
		String username = ConfigUtil.getUsername();
		String password = ConfigUtil.getPassword();

		// Build the login request object with username and password using Builder
		// pattern
		LoginRequest loginRequest = new LoginRequest.Builder().username(username).password(password).build();

		// Create instance of AuthService to call login API
		AuthService authService = new AuthService();

		// Call login API and get the response
		Response response = authService.login(loginRequest);

		// Deserialize the login response JSON into LoginResponse POJO
		LoginResponse loginResponse = response.as(LoginResponse.class);

		// Use the token from login response to call get profile API
		UserProfileManagementService userProfileManagementService = new UserProfileManagementService();
		response = userProfileManagementService.getProfile(loginResponse.getToken());

		// Assert that the response status code is 200 OK
		Assert.assertEquals(response.statusCode(), 200, "Expected status code is 200");

		// Deserialize the get profile response JSON into GetProfileResponse POJO
		GetProfileResponse getProfileResponse = response.as(GetProfileResponse.class);

		// Assert the profile response fields against expected data passed from data
		// provider
		Assert.assertEquals(getProfileResponse.getUsername(), username, "Username mismatch");
		Assert.assertEquals(getProfileResponse.getEmail(), email, "Email mismatch");
		Assert.assertEquals(getProfileResponse.getFirstName(), firstName, "First name mismatch");
		Assert.assertEquals(getProfileResponse.getLastName(), lastName, "Last name mismatch");
		Assert.assertEquals(getProfileResponse.getMobileNumber(), mobileNumber, "Mobile number mismatch");

	}

}
