package com.booking.stepdefinition;

import com.booking.util.HotelBookingContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor
public class DeleteBookingSteps {

    private Response response;
    private final HotelBookingContext context;
    private final Integer storedBookingId = HotelBookingContext.getBookingIds().getLast();

    // Step to send DELETE request
    @When("the user deletes the booking with booking ID")
    public void theUserDeletesTheBookingWithBookingID() {
        Cookie token = (Cookie) context.session.get("token");
        if (storedBookingId != null) {
            response = context.requestSetup().cookie(token).when()
                    .delete(context.session.get("endpoint").toString() + storedBookingId);
        } else {
            throw new RuntimeException("Booking ID not available for DELETE request.");
        }
    }

    @Then("the user should get a response code of {int}")
    public void theUserShouldGetResponseCodeOf(final int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @And("the booking should be deleted successfully")
    public void theBookingWithIdShouldBeDeletedSuccessfully() {
        // Attempt to retrieve the booking again to check if it was deleted (should return 404)
        Response retrieveResponse = given().cookie(context.retriveAuthenticatedCookie()).when()
                .get(context.session.get("endpoint").toString() + storedBookingId);

        // Ensure the status code is 404 (not found), meaning the booking is deleted
        assertThat("Booking should be deleted", retrieveResponse.statusCode(), equalTo(404));
    }

    @When("the user deletes the booking with invalid booking ID {int}")
    public void theUserDeletesTheBookingWithInvalidBookingID(final int bookingid) {
        response = context.requestSetup().cookie(context.retriveAuthenticatedCookie()).when()
                .delete(context.session.get("endpoint").toString() + bookingid);

    }
}
