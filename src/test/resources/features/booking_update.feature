@bookingAPI @updateBooking
Feature: Booking Update API Tests

  @bookingUpdateSuccess
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

  @bookingUpdateError
  Scenario Outline: Update an existing booking with invalid data
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname | lastname | email                  | phone         | checkin    | checkout   |
      | Walter    | White    | walter.white@gmail.com | 7394873984783 | 2025-02-27 | 2025-02-28 |
    Then the response status code should be 201
    When the user updates the booking with following booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 400
    And the user should see response with incorrect "<FieldError>"

    Examples:
      | firstname | lastname | email                  | phone         | checkin    | checkout   | FieldError                            |
      |           | White    | walter.white@gmail.com | 7394873984783 | 2025-02-15 | 2025-02-18 | [Firstname should not be blank]       |
      | Walter    | Wh       | walter.white@gmail.com | 7394873984783 | 2025-02-15 | 2025-02-18 | [size must be between 3 and 30]       |
      | Walter    | White    | walter                 | 7394873984783 | 2025-02-15 | 2025-02-18 | [must be a well-formed email address] |
      | Walter    | White    | walter.white@gmail.com | 739487398     | 2025-02-15 | 2025-02-18 | [size must be between 11 and 21]      |
      | Walter    | White    | walter.white@gmail.com | 7394873984783 |            | 2025-02-18 | [must not be null]                    |
      | Walter    | White    | walter.white@gmail.com | 7394873984783 | 2025-02-15 |            | [must not be null]                    |