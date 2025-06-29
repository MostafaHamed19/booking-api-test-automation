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
@Feature("Partial Update Booking")
public class PartialUpdateBookingTest extends BaseTest {
    @Test(description = "Partially update an existing booking and verify updated fields.")
    @Story("Patch booking with new data")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test creates a booking, updates some fields using PATCH request, and verifies updated and unchanged fields.")
    public void partialUpdateBookingTest() {
        //Step-1 ==> Create Booking
        Response responseCreate = createBooking();
        responseCreate.prettyPrint();

        //Step-2 ==> Get BookingId of new Booking
        int bookingId = responseCreate.jsonPath().getInt("bookingid");

        //Step-3 ==> Update Booking
        //Create JsonBody
        JSONObject obj = new JSONObject();
        obj.put("firstname", "Sayed");
        obj.put("bookingdates.checkin", "2025-06-29");


        //Get Response
        Response responseUpdate = RestAssured.given(Spec).auth().preemptive().basic("admin","password123").contentType(ContentType.JSON).
                body(obj.toString()).patch("/booking/" + bookingId);
        responseUpdate.prettyPrint();

        //"username" : "admin",
        //"password" : "password123"

        //Verification
        //Verify Response 200
        Assert.assertEquals(responseUpdate.getStatusCode(), 200,"Status Code Should be 200 but its status code is "+responseUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();

        String actualFirstname = responseUpdate.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstname,"Sayed","Firstname in response not as expected");

        String actualLastname = responseUpdate.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastname,"Hamed","Lastname in response not as expected");

        int actualTotalPrice = responseUpdate.jsonPath().getInt("totalprice");
        softAssert.assertEquals(actualTotalPrice,450,"Total Price in response not as expected");

        boolean actualDepositPaid = responseUpdate.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(actualDepositPaid,"DepositPaid Should be False");

        String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin,"2025-06-29","CheckIn in response not as expected");

        String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout,"2025-06-30","CheckOut in response not as expected");

        String actualAdditionalNeeds = responseUpdate.jsonPath().getString("additionalneeds");
        softAssert.assertEquals(actualAdditionalNeeds,"Breakfast,lunch","Additional needs in response not as expected");

        softAssert.assertAll();
    }
}
