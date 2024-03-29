package qtriptest.APITests;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
public class testCase_API_02 {

String baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
String basePath = "/api/v1/cities";

RequestSpecification requestSpecification;
JSONObject jsonObject;
Response response;

@Test(description = "Search City - Verify that the search City API Returns the correct number of results",groups = {"API Tests"})
public void citySearch(){

    //1. Search for "beng" using the cities search API
    System.out.println("Search for \"beng\" using the cities search API - Started");

    requestSpecification = RestAssured.given().queryParam("q", "beng").log().all();    
    requestSpecification.contentType("application/json");
    
    response = requestSpecification.get(baseURI+basePath+"");

    JsonPath jsonPath = new JsonPath(response.body().asString());

    System.out.println(response.asPrettyString());

    System.out.println("Search for \"beng\" using the cities search API - Ended");

    System.out.println("2. Verify the count of results being returned - Started");

    List<JSONObject> list = jsonPath.getList("$");
    System.out.println("The length of the List is : " + list.size());

    System.out.println("2. Verify the count of results being returned - Ended");
    System.out.println("After successful search, the status code must be 200 - Started");

    Assert.assertEquals(response.statusCode(),200);

    System.out.println("After successful search, the status code must be 200 - Ended");

    System.out.println("3. The Description should contain \"100+ Places\" - Started");

    System.out.println("The value of description is : " + jsonPath.getString("[0].description"));
    // ResponseBody responseBody = response.getBody();
    // String responseBodyString = responseBody.asString();

    //System.out.println("The value of description is : " + responseBodyString.contains("100+ Places"));
    Assert.assertEquals(jsonPath.getString("[0].description"), "100+ Places");
    
    System.out.println("3. The Description should contain \"100+ Places\" - Ended");
    System.out.println("4. Validate Schema - Started");


	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/schema.json")));


    System.out.println("4. Validate Schema - Ended");

}




}
