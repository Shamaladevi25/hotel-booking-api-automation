package com.booking.stepdefinition;

import com.booking.util.HotelBookingContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class BookingSummarySteps {

    private Response response;
    private final HotelBookingContext context;

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
        context.response = context.requestSetup().when().get(context.session.get("endpoint").toString() + "?roomid=" + roomid);
    }
}
