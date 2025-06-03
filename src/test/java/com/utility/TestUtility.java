package com.utility;

import com.api.models.request.UpdateProfileRequest;
import com.api.models.request.SignUpRequest;
import com.github.javafaker.Faker;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class TestUtility {

	public static class ConfigUtil {
		private static Properties properties = new Properties();

		static {
			try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
				properties.load(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static String getUsername() {
			return properties.getProperty("username");
		}

		public static String getPassword() {
			return properties.getProperty("password");
		}
	}

	public static class Constants {
		public static final int SUCCESS_STATUS_CODE = 200;
		public static final String TOKEN_TYPE_BEARER = "Bearer";
		public static final String ROLE_USER = "ROLE_USER";
	}

	private static final Faker faker = new Faker();

	public static SignUpRequest generateSignUpData() {

		return new SignUpRequest.Builder().email(faker.internet().emailAddress()).firstName(faker.name().firstName())
				.lastName(faker.name().lastName()).username(faker.name().username() + faker.number().digits(3))
				.password("Test@123").mobileNumber(faker.phoneNumber().subscriberNumber(10)).build();

	}

	public static UpdateProfileRequest generateUserData() {

		return new UpdateProfileRequest.Builder().email(faker.internet().emailAddress())
				.firstName(faker.name().firstName()).lastName(faker.name().lastName())
				.mobileNumber(faker.phoneNumber().subscriberNumber(10)).build();

	}

	public static UpdateProfileRequest getUpdateProfileData() {
		return new UpdateProfileRequest.Builder().email("adilkhn814@gmail.com").firstName("Adil").lastName("Khan")
				.mobileNumber("8767157670").build();
	}

	public static Iterator<String[]> readCSV(String fileName) {

		File csvFile = new File(System.getProperty("user.dir") + "/testData/" + fileName);
		FileReader fileReader = null;
		CSVReader csvReader;
		List<String[]> dataList = null;

		try {
			fileReader = new FileReader(csvFile);
			csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1) // <-- This line skips the first line (header)
					.build();
			dataList = csvReader.readAll();
		}

		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Iterator<String[]> dataIterator = dataList.iterator();
		return dataIterator;

	}

	// *Token Generation Utility*//
//	public static String getToken(String role) {
//
//		LoginRequestPOJO loginRequestPOJO = null;
//
//		if (role.equalsIgnoreCase("user")) {
//			loginRequestPOJO = new LoginRequestPOJO("8767157670", "chrome");
//		} else if (role.equalsIgnoreCase("admin")) {
//			loginRequestPOJO = new LoginRequestPOJO("7397864117", "chrome");
//		} else if (role.equalsIgnoreCase("doctor")) {
//			loginRequestPOJO = new LoginRequestPOJO("8552855141", "chrome");
//		}
//
//		String token = given().header(new Header("content-type", "application/json"))
//				.body(TestUtility.getJsonData(TestUtility.LoginRequestPOJO())).when().post("api/login-mobile").then()
//				.extract().jsonPath().getString("message");
//
//		return token;
//	}

}
