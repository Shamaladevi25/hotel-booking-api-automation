@bookingAPI @deleteBooking
Feature: Delete bookings API Tests

  @deleteBookingByValidId
  Scenario: User deletes a booking by ID
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname | lastname | email                | phone        | checkin    | checkout   |
      | John      | Smith    | john.smith@gmail.com | 549879465454 | 2025-04-15 | 2025-04-18 |
    Then the response status code should be 201
    When the user deletes the booking with booking ID
    Then the user should get a response code of 202
    And the booking should be deleted successfully

  @deleteBookingByInvalidId
  Scenario: User deletes a booking by invalid booking ID
    Given user has access to endpoint "/booking/"
    When the user deletes the booking with invalid booking ID 0
    Then the user should get a response code of 404
