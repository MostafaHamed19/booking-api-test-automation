package com.herokuapp.restfulbooker;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Booking API")
@Feature("Get Booking IDs")
public class GetBookingIdsTest extends BaseTest {
    @Test(description = "Get all booking IDs without applying any filters.")
    @Story("Get booking IDs without filters")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test retrieves all available booking IDs from the system and verifies that at least one ID exists.")
    public void getBookingIdsWithoutFilterTest(){
        //Get Response With Booking Ids
        Response  response = RestAssured.given(Spec).get("/booking\n");
        response.prettyPrint();

        //Verify Response 200
        Assert.assertEquals(response.getStatusCode(), 200,"Status Code Should be 200 but its status code is "+response.getStatusCode());

        //Verify At Least 1 Booking id  In Response
        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(),"BookingIds should not be empty");
    }

    @Test(description = "Get booking IDs filtered by firstname and lastname.")
    @Story("Get booking IDs with filters")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test retrieves booking IDs by filtering with firstname='Mostafa' and lastname='Hamed', then verifies results.")
    public void getBookingIdsWithFilterTest(){
        Spec.queryParam("firstname","Mostafa");
        Spec.queryParam("lastname","Hamed");
        Response  response = RestAssured.given(Spec).get("/booking");
        response.prettyPrint();

        //Verify Response 200
        Assert.assertEquals(response.getStatusCode(), 200,"Status Code Should be 200 but its status code is "+response.getStatusCode());

        //Verify At Least 1 Booking id  In Response
        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(),"BookingIds should not be empty");
    }
}
