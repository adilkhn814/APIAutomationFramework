//package com.api.test;
//
//import org.testng.Assert;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.api.base.AuthService;
//import com.api.models.request.LoginRequest;
//import com.api.models.request.ResetPasswordRequest;
//import com.api.models.response.LoginResponse;
//
//import io.restassured.response.Response;
//
//@Listeners(com.api.listeners.TestListener.class)
//public class ResetPasswordTest {
//
//	@Test(description = "Verify if Reset API is working fine")
//	public void resetPasswordTest() {
//
//		LoginRequest loginRequest = new LoginRequest.Builder().username("adilkhan814").password("Adil@123").build();
//		AuthService authService = new AuthService();
//		Response response = authService.login(loginRequest);
//		Assert.assertEquals(response.getStatusCode(), 200);
//		LoginResponse loginResponse = response.as(LoginResponse.class);
//		
//		
//		
//
//		ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(loginResponse.getToken(), "Password@33",
//				"Password@33");
//
//		response = authService.resetPassword(resetPasswordRequest, loginResponse.getToken());
//		System.out.println("---------------------");
//		System.out.println(loginResponse.getToken());
//
//	}
//
//}
