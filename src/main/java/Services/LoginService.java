package Services;

import java.util.Map;

import org.json.simple.JSONObject;

import Base.BaseService;
import Constants.APIEndpoints;
import io.restassured.response.Response;

public class LoginService extends BaseService {
	
	GenerateTokenService generateTokenService = new GenerateTokenService();
	Response res;

	@SuppressWarnings("unchecked")
	public Response login(String emailID, String password) {
          JSONObject loginpayload = new JSONObject();
          loginpayload.put("email_id", emailID);
          loginpayload.put("password", password);
          return login(loginpayload);
          
	}

	public Response login(JSONObject emailLoginPayload) {
		generateTokenService.getUserId(emailLoginPayload.get("email_id").toString(), emailLoginPayload.get("password").toString());
		Map<String, String> headerMap = getHeaderHavingAuth(generateTokenService.getToken());
		return executePostAPI(APIEndpoints.LOGIN, headerMap, emailLoginPayload);
	}
	
	public int loginAndGetUserId(JSONObject loginPayload) {
		res = login(loginPayload);
		return res.jsonPath().getInt("content.userId");
	}


}
