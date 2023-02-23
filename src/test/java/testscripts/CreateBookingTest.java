package testscripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Constants.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.request.createbooking.Bookingdates;
import pojo.request.createbooking.CreateBookingRequest;

public class CreateBookingTest {

	String token;
	int bookindId;
	CreateBookingRequest payload = new CreateBookingRequest();

	@BeforeMethod
	public void generateToken() {

		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.body("{\n" + "    \"username\" : \"admin\",\n" + "    \"password\" : \"password123\"\n" + "}").when()
				.post("/auth").then().assertThat().statusCode(200).log().all().extract().response();

		token = res.jsonPath().getString("token");

	}

	@Test(enabled = false)
	public void createBookingTest() {

		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json")
				.body("{\n" + "    \"firstname\" : \"Vaishnavi\",\n" + "    \"lastname\" : \"Brown\",\n"
						+ "    \"totalprice\" : 111,\n" + "    \"depositpaid\" : true,\n" + "    \"bookingdates\" : {\n"
						+ "        \"checkin\" : \"2018-01-01\",\n" + "        \"checkout\" : \"2019-01-01\"\n"
						+ "    },\n" + "    \"additionalneeds\" : \"Breakfast\"\n" + "}")
				.when().post("/booking").then().assertThat().statusCode(200).log().all().extract().response();

		// System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
		// System.out.println(res.asPrettyString());
	}

	@Test
	public void createBookingTestWithPOJO() {

		// RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		Bookingdates bookingdates = new Bookingdates();
		bookingdates.setCheckin("2018-01-01");
		bookingdates.setCheckout("2018-01-02");

		// CreateBookingRequest payload = new CreateBookingRequest();
		payload.setFirstname("Vaishnavi");
		payload.setLastname("vaidya");
		payload.setTotalprice(123);
		payload.setDepositpaid(true);
		payload.setAdditionalneeds("breakfast");
		payload.setBookingdates(bookingdates);

		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").body(payload).when().post("/booking").then().assertThat()
				.statusCode(200).log().all().extract().response();

		bookindId = res.jsonPath().getInt("bookingid");
		Assert.assertTrue(bookindId > 0);
		// System.out.println(res.statusCode());
		validateResponse(res, payload, "booking.");
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
		// System.out.println(res.asPrettyString());
	}

	@Test(priority = 1)
	public void getAllBookindTest() {
		Response res = RestAssured.given().headers("Accept", "application/json").when().get("/booking");

		List<Integer> listofBookidIds = res.jsonPath().getList("bookingid");
		Assert.assertTrue(listofBookidIds.contains(bookindId));
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
		// System.out.println(res.asPrettyString());
	}

	@Test(priority = 2)
	public void getBookindIdTest() {
		Response res = RestAssured.given().headers("Accept", "application/json").when().get("/booking/" + bookindId);
		validateResponse(res, payload, "");
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
	}

	@Test(priority = 3)
	public void getBookindIdDeserealisedTest() {
		Response res = RestAssured.given().headers("Accept", "application/json").when().get("/booking/" + bookindId);

		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		Assert.assertEquals(payload.firstname, responseBody.firstname);
		Assert.assertTrue(payload.equals(responseBody));
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
	}

	@Test(priority = 4)
	public void updateBookindIdDeserealisedTest() {
		payload.setFirstname("Kajol");
		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").headers("Cookie", "token=" + token)
				.body(payload).when().put("/booking/" + bookindId);

		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		Assert.assertEquals(payload.firstname, responseBody.firstname);
		Assert.assertTrue(payload.equals(responseBody));
		Assert.assertEquals(res.statusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
	}

	@Test(priority = 5)
	public void updatePartialBookingIdTest() {
		payload.setFirstname("Vaishnavi");
		payload.setLastname("Vaidya");
		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").headers("Cookie", "token=" + token)
				.pathParam("bookingId", bookindId).body(payload).when().patch("/booking/{bookingId}");
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		Assert.assertTrue(responseBody.equals(payload));
		System.out.println(res.jsonPath());
	}

	@Test(priority = 6)
	public void DeleteBookingTest() {
		Response res = RestAssured.given().headers("Cookie", "token=" + token).when()
				.delete("/booking/" + bookindId);

		Assert.assertEquals(res.statusCode(), StatusCode.CREATED);
		verifyDeletedBookingId();
	}

	private void validateResponse(Response res, CreateBookingRequest payload, String object) {
		Assert.assertEquals(res.jsonPath().getString(object + "firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString(object + "lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt(object + "totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean(object + "depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().getString(object + "additionalneeds"), payload.getAdditionalneeds());
		Assert.assertEquals(res.jsonPath().getString(object + "bookingdates.checkin"),
				payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString(object + "bookingdates.checkout"),
				payload.getBookingdates().getCheckout());
	}
	
	public void verifyDeletedBookingId() {
		Response res = RestAssured.given().headers("Accept", "application/json").when().get("/booking");
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		List<Integer> listOfBookingId = res.jsonPath().getList("bookingid");
		Assert.assertFalse(listOfBookingId.contains(bookindId));
	}

}
