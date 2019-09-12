package TwitterAPIExample;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static org.hamcrest.Matchers.equalTo;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TwitterEndtoEndWorkflow {
	
	String consumerKey = "5XCX9eXGRdffAWB2PabEQ3N2o";
	String consumerSecret = "DM6rQchWBuNw6dkAAOI0ZM10Ll4XS4c0uPFcJwai67k41xtL7x";
	String accessToken = "1020375614652583936-96lA0A6ltQtBRMXyJbBjqPgsyI3DFU";
	String secretToken = "sDmxTinqP6OwDkfVEtLjEFAwszTwzyfT3ltGrouhQIGD3";
	String tweetId = "";
	
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = "https://api.twitter.com";
		RestAssured.basePath = "/1.1/statuses";
	}
	
	@Test 
	public void postTweet() {
		Response res = given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, secretToken)
			.queryParam("status", "My first Tweet")
		.when()
			.post("/update.json")
		.then()
			.statusCode(200)
			.extract().response();
		
		tweetId = res.path("id_str");
		System.out.println("The response.path: " + tweetId);
		
	}
	
	@Test(dependsOnMethods = {"postTweet"})
	public void readTweet() {
		Response res = given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, secretToken)
			.queryParam("id", tweetId)
		.when()
			.get("/show.json")
		.then()
			.extract().response();
		
		String text = res.path("text");
		System.out.println("The tweet text is: " + text);
		
	}
	
	@Test(dependsOnMethods = {"readTweet"})
	public void deleteTweet() {
		given()
			.auth()
			.oauth(consumerKey, consumerSecret, accessToken, secretToken)
			.pathParam("id", tweetId)
		.when()
			.post("/destroy/{id}.json")
		.then()
			.statusCode(200);
		
	}
}
