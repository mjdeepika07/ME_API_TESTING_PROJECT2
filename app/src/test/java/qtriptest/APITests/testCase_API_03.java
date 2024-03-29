package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.UUID;

//Verify that a reservation can be made using the QTrip API

public class testCase_API_03 {

    //Verify that a reservation can be made using the QTrip API
    RequestSpecification requestSpecification;
    JSONObject jsonObject;
    Response response;
    String email = "deepikamj"+UUID.randomUUID()+"@gmail.com";

    //@BeforeClass
    //public void init(){
    
        //RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        //RestAssured.basePath = "/api/v1";

        //email = "deepikamj"+UUID.randomUUID()+"@gmail.com";
    //}
  

    @Test(description = "booking Flow - Verify that a reservation can be made using the QTrip API",groups = {"API Tests"})
    public void verifyReservation(){

        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1";
        
        System.out.println("Verify that a reservation can be made using the QTrip API - Started");

       
        
        System.out.println("1.1 Use the register API to register a new user - Start");

        requestSpecification = RestAssured.given().log().all();
        requestSpecification.contentType("application/json");
        
        //constructing the request body using jsonObject for register API
        jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password","Password");
        jsonObject.put("confirmpassword","Password");
        requestSpecification.body(jsonObject.toString());

        //calling the register API using POST method
        response = requestSpecification.post("/register");

        response.then().log().all();
        Assert.assertEquals(response.statusCode(), 201);

        System.out.println("1.1 Use the register API to register a new user - End");

        System.out.println("1.2 Use the Login API to login using the registered user - Start");

        requestSpecification = RestAssured.given().log().all();
        requestSpecification.contentType("application/json");
        
        //Modifying the request body using jsonObject for login API
        jsonObject.remove("confirmpassword");
        requestSpecification.body(jsonObject.toString());

        //calling the login API using POST method
        response = requestSpecification.post("/login");

        response.then().log().all();
        //check if the response status code is '201' or not 
        Assert.assertEquals(response.statusCode(), 201);

        //create JsonPath object to access the response variables in response body
        JsonPath jsonPath = new JsonPath(response.body().asString());
        String userId = jsonPath.getString("data.id");
        String token = jsonPath.getString("data.token");

        System.out.println("The userId from login API response is : " + userId);

        System.out.println("1.2 Use the Login API to login using the registered user - End");

        System.out.println("2. Perform a booking using a post call - Started");

        requestSpecification = RestAssured.given().log().all();
        requestSpecification.contentType("application/json").headers("Authorization", "Bearer "+token);

        //Modifying the same jsonObject to accomodate the new variables and their values in the request body
        jsonObject.remove("email");
        jsonObject.remove("password");

        jsonObject.put("userId",userId);
        jsonObject.put("name","Deepika");
        jsonObject.put("date","2024-09-09");
        jsonObject.put("person","1");
        jsonObject.put("adventure","2447910730");

        //Passing the jsonObject as body to the requestSpecification Object
        requestSpecification.body(jsonObject.toString());
        
         //Getting the adventure id from the jsonObject (body which was sent as input)
         String bookedAventureId = jsonObject.getString("adventure");
         System.out.println("The booked Adventure Id is : "+bookedAventureId);


        response = requestSpecification.post("/reservations/new");
       

        System.out.println("3. Ensure that the booking goes fine - Started");
        Assert.assertTrue(jsonPath.getBoolean("success"));
        System.out.println("3. Ensure that the booking goes fine - Ended");

        //printing response
        System.out.println("***************The response*************");
        System.out.println(response.asPrettyString());

        System.out.println("1. On a successful booking, status code 200 should be returned - Started");
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
        
        System.out.println("1. On a successful booking, status code 200 should be returned - Ended");

        System.out.println("2. Perform a GET Reservations call for the user and ensure that the successful booking is listed there - Started");

        RestAssured.given().log().all();
        response = requestSpecification.get("/reservations?id="+userId);
        System.out.println("The adventure 2447910730 is booked for the user " + userId + ": " + response.asPrettyString());

        jsonPath = new JsonPath(response.body().asString());
        String retrievedAdventureId = jsonPath.getString("[0].adventure");
        System.out.println("The retrieved adventure id is : " + retrievedAdventureId);
        Assert.assertEquals(retrievedAdventureId,bookedAventureId);

        System.out.println("2. Perform a GET Reservations call for the user and ensure that the successful booking is listed there - Ended");

        System.out.println("2. Perform a booking using a post call - Ended");

        System.out.println("Verify that a reservation can be made using the QTrip API - Ended");


        

    }



}