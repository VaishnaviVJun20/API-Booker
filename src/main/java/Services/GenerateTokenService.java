package Services;

import java.util.Map;

import org.json.simple.JSONObject;

import Base.BaseService;
import Constants.APIEndpoints;
import io.restassured.response.Response;
import utilities.DataGenerator;

public class GenerateTokenService extends BaseService {

	String emailId = DataGenerator.getemailID();

	public Response getTokenResponse() {
		Map<String, String> headerMap = getHeaderWithoutAuth();
		Response res = executeGetAPI(APIEndpoints.TOKEN, headerMap);
		return res;
	}

	public String getToken() {
		Response res = getTokenResponse();
		return res.jsonPath().get("accessToken");
	}

	public Response getEmailSignupResponse(JSONObject emailSignUpPayload) {
		Map<String, String> headerMap = getHeaderHavingAuth(getToken());
		return executePostAPI(APIEndpoints.EMAIL_SIGNUP, headerMap, emailSignUpPayload);
	}

	@SuppressWarnings("unchecked")
	public Response getEmailSignupResponse() {
		JSONObject emailSignUpPayload = new JSONObject();
		emailSignUpPayload.put("email_id", emailId);

		Map<String, String> headerMap = getHeaderHavingAuth(getToken());
		return executePostAPI(APIEndpoints.EMAIL_SIGNUP, headerMap, emailSignUpPayload);
	}

	public String getOtpFromEmailSignupResponse(JSONObject emailSignUpPayload) {
		Response res = getEmailSignupResponse(emailSignUpPayload);
		String otp = res.jsonPath().getString("content.otp");
		return otp;
	}

	public Response getVerifyOtpResponse(JSONObject verifyOtpPayload) {
		Map<String, String> headerMap = getHeaderHavingAuth(getToken());
		return executePutAPI(APIEndpoints.VERIFY_OTP, headerMap, verifyOtpPayload);
	}

	public int getUserIdFromVerifyOtpResponse(JSONObject verifyOtpPayload) {
		Response res = getVerifyOtpResponse(verifyOtpPayload);
		int userId = res.jsonPath().getInt("content.userId");
		return userId;
	}

	@SuppressWarnings("unchecked")
	public int getUserId(String emailId, String password) {
		
		JSONObject emailSignUpPayLoad = new JSONObject();
		emailSignUpPayLoad.put("email_id", emailId);
		String otp = getOtpFromEmailSignupResponse(emailSignUpPayLoad);
		
		String fullName = DataGenerator.getfullname();
		String phoneNumber = DataGenerator.getphoneNumber(10);
		
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id", emailId);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		verifyOtpPayload.put("otp", otp);
		
		return getUserIdFromVerifyOtpResponse(verifyOtpPayload);
	}
}
