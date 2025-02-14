@bookingAPI @getBookingByID
Feature: Booking Retrieval API Tests

  Background: create an auth token
    Given user has access to endpoint "/auth/login"
    When user creates a auth token with credential "admin" & "password"
    Then user should get the response code 200

  @getBookingByBookingID
  Scenario: Retrieve bookings with valid Booking IDs
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname | lastname | email                | phone         | checkin    | checkout   |
      | Gary      | Field    | gary.field@gmail.com | 8475948759847 | 2025-03-27 | 2025-03-28 |
    Then the response status code should be 201
    When the user retrieves booking with booking ID
    Then user should get the response code 200
    And the response body should contain the response with JSON schema "retrieveBookingByBookingIDSchema.json"

  @getBookingByInvalidBookingID
  Scenario: Retrieve bookings with invalid Booking IDs
    Given user has access to endpoint "/booking/"
    When the user retrieves booking with invalid booking ID 0
    Then user should get the response code 404

  @getBookingByRoomID
  Scenario: Retrieve bookings with valid Room IDs
    Given user has access to endpoint "/booking/"
    When the user retrieves booking with room ID
    Then user should get the response code 200
    And the response body should contain the response with JSON schema "retrieveBookingByRoomIDSchema.json"

  @getBookingByInvalidRoomID
  Scenario: Retrieve bookings with invalid room IDs
    Given user has access to endpoint "/booking/"
    When the user retrieves booking with invalid room ID 0
    Then user should get the response code 200
    And the response body should contain an empty bookings array
