package testscripts;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import Base.BaseService;
import Constants.StatusCode;
import Services.GenerateTokenService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.DataGenerator;

import static io.restassured.RestAssured.given;

import java.util.Map;

public class FMC_Test3 {

	String emailId = DataGenerator.getemailID();
	String fullName = DataGenerator.getfullname();
	String phoneNumber = DataGenerator.getphoneNumber(10);
	String password = "pass@123";
	String otp;
	String token;
	int userId;
	Response res;
	BaseService baseService = new BaseService();
	GenerateTokenService generateTokenService = new GenerateTokenService();

	@Test()
	public void createToken() {
		res = generateTokenService.getTokenResponse();
		token = generateTokenService.getToken();
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		System.out.println(token);
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 1)
	public void emailSignUp() {

		JSONObject emailSignUpPayload = new JSONObject();
		emailSignUpPayload.put("email_id", emailId);

		res = generateTokenService.getEmailSignupResponse(emailSignUpPayload);
		Assert.assertEquals(res.getStatusCode(), StatusCode.CREATED);
		otp = generateTokenService.getOtpFromEmailSignupResponse(emailSignUpPayload);
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 2)
	public void verifyOtp() {
		
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id", emailId);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		otp = generateTokenService.getOtpFromEmailSignupResponse(verifyOtpPayload);
		verifyOtpPayload.put("otp", otp);

		res = generateTokenService.getVerifyOtpResponse(verifyOtpPayload);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
	    userId = generateTokenService.getUserIdFromVerifyOtpResponse(verifyOtpPayload);
	    System.out.println(userId);
	    
	}
	
	@Test(priority = 3)
	public void verifyOtp1() {

	    userId = generateTokenService.getUserId(emailId, password);
	    System.out.println(userId);
	    
	}

}
