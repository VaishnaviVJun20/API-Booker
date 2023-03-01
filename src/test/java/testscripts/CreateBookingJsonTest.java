package testscripts;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Constants.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.datafaker.Faker;
import pojo.request.createbooking.Bookingdates;
import pojo.request.createbooking.CreateBookingRequest;

public class CreateBookingJsonTest {
	
	String token;
	int bookindId;

	@BeforeMethod
	public void generateToken() {

		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.body("{\n" + "    \"username\" : \"admin\",\n" + "    \"password\" : \"password123\"\n" + "}").when()
				.post("/auth").then().assertThat().statusCode(200).log().all().extract().response();

		token = res.jsonPath().getString("token");

	}


	@SuppressWarnings("unchecked")
	@Test
	public void createBookingTestWithJSONObject() {
		
		Faker faker = new Faker();

		JSONObject jsonobjectbooking = new JSONObject();
		jsonobjectbooking.put("checkin", "2018-01-01");
		jsonobjectbooking.put("checkout", "2019-01-01");

		JSONObject jsonobject = new JSONObject();
		jsonobject.put("firstname", faker.name().firstName());
		jsonobject.put("lastname", faker.name().lastName());
		jsonobject.put("totalprice", faker.number().positive());
		jsonobject.put("depositpaid", faker.bool().bool());
		jsonobject.put("bookingdates", jsonobjectbooking);
		jsonobject.put("additionalneeds", "Breakfast");
		

		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").body(jsonobject).when().post("/booking").then().assertThat()
				.statusCode(200).log().all().extract().response();

		bookindId = res.jsonPath().getInt("bookingid");
		Assert.assertTrue(bookindId > 0);
		// System.out.println(res.statusCode());
		//validateResponse(res, payload, "booking.");
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
	    System.out.println(res.asPrettyString());
	}
	
	

}
