@bookingAPI @createBooking
Feature: Booking Creation API Tests

  Scenario Outline: Create a booking with valid data successfully
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 201
    And validate the response with JSON schema "createBookingSchema.json"

    Examples:
      | firstname | lastname | email                    | phone        | checkin    | checkout   |
      | Jake      | Doe      | jake.doe@gmail.com       | 879558797034 | 2025-03-15 | 2025-03-18 |
      | Jane      | Smith    | jane.smith@gmail.com     | 988877665544 | 2025-03-15 | 2025-03-18 |
      | George    | William  | george.william@gmail.com | 79879898232  | 2025-03-18 | 2025-03-21 |
