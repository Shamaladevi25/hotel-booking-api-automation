package com.booking.stepdefinition;

import com.booking.util.HotelBookingContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Cookie;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateAndUpdateBookingSteps {

    private Response response;
    private JSONObject requestBody;
    private final HotelBookingContext context;
    private static final Logger LOG = LogManager.getLogger(CreateAndUpdateBookingSteps.class);

    public CreateAndUpdateBookingSteps(HotelBookingContext context) {
        this.context = context;
    }

    @Given("user has access to endpoint {string}")
    public void userHasAccessToEndpoint(final String endpoint) {
        context.session.put("endpoint", endpoint);
    }

    @When("the user books a room with the following booking details")
    public void theUserBooksRoomWithBookingDetails(final DataTable dataTable) {

        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            final int roomId = Integer.parseInt(generateRandomRoomId());
            HotelBookingContext.addRoomId(roomId);

            requestBody = createBookingRequestBody(row, roomId);
            response = context.requestSetup().body(requestBody.toString()).when().post(context.session.get("endpoint").toString());

            LOG.info("Booking has been created successfully" + response.toString());
            validateBookingResponse(row.get("firstname"), row.get("lastname"), row.get("checkin"), row.get("checkout"));
            if (response.getStatusCode() == 201) {
                final int bookingId = response.jsonPath().getInt("booking.bookingid");
                HotelBookingContext.addBookingId(bookingId);
            }
        }
    }

    private void validateBookingResponse(String firstname, String lastname, String checkin, String checkout) {
        String responseFirstname = response.jsonPath().getString("booking.firstname");
        String responseLastname = response.jsonPath().getString("booking.lastname");
        String responseCheckIn = response.jsonPath().getString("booking.bookingdates.checkin");
        String responseCheckout = response.jsonPath().getString("booking.bookingdates.checkout");

        assertThat(firstname).isEqualTo(responseFirstname);
        assertThat(lastname).isEqualTo(responseLastname);
        assertThat(checkin).isEqualTo(responseCheckIn);
        assertThat(checkout).isEqualTo(responseCheckout);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(final int expectedStatusCode) {
        assertThat(expectedStatusCode).isEqualTo(response.getStatusCode());
    }

    @Then("validate the response with JSON schema {string}")
    public void userValidatesResponseWithJSONSchema(final String schemaFileName) {
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + schemaFileName));
    }

    @When("the user tries to book a room with invalid booking details")
    public void theUserSendsPOSTRequestWithTheInvalidBookingDetails(final DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            final int roomId = Integer.parseInt(generateRandomRoomId());
            requestBody = createBookingRequestBody(row, roomId);
            response = context.requestSetup().body(requestBody.toString()).when().post(context.session.get("endpoint").toString());
            LOG.info("Booking has failed: " + response.toString());
        }
    }

    @And("the user should see response with incorrect {string}")
    public void theUserShouldSeeTheResponseWithIncorrectField(final String errorMessage) {
        final String actualErrorMessage = response.jsonPath().getString("fieldErrors");
        assertThat(errorMessage).isEqualTo(actualErrorMessage);
    }

    @When("the user updates the booking with following booking details")
    public void theUserUpdatesTheBookingWithFollowingDetails(final DataTable dataTable) {
        Cookie token =(Cookie) context.session.get("token");

        for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
            final String bookingId = HotelBookingContext.getBookingIds().getLast().toString();
            requestBody = createBookingRequestBody(data, Integer.parseInt(generateRandomRoomId()));
            response = context.requestSetup().cookie(token).body(requestBody.toString())
                    .when().put(context.session.get("endpoint").toString() + bookingId);

            final String updatedLastname = response.jsonPath().getString("booking.lastname");
            if (response.statusCode() == 200) {
                assertThat(data.get("lastname")).isEqualTo(updatedLastname);
            }
        }
    }

    private JSONObject createBookingRequestBody(Map<String, String> row, int roomid) {
        return new JSONObject()
                .put("bookingid", 0)
                .put("roomid", roomid)
                .put("firstname", row.get("firstname"))
                .put("lastname", row.get("lastname"))
                .put("depositpaid", true)
                .put("email", row.get("email"))
                .put("phone", row.get("phone"))
                .put("bookingdates", new JSONObject()
                        .put("checkin", row.get("checkin"))
                        .put("checkout", row.get("checkout")));
    }

    private static String generateRandomRoomId() {
        final Random random = new Random();
        return String.valueOf(2000 + random.nextInt(900)); // Generates a number between 2000 and 2999
    }
}
