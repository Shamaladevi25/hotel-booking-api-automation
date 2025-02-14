@bookingAPI @updateBooking
Feature: Booking Update API Tests

  Background: create an auth token
    Given user has access to endpoint "/auth/login"
    When user creates a auth token with credential "admin" & "password"
    Then user should get the response code 200

  Scenario: Successfully update an existing booking with valid data
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname | lastname | email                  | phone         | checkin    | checkout   |
      | Jackson   | Wary     | jackson.wary@gmail.com | 7875132139898 | 2025-02-25 | 2025-02-27 |
    Then the response status code should be 201
    When the user updates the booking with following booking details
      | firstname | lastname | email                   | phone         | checkin    | checkout   |
      | Jackson   | Glide    | jackson.glide@gmail.com | 7875132139898 | 2025-02-25 | 2025-02-27 |
    Then the response status code should be 200
    And validate the response with JSON schema "createAndUpdateBookingSchema.json"
