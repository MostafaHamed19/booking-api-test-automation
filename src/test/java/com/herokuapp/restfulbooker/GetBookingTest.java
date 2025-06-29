package com.herokuapp.restfulbooker;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


@Epic("Booking API")
@Feature("Get Booking Details")
public class GetBookingTest extends BaseTest {
    @Test(description = "Get booking details in JSON format using booking ID.")
    @Story("Get booking as JSON")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test creates a booking, retrieves its details in JSON format, and validates all fields.")
    public void getBookingJsonTest() {
        Response responseCreating = createBooking();
        responseCreating.prettyPrint();
        Spec.pathParam("bookingId",responseCreating.jsonPath().getInt("bookingid"));
        Response response = RestAssured.given(Spec).get("/booking/{bookingId}");
        response.prettyPrint();

        //verify Status Code Is 200
        Assert.assertEquals(response.getStatusCode(), 200,"Status Code Should be 200");

        //verify all fields
        SoftAssert softAssert = new SoftAssert();

        String actualFirstname = response.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstname,"Mostafa","Firstname in response not as expected");

        String actualLastname = response.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastname,"Hamed","Lastname in response not as expected");

        int actualTotalPrice = response.jsonPath().getInt("totalprice");
        softAssert.assertEquals(actualTotalPrice,450,"Total Price in response not as expected");

        boolean actualDepositPaid = response.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(actualDepositPaid,"DepositPaid Should be False");

        String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin,"2025-06-28","CheckIn in response not as expected");

        String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout,"2025-06-30","CheckOut in response not as expected");

        softAssert.assertAll();
    }
    @Test(description = "Get booking details in XML format using booking ID.")
    @Story("Get booking as XML")
    @Severity(SeverityLevel.MINOR)
    @Description("This test creates a booking, retrieves its details in XML format, and validates all fields.")
    public void getBookingXMLTest() {
        Response responseCreating = createBooking();
        responseCreating.prettyPrint();
        Spec.pathParam("bookingId",responseCreating.jsonPath().getInt("bookingid"));
        Header xml = new Header("Accept","application/xml");
        Spec.header(xml);
        Response response = RestAssured.given(Spec).get("/booking/{bookingId}");
        response.prettyPrint();

        //verify Status Code Is 200
        Assert.assertEquals(response.getStatusCode(), 200,"Status Code Should be 200");

        //verify all fields
        SoftAssert softAssert = new SoftAssert();

        String actualFirstname = response.xmlPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstname,"Mostafa","Firstname in response not as expected");

        String actualLastname = response.xmlPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastname,"Hamed","Lastname in response not as expected");

        int actualTotalPrice = response.xmlPath().getInt("booking.totalprice");
        softAssert.assertEquals(actualTotalPrice,450,"Total Price in response not as expected");

        boolean actualDepositPaid = response.xmlPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(actualDepositPaid,"DepositPaid Should be False");

        String actualCheckin = response.xmlPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin,"2025-06-28","CheckIn in response not as expected");

        String actualCheckout = response.xmlPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout,"2025-06-30","CheckOut in response not as expected");

        softAssert.assertAll();
    }
}
