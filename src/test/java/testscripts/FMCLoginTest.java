package testscripts;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Constants.StatusCode;
import io.restassured.response.Response;
import utilities.DataGenerator;
import Services.LoginService;

public class FMCLoginTest {
	
	LoginService loginService = new LoginService();
	String emailId = DataGenerator.getemailID();
	String password = "vaish12390";
	
	@Test
	public void loginTest() {
		Response res = loginService.login(emailId, password);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
	}
	
	//@Test(dataProvider = "loginDataDetails")
	public void loginAPITestDataDriven(String emailId, String password, String result) {
		Response res = loginService.login(emailId, password);
		if(result.equals("Success"))
			Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		else {
			Assert.assertEquals(res.getStatusCode(), StatusCode.BADREQUEST);
		}
		System.out.println(res.asPrettyString());
	}

	@DataProvider(name = "loginDataDetails")
	public String[][] getLoginData(){
		String[][] data = new String[2][3];

		data[0][0] = emailId;
		data[0][1] = password;
		data[0][2] = "Success";

		data[1][0] = "VV@gmail.com";
		data[1][1] = "";
		data[1][2] = "fail";

		return data; 
	}

}
