package testscripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Constants.StatusCode;
import Services.LoginService;
import Services.ReportService;
import io.restassured.response.Response;
import pojo.request.addreport.AddReport;
import utilities.DataGenerator;

public class FMCReports {
	
	LoginService loginService = new LoginService();
	String emailId = DataGenerator.getemailID();
	String password = "vaish12390";
	int userId;
	ReportService reportService = new ReportService();
	AddReport addReportPayload;
	Response res;
	int reportId;
	
	
	@BeforeClass
	public void login() {
		res = loginService.login(emailId, password);
		userId = res.jsonPath().getInt("content.userId");
	}

	@Test
	public void verfiyAddReportTest() {
		addReportPayload = reportService.addReportRequestPayload(userId);

		res = reportService.getAddReportResponse(userId, addReportPayload);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		Assert.assertEquals(res.jsonPath().getString("status"), "Success");
		Assert.assertEquals(res.jsonPath().getString("message"), "Report created successfully");
		reportId = res.jsonPath().getInt("content");
		Assert.assertTrue(reportId > 0);
		System.out.println(reportId);
	}
	
	@Test(priority = 1)
	public void verifyGetReportTest() {
		res = reportService.getReportResponse(userId);
		System.out.println("get: " +res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		Assert.assertEquals(res.jsonPath().getInt("content[0].reporter_details.report_id"), reportId);
		validateResponse(res, addReportPayload);
	}

	@Test(priority = 2)
	public void verifyDeleteReportTest() {
		res = reportService.getDeleteReportResponse(reportId, userId);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
	}
	
	
	//Adding few validations on payload
	private void validateResponse(Response res, AddReport addreportPayload) {
		
		Assert.assertEquals(res.jsonPath().getInt("content[0].reporter_details.report_id"), reportId);
		Assert.assertEquals(res.jsonPath().getString("content[0].reporter_details.reporter_fullname"),
				addreportPayload.getReporter_details().getReporter_fullname());
	
		Assert.assertEquals(res.jsonPath().getInt("content[0].reporter_details.user_id"), userId);
		Assert.assertEquals(res.jsonPath().getString("content[0].child_details.fullname"),
				addreportPayload.getChild_details().getFullname());
		
		Assert.assertEquals(res.jsonPath().getString("content[0].incident_details.incident_date"),
				addreportPayload.getIncident_details().getIncident_date());		

	}

}
