package com.herokuapp.restfulbooker;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Booking API")
@Feature("Full Update Booking")
public class UpdateBookingTest extends BaseTest {
    @Test(description = "Fully update an existing booking and verify all fields.")
    @Story("Put booking with new full data")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test creates a booking, fully updates it using a PUT request, and verifies all fields in the response.")
    public void updateBookingTest() {
        //Step-1 ==> Create Booking
        Response responseCreate = createBooking();
        responseCreate.prettyPrint();

        //Step-2 ==> Get BookingId of new Booking
        int bookingId = responseCreate.jsonPath().getInt("bookingid");

        //Step-3 ==> Update Booking
        //Create JsonBody
        JSONObject obj = new JSONObject();
        obj.put("firstname", "Mohamed");
        obj.put("lastname", "Hamed");
        obj.put("totalprice",150);
        obj.put("depositpaid", true);

        JSONObject bookingDate = new JSONObject();
        bookingDate.put("checkin", "2025-06-28");
        bookingDate.put("checkout", "2025-06-30");

        obj.put("bookingdates", bookingDate);
        obj.put("additionalneeds","Breakfast,lunch");


        //Get Response
        Response responseUpdate = RestAssured.given(Spec).auth().preemptive().basic("admin","password123").contentType(ContentType.JSON).
                body(obj.toString()).put("/booking/" + bookingId);
        responseUpdate.prettyPrint();

        //"username" : "admin",
        //"password" : "password123"

        //Verification
        //Verify Response 200
        Assert.assertEquals(responseUpdate.getStatusCode(), 200,"Status Code Should be 200 but its status code is "+responseUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();

        String actualFirstname = responseUpdate.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstname,"Mohamed","Firstname in response not as expected");

        String actualLastname = responseUpdate.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastname,"Hamed","Lastname in response not as expected");

        int actualTotalPrice = responseUpdate.jsonPath().getInt("totalprice");
        softAssert.assertEquals(actualTotalPrice,150,"Total Price in response not as expected");

        boolean actualDepositPaid = responseUpdate.jsonPath().getBoolean("depositpaid");
        softAssert.assertTrue(actualDepositPaid,"DepositPaid Should be True");

        String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin,"2025-06-28","CheckIn in response not as expected");

        String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout,"2025-06-30","CheckOut in response not as expected");

        String actualAdditionalNeeds = responseUpdate.jsonPath().getString("additionalneeds");
        softAssert.assertEquals(actualAdditionalNeeds,"Breakfast,lunch","Additional needs in response not as expected");

        softAssert.assertAll();
    }
}
