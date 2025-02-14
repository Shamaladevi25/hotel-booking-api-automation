package com.booking.stepdefinition;

import com.booking.util.HotelBookingContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;

@RequiredArgsConstructor
public class RetrieveBookingSteps {

    private final HotelBookingContext context;

    @When("the user retrieves booking with booking ID")
    public void theUserRetrievesBookingWithBookingID() {
        final Integer storedBookingId = HotelBookingContext.getBookingIds().getLast();
        if (storedBookingId != null) {
            context.response = context.requestSetup().cookie(context.retriveAuthenticatedCookie()).when()
                    .get(context.session.get("endpoint").toString() + storedBookingId);
        } else {
            throw new RuntimeException("Booking ID not available for GET request.");
        }
    }

    @Then("the response body should contain the response with JSON schema {string}")
    public void theResponseBodyShouldContainBookingDetailsWithID(final String schemaFileName) {
        context.response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + schemaFileName));
    }

    @Then("user should get the response code {int}")
    public void userShouldGetTheResponseCode(final Integer statusCode) {
        assertThat(Long.valueOf(statusCode)).isEqualTo(Long.valueOf(context.response.getStatusCode()));
    }

    @When("user creates a auth token with credential {string} & {string}")
    public void createAuthTokenWithCredentials(final String username, final String password) {
        JSONObject credentials = new JSONObject();
        credentials.put("username", username);
        credentials.put("password", password);
        context.response = context.requestSetup().body(credentials.toString()).when()
                .post(context.session.get("endpoint").toString());
        context.session.put("token", context.retriveAuthenticatedCookie());
    }

    @When("the user retrieves booking with invalid booking ID {int}")
    public void theUserRetrievesBookingWithInvalidBookingID(final Integer bookingid) {
        context.response = context.requestSetup().cookie(context.retriveAuthenticatedCookie()).when()
                .get(context.session.get("endpoint").toString() + bookingid);
    }

    @And("the response body should contain an empty bookings array")
    public void theResponseBodyShouldContainEmptyBookings() {
        context.response.then().assertThat().body("bookings", empty());
    }

    @When("the user retrieves booking with room ID")
    public void theUserRetrievesBookingWithRoomID() {
        final Integer roomId = HotelBookingContext.getRoomIds().getLast();
        context.response = context.requestSetup().cookie(context.retriveAuthenticatedCookie()).when()
                .get(context.session.get("endpoint").toString() + "?roomid=" + roomId);
    }

    @When("the user retrieves booking with invalid room ID {int}")
    public void theUserRetrievesBookingWithInvalidRoomID(final Integer roomid) {
        context.response = context.requestSetup().cookie(context.retriveAuthenticatedCookie()).when()
                .get(context.session.get("endpoint").toString() + "?roomid=" + roomid);

    }
}
