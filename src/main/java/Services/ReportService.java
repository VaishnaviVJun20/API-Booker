package Services;

import java.util.Map;

import Base.BaseService;
import Constants.APIEndpoints;
import io.restassured.response.Response;
import pojo.request.addreport.AddReport;
import pojo.request.addreport.ChildDetails;
import pojo.request.addreport.IncidentDetails;
import pojo.request.addreport.ReporterDetails;
import utilities.DataGenerator;

public class ReportService extends BaseService{
	
	AddReport addReport = new AddReport();
	
	Response res;
	String requestId = DataGenerator.getNumber(6);
	String reportDate = DataGenerator.getDate();
	String reporterFullname = DataGenerator.getfullname();
	String reporterAge = DataGenerator.getNumber(2);
	String reporterGender = DataGenerator.getGender();
	String reporterRelation = DataGenerator.getRelationship();
	String parentingType = DataGenerator.getRelationship();
	String contactAddress_type = DataGenerator.getContactAddressType();
	String contactAddressline1 = DataGenerator.getContactAddressLine1();
	String contactAddressline2 = DataGenerator.getContactAddressLine2();
	String pinCode = DataGenerator.getPincode();
	String country = DataGenerator.getCountry();
	String primaryCountryCode = DataGenerator.getPrimaryCountryCode();
	String primaryContactNumber = DataGenerator.getphoneNumber(10);
	String secondaryCountryCode = DataGenerator.getPrimaryCountryCode();
	String secondaryContactNumber = DataGenerator.getphoneNumber(10);
	String communicationLanguage = DataGenerator.getLanguage();
	

	String childFullName = DataGenerator.getfullname();
	String childAge = DataGenerator.getNumber(2);
	String childGender = DataGenerator.getGender();
	String childNickname = DataGenerator.getNickName();
	String randomtext = DataGenerator.getRandonText();

	String incidentDate = DataGenerator.getDate();
	String incidentLocation = DataGenerator.getContactAddressLine1();
	boolean flag = DataGenerator.getFlag();

	GenerateTokenService generateTokenService = new GenerateTokenService();

	public AddReport addReportRequestPayload(int userId) {
		
		ReporterDetails reporterDetails = new ReporterDetails();
		ChildDetails childDetails = new ChildDetails();
		IncidentDetails incidentDetails = new IncidentDetails();
		
		reporterDetails.setRequest_id(requestId);
		reporterDetails.setUser_id(userId);
		reporterDetails.setReport_date(reportDate);
		reporterDetails.setReporter_fullname(reporterFullname);
		reporterDetails.setReporter_age(Integer.parseInt(reporterAge));
		reporterDetails.setReporter_gender(reporterGender);
		reporterDetails.setReporter_relation(reporterRelation);
		reporterDetails.setParenting_type(parentingType);
		reporterDetails.setContact_address_type(contactAddress_type);
		reporterDetails.setContact_address_line_1(contactAddressline1);
		reporterDetails.setContact_address_line_2(contactAddressline2);
		reporterDetails.setPincode(pinCode);
		reporterDetails.setCountry(country);
		reporterDetails.setPrimary_country_code(primaryCountryCode);
		reporterDetails.setPrimary_contact_number(primaryContactNumber);
		reporterDetails.setSecondary_country_code(secondaryCountryCode);
		reporterDetails.setSecondary_contact_number(secondaryContactNumber);
		reporterDetails.setCommunication_language(communicationLanguage);
		reporterDetails.setStatus("INCOMPLETE");

		childDetails.setFullname(childFullName);
		childDetails.setAge(Integer.parseInt(childAge));
		childDetails.setGender(childGender);
		childDetails.setHeight("5ft");
		childDetails.setWeight("54kg");
		childDetails.setComplexion(randomtext);
		childDetails.setClothing(randomtext);
		childDetails.setBirth_signs(randomtext);
		childDetails.setOther_details(randomtext);
		childDetails.setImage_file_key(null);
		childDetails.setNickname(childNickname);

		incidentDetails.setIncident_date(incidentDate);
		incidentDetails.setIncident_brief(randomtext);
		incidentDetails.setLocation(incidentLocation);
		incidentDetails.setLandmark_signs(randomtext);
		incidentDetails.setNearby_police_station(randomtext);
		incidentDetails.setNearby_NGO(randomtext);
		incidentDetails.setAllow_connect_police_NGO(flag);
		incidentDetails.setSelf_verification(flag);
		incidentDetails.setCommunity_terms(flag);

		addReport.setReporter_details(reporterDetails);
		addReport.setIncident_details(incidentDetails);
		addReport.setChild_details(childDetails);
		
		return addReport;
	}

	public Response getAddReportResponse(int userId, AddReport addReportpayload) {
		Map<String,String> headerMap = generateTokenService.getHeaderHavingAuth(generateTokenService.getToken());
		return executePostAPI(APIEndpoints.REPORT, headerMap, addReportpayload);
	}

	public Response getReportResponse(int userId) {
		Map<String,String> headerMap = generateTokenService.getHeaderHavingAuth(generateTokenService.getToken());
		return executeGetAPI(APIEndpoints.REPORT + "/" + String.valueOf(userId), headerMap);
	}

	public Response getDeleteReportResponse(int content, int userId) {
		Map<String,String> headerMap = generateTokenService.getHeaderHavingAuth(generateTokenService.getToken());
		return executeDeleteAPI(APIEndpoints.REPORT + "/" + String.valueOf(content) + "/users/" + userId, headerMap);
	}

}
