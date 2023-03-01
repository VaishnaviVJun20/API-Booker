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

public class FMC_Test {

	String token;
	String Otp;
	Response res;
	BaseService baseservice = new BaseService();

	private void createToken() {
		Map<String, String> headermap = baseservice.getHeaderWithoutAuth();
		res = baseservice.executeGetAPI("/fmc/token", headermap);

		token = res.jsonPath().get("accessToken");
		System.out.println(token);
	}

	@SuppressWarnings("unchecked")
	private String emailSignup(String emailId) {

		JSONObject jsonobject = new JSONObject();
		jsonobject.put("email_id", emailId);

		Map<String, String> headermap = baseservice.getHeaderHavingAuth(token);
		res = baseservice.executePostAPI("/fmc/email-signup-automation", headermap, jsonobject);
		
	//	res = given().headers("Content-Type", "application/json").headers("Authorization", "Bearer " + token)
	//			.body(jsonobject).when().post("/fmc/email-signup-automation");

		String otp = res.jsonPath().getString("content.otp");
		return otp;
	}
	
	@SuppressWarnings("unchecked")
	private void verifyOtp(String emailId, String fullName, String phoneNumber,String  password, String otp) {
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id",emailId);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		verifyOtpPayload.put("otp", otp);
		
		Map<String, String> headermap = baseservice.getHeaderHavingAuth(token);
		res = baseservice.executePutAPI("/fmc/verify-otp", headermap, verifyOtpPayload);
		
	//	res = given().headers("Content-Type", "application/json").headers("Authorization", "Bearer " + token)
	//			.body(verifyOtpPayload).when().put("/fmc/verify-otp");
		
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
	}

	@Test
	public void signupTest() {
		createToken();
		String emailID = DataGenerator.getemailID();
		String fullname = DataGenerator.getfullname();
		String phoneNumber = DataGenerator.getphoneNumber(10);
		String password = "123@34kl";
		Otp = emailSignup(emailID);		
		verifyOtp(emailID, fullname, phoneNumber, password, Otp);
		
	}

}
