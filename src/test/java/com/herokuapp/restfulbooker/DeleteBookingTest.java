package com.herokuapp.restfulbooker;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Booking API")
@Feature("Delete Booking")
public class DeleteBookingTest extends BaseTest {
    @Test(description = "Delete a booking and verify it no longer exists.")
    @Story("Delete booking by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test creates a booking, deletes it using the booking ID, and verifies that it no longer exists.")
    public void createBookingTest() {
        //Create JSON Body
        Response responseCreate = createBooking();
        responseCreate.prettyPrint();
        int bookingId = responseCreate.jsonPath().getInt("bookingid");
        Response deleteBookingResponse = RestAssured.given(Spec).auth().preemptive()
                .basic("admin","password123").delete("/booking/"+bookingId);
        deleteBookingResponse.prettyPrint();


        //Verification
        //Verify Response 201
        Assert.assertEquals(deleteBookingResponse.getStatusCode(), 201,"Status Code Should be 201 but its status code is "+deleteBookingResponse.getStatusCode());
        Response responseGet = RestAssured.get("https://restful-booker.herokuapp.com/booking/"+bookingId);
        responseGet.prettyPrint();

        Assert.assertEquals(responseGet.getBody().asString(),"Not Found","Body Should be 'not found' But Its Not");
    }
}
