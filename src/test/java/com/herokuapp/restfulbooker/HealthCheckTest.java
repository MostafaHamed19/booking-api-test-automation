package com.herokuapp.restfulbooker;


import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

@Epic("Booking API")
@Feature("Health Check and Metadata")
public class HealthCheckTest extends BaseTest {
    @Test(description = "Basic health check test to ensure the API is up.")
    @Story("API health check")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This test hits the /ping endpoint and verifies that the API is alive and returns 201 status code.")
    public void healthCheckTest() {
        given().
        spec(Spec).
        when().
             get("/ping\n").
        then().
             assertThat().
             statusCode(201);

    }
    @Test(description = "Get and log response headers and cookies from the health check endpoint.")
    @Story("Inspect response metadata")
    @Severity(SeverityLevel.MINOR)
    @Description("This test sends a request to /ping and logs all response headers and cookies for inspection.")
    public void headersAndCookiesTest() {
        Header someHeader = new Header("some-header", "some-value");
        Spec.header(someHeader);
        Response response = RestAssured.given(Spec).get("/ping");
        //Get Headers
        Headers headers = response.getHeaders();
        System.out.println("Headers : "+headers);
        Header serverHeader1 = headers.get("Server");
        System.out.println(serverHeader1.getName()+" : "+serverHeader1.getValue());
        //GetCookies
        Cookies cookies = response.getDetailedCookies();
        System.out.println("Cookies : "+cookies);
    }
}
