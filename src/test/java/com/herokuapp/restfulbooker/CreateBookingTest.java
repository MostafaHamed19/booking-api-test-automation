package com.herokuapp.restfulbooker;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Booking API")
@Feature("Create Booking")
public class CreateBookingTest extends BaseTest {
    @Test(description = "Create a booking with raw JSON and verify response fields.")
    @Story("Create booking with raw JSON")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test creates a booking using raw JSON request body and verifies the full response.")
    public void createBookingTest() {
        //Create JSON Body
        Response response = createBooking();
        response.prettyPrint();

        //Verification
        //Verify Response 200
        Assert.assertEquals(response.getStatusCode(), 200,"Status Code Should be 200 but its status code is "+response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();

        String actualFirstname = response.jsonPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstname,"Mostafa","Firstname in response not as expected");

        String actualLastname = response.jsonPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastname,"Hamed","Lastname in response not as expected");

        int actualTotalPrice = response.jsonPath().getInt("booking.totalprice");
        softAssert.assertEquals(actualTotalPrice,450,"Total Price in response not as expected");

        boolean actualDepositPaid = response.jsonPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(actualDepositPaid,"DepositPaid Should be False");

        String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin,"2025-06-28","CheckIn in response not as expected");

        String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout,"2025-06-30","CheckOut in response not as expected");

        String actualAdditionalNeeds = response.jsonPath().getString("booking.additionalneeds");
        softAssert.assertEquals(actualAdditionalNeeds,"Breakfast,lunch","Additional needs in response not as expected");

        softAssert.assertAll();
    }
    @Test(description = "Create a booking using POJO and validate the response object.")
    @Story("Create booking with POJO model")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test sends a booking request using a POJO object and compares the request with the response mapping.")
    public void createBookingWithPOJOTest() {
        //Create JSON Body Using POJOs
        Bookingdates bookingdates=new Bookingdates("2025-06-28","2025-06-30");
        Booking booking = new Booking("Kareem","Hamed",450,false,bookingdates,"Breakfast,lunch");


        //Get Response
        Response response = RestAssured.given(Spec).contentType(ContentType.JSON).
                body(booking).post("/booking\n");
        response.prettyPrint();
        Bookingid bookingid = response.as(Bookingid.class);
        System.out.println("Request Booking : "+booking.toString());
        System.out.println("Response Booking : "+bookingid.getBooking().toString());
        //Verification
        //Verify Response 200
        Assert.assertEquals(response.getStatusCode(), 200,"Status Code Should be 200 but its status code is "+response.getStatusCode());
        Assert.assertEquals(bookingid.getBooking().toString(),booking.toString());
    }
}
