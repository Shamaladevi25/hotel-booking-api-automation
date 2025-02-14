package com.booking.stepdefinition;

import com.booking.util.HotelBookingContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;

public class BookingSummarySteps {

    private Response response;
    private final HotelBookingContext context;

    public BookingSummarySteps(HotelBookingContext context) {
        this.context = context;
    }

    @When("the user retrieves booking summary {string} with roomid")
    public void theUserRetrievesBookingSummaryWithRoomId(final String endpoint) {
        context.session.put("endpoint", endpoint);
        final Integer roomId = HotelBookingContext.getRoomIds().getLast();
        response = context.requestSetup().when().get(context.session.get("endpoint").toString() + "?roomid=" + roomId);
    }

    @Then("the response should contain the booking dates {string} and {string} for room id and status code {int}")
    public void theResponseShouldContainTheBookingDatesForRoom(final String expectedCheckInDate, final String expectedCheckOutDate, final int expectedStatusCode) {

        final String checkIn = response.jsonPath().getString("bookings[0].bookingDates.checkin");
        final String checkOut = response.jsonPath().getString("bookings[0].bookingDates.checkout");

        assertThat(checkIn).isNotNull();
        assertThat(checkOut).isNotNull();
        assertThat(expectedCheckInDate).isEqualTo(checkIn);
        assertThat(expectedCheckOutDate).isEqualTo(checkOut);
        assertThat(expectedStatusCode).isEqualTo(response.getStatusCode());
    }

    @When("the user retrieves booking summary {string} with invalid roomid {int}")
    public void theUserRetrievesBookingSummaryWithInvalidRoomId(final String endpoint, final int roomid) {
        context.session.put("endpoint", endpoint);
        response = context.requestSetup().when().get(context.session.get("endpoint").toString() + "?roomid=" + roomid);
    }

    @And("the response body should contain an empty bookings array")
    public void theResponseBodyShouldContainEmptyBookings() {
        response.then().assertThat().body("bookings", empty());
    }

    @Then("user should get the response code {int}")
    public void userShouldGetTheResponseCode(final Integer statusCode) {
        assertThat(Long.valueOf(statusCode)).isEqualTo(Long.valueOf(response.getStatusCode()));
    }
}
