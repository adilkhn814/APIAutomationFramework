package com.api.test;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;
import com.api.base.UserProfileManagementService;
import com.api.models.request.LoginRequest;
import com.api.models.request.UpdateProfileRequest;
import com.api.models.response.GetProfileResponse;
import com.api.models.response.LoginResponse;
import com.utility.TestUtility;
import com.utility.TestUtility.ConfigUtil;
import com.dataprovider.LoginDataprovider;

import io.restassured.response.Response;

// Attach TestListener to listen to test execution events (start, success, failure, etc.)
@Listeners(com.api.listeners.TestListener.class)
public class UpdateProfileTest {

	/**
	 * Test to verify that the 'Update Profile' API works as expected.
	 * - Logs in using valid credentials.
	 * - Updates the user's profile with new data (generated using Faker).
	 * - Verifies the updated fields match the request payload.
	 *
	 * @param email (provided by data provider but not used in logic directly)
	 */
	@Test(description = "Verify if update profile API is working correctly",
	      dataProviderClass = com.dataprovider.LoginDataprovider.class,
	      dataProvider = "Login Data")
	public void updateProfileTest(String email) {

		// Fetch username and password from config file
		String username = ConfigUtil.getUsername();
		String password = ConfigUtil.getPassword();

		// Build login request using Builder pattern
		LoginRequest loginRequest = new LoginRequest.Builder()
				.username(username)
				.password(password)
				.build();

		// Perform login and extract token
		AuthService authService = new AuthService();
		Response response = authService.login(loginRequest);
		LoginResponse loginResponse = response.as(LoginResponse.class);

		// Generate fake user data for profile update
		UserProfileManagementService userProfileManagementService = new UserProfileManagementService();
		UpdateProfileRequest updateProfileRequest = TestUtility.generateUserData();

		// Call update profile API with new data and get response
		response = userProfileManagementService.updateProfile(updateProfileRequest, loginResponse.getToken());
		GetProfileResponse getProfileResponse = response.as(GetProfileResponse.class);

		// Assertions to verify if updated data is reflected correctly
		Assert.assertEquals(getProfileResponse.getUsername(), loginResponse.getUsername(), "Username mismatch");
		Assert.assertEquals(getProfileResponse.getEmail(), updateProfileRequest.getEmail(), "Email mismatch");
		Assert.assertEquals(getProfileResponse.getFirstName(), updateProfileRequest.getFirstName(), "First name mismatch");
		Assert.assertEquals(getProfileResponse.getLastName(), updateProfileRequest.getLastName(), "Last name mismatch");
		Assert.assertEquals(getProfileResponse.getMobileNumber(), updateProfileRequest.getMobileNumber(), "Mobile number mismatch");
	}

	/**
	 * AfterMethod to revert any changes made to the user profile during test execution.
	 * Ensures test isolation by restoring original profile data after each test run.
	 */
	@AfterMethod
	public void revertUserProfile() {
		String username = ConfigUtil.getUsername();
		String password = ConfigUtil.getPassword();

		// Re-login to get fresh token
		LoginRequest loginRequest = new LoginRequest.Builder()
				.username(username)
				.password(password)
				.build();

		AuthService authService = new AuthService();
		Response response = authService.login(loginRequest);
		LoginResponse loginResponse = response.as(LoginResponse.class);

		// Restore original profile data from static utility method
		UserProfileManagementService userProfileManagementService = new UserProfileManagementService();
		UpdateProfileRequest updateProfileRequest = TestUtility.getUpdateProfileData();
		userProfileManagementService.updateProfile(updateProfileRequest, loginResponse.getToken());
	}
}
