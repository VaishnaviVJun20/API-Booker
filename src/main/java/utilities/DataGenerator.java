package utilities;

import net.datafaker.Faker;

public class DataGenerator {

	private static Faker faker = new Faker();

	public static String getemailID() {
		String emailId = faker.name().firstName() + "@gmail.com";
		return emailId;
	}

	public static String getfullname() {
		return faker.name().fullName();
	}

	public static String getphoneNumber(int digits) {
		return faker.number().digits(digits);

	}

	public static String getNumber(int count) {
		return faker.number().digits(count);
	}

	public static String getDate() {
		return faker.date().birthday("yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	public static String getGender() {
		return faker.gender().binaryTypes();
	}

	public static String getRelationship() {
		return faker.relationships().any();
	}

	public static String getContactAddressType() {
		return faker.house().room();
	}

	public static String getContactAddressLine1() {
		return faker.address().city();
	}

	public static String getContactAddressLine2() {
		return faker.address().citySuffix();
	}

	public static String getPincode() {
		return faker.address().postcode();
	}

	public static String getCountry() {
		return faker.address().country();
	}

	public static String getPrimaryCountryCode() {
		return faker.address().countryCode();
	}

	public static String getLanguage() {
		return faker.programmingLanguage().name();
	}

	public static String getRandonText() {
		return faker.text().text(7);
	}

	public static boolean getFlag() {
		return faker.random().nextBoolean();
	}
	
	public static String getNickName() {
		return faker.name().firstName();
	}

}
