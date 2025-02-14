@bookingAPI @bookingE2ETest
Feature: End to End Booking Test

  Scenario Outline: To perform a CURD operation on booking API
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 201
    And validate the response with JSON schema "createAndUpdateBookingSchema.json"
    When the user retrieves booking with booking ID
    Then user should get the response code 200
    And the response body should contain the response with JSON schema "retrieveBookingByBookingIDSchema.json"
    When the user updates the booking with following booking details
      | firstname | lastname | email              | phone         | checkin    | checkout   |
      | Jack      | Raymond  | jack.ray@gmail.com | 7875132139898 | 2025-04-21 | 2025-04-23 |
    Then the response status code should be 200
    And validate the response with JSON schema "createAndUpdateBookingSchema.json"
    When the user deletes the booking with booking ID
    Then the user should get a response code of 202

    Examples:
      | firstname | lastname | email              | phone         | checkin    | checkout   |
      | Jack      | Ray      | jack.ray@gmail.com | 7875132139898 | 2025-04-21 | 2025-04-23 |
