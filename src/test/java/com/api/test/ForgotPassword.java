package com.api.test;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.api.base.AuthService;

import io.restassured.response.Response;

@Listeners(com.api.listeners.TestListener.class)
public class ForgotPassword {

	@Test(description = "Verify if forgot passowrd api is working fine")
	public void forgotPassword() {

		AuthService authService = new AuthService();
		Response response = authService.forgotPassword("adilkhn814@gmail.com");
		System.out.println(response.asPrettyString());
		// Assert.assertEquals(response.sessionId(), 200);

	}

}
