package qtriptest.APITests;
import io.restassured.http.ContentType;
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
import java.util.UUID;



public class testCase_API_01 {
    
    String baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
    String basePath = "/api/v1";

    RequestSpecification requestSpecification;
    JSONObject jsonObject;
    Response response;

    String email = "deepikamj"+UUID.randomUUID()+"@gmail.com";

    @Test(description = "User Registration - Verify that a new user can be registered and login using APIs of QTrip",groups = {"API Tests"})
    public void Registration(){

        
        System.out.println("1. Use the register API to register a new user - Start");

        requestSpecification = RestAssured.given().log().all();
        requestSpecification.contentType("application/json");
        
        jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password","Password");
        jsonObject.put("confirmpassword","Password");
        requestSpecification.body(jsonObject.toString());

        response = requestSpecification.post(baseURI+basePath+"/register");

        response.then().log().all();
        Assert.assertEquals(response.statusCode(), 201);

        System.out.println("1. Use the register API to register a new user - End");

        System.out.println("2. Use the Login API to login using the registered user - Start");

        requestSpecification = RestAssured.given().log().all();
        requestSpecification.contentType("application/json");
        
        jsonObject.remove("confirmpassword");
        requestSpecification.body(jsonObject.toString());

        response = requestSpecification.post(baseURI+basePath+"/login");

        response.then().log().all();
        Assert.assertEquals(response.statusCode(), 201);

        System.out.println("2. Use the Login API to login using the registered user - End");

        System.out.println("3. Validate that the login was successful - Start");

        JsonPath jsonPath = new JsonPath(response.body().asString());
        Assert.assertTrue(jsonPath.getBoolean("success"));
        
        System.out.println("3. Validate that the login was successful - End");

        System.out.println("4. Verify that the token and user id is returned for login - Start");

        Assert.assertNotNull("jsonPath.data.token");
        Assert.assertNotNull(jsonPath.getString("data.id"));

        System.out.println("4. Verify that the token and user id is returned for login - End");


    }
   
}
