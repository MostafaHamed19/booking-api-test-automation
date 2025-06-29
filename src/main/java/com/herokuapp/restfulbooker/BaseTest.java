package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected RequestSpecification Spec;
    @BeforeMethod
    public void setUp(){
         Spec = new RequestSpecBuilder().
                setBaseUri("https://restful-booker.herokuapp.com").
                build();
    }
    protected  Response createBooking() {
        //Create JSON Body
        JSONObject obj = new JSONObject();
        obj.put("firstname", "Mostafa");
        obj.put("lastname", "Hamed");
        obj.put("totalprice",450);
        obj.put("depositpaid", false);

        JSONObject bookingDate = new JSONObject();
        bookingDate.put("checkin", "2025-06-28");
        bookingDate.put("checkout", "2025-06-30");

        obj.put("bookingdates", bookingDate);
        obj.put("additionalneeds","Breakfast,lunch");


        //Get Response
        Response response = RestAssured.given(Spec).contentType(ContentType.JSON).
                body(obj.toString()).post("/booking\n");
        return response;
    }
}
