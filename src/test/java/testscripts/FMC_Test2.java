package testscripts;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import Base.BaseService;
import Constants.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.DataGenerator;

import static io.restassured.RestAssured.given;

import java.util.Map;

public class FMC_Test2 {

	String emailId = DataGenerator.getemailID();
	String fullName = DataGenerator.getfullname();
	String phoneNumber = DataGenerator.getphoneNumber(10);
	String password = "pass@123";
	String otp;
	String token;
	Response res;
	BaseService baseService = new BaseService();

	@Test()
	public void createToken() {
		Map<String, String> headerMap = baseService.getHeaderWithoutAuth();
		res = baseService.executeGetAPI("/fmc/token", headerMap);
		token = res.jsonPath().get("accessToken");
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		System.out.println(token);
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 1)
	public void emailSignUp() {

		JSONObject emailSignUpPayload = new JSONObject();
		emailSignUpPayload.put("email_id", emailId);

		Map<String, String> headerMap = baseService.getHeaderHavingAuth(token);
		res = baseService.executePostAPI("/fmc/email-signup-automation", headerMap, emailSignUpPayload);
		Assert.assertEquals(res.getStatusCode(), StatusCode.CREATED);
		otp = res.jsonPath().getString("content.otp");
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 2)
	public void verifyOtp() {
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id", emailId);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		verifyOtpPayload.put("otp", otp);

		Map<String, String> headerMap = baseService.getHeaderHavingAuth(token);
		res = baseService.executePutAPI("/fmc/verify-otp/", headerMap, verifyOtpPayload);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
	}

}
