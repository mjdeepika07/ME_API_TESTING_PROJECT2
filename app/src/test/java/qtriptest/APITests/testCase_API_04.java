package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

public class testCase_API_04 {

    RequestSpecification requestSpecification;
    JSONObject jsonObject;
    Response response;
    String email = "deepikamj"+UUID.randomUUID()+"@gmail.com";
 
    // @BeforeClass
    // public void init(){
    
    //     RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
    //     RestAssured.basePath = "/api/v1";

    //     email = "deepikamj"+UUID.randomUUID()+"@gmail.com";
    // }

    @Test(description = "Add a Negative Test Case - Verify that a duplicate user account cannot be created on the Qtrip Website",groups = {"API Tests"})
    public void verifyReservation(){

        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1";

        //register a new user
        System.out.println("Registering the new user - Started");

        requestSpecification = RestAssured.given().log().all();
        requestSpecification.contentType("application/json");

        //constructing the request body using jsonObject for register API
        jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password","Password");
        jsonObject.put("confirmpassword","Password");

        requestSpecification.body(jsonObject.toString());

        response = requestSpecification.post("/register");
        response.then().log().all();

        System.out.println("Registering the new user - Ended");

        System.out.println("Registering the same user again - Started");

        response = requestSpecification.post("/register");
        response.then().log().all();        

        System.out.println("Registering the same user again - Ended");

        System.out.println("Ensure that the second registration fails with status code 400 - Started");

        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);

        System.out.println("Ensure that the second registration fails with status code 400 - Ended");

        System.out.println("Ensure that \"message\": \"Email already exists\" is part of the response - Started");

        JsonPath jsonPath = new JsonPath(response.body().asString());
        System.out.println(jsonPath.getString("message"));
        Assert.assertEquals(jsonPath.getString("message"), "Email already exists");

        System.out.println("Ensure that \"message\": \"Email already exists\" is part of the response - Ended");

    }
}
  

