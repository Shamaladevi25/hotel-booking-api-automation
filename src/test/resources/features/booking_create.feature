@bookingAPI @createBooking @noAuth
Feature: Booking Creation API Tests

  @bookingCreation @noAuth
  Scenario Outline: Create a booking with valid data successfully
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 201
    And validate the response with JSON schema "createAndUpdateBookingSchema.json"

    Examples:
      | firstname | lastname | email                    | phone        | checkin    | checkout   |
      | Jake      | Doe      | jake.doe@gmail.com       | 879558797034 | 2025-03-15 | 2025-03-18 |
      | Jane      | Smith    | jane.smith@gmail.com     | 988877665544 | 2025-03-15 | 2025-03-18 |
      | George    | William  | george.william@gmail.com | 79879898232  | 2025-03-18 | 2025-03-21 |

  @bookingCreationError @noAuth
  Scenario Outline: Create booking without passing required fields should fail
    Given user has access to endpoint "/booking/"
    When the user tries to book a room with invalid booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   | bookingid   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> | <bookingid> |
    Then the response status code should be 400
    And the user should see response with incorrect "<FieldError>"

    Examples:
      | firstname | lastname | email              | phone        | checkin    | checkout   | FieldError                            |
      |           | Doe      | jake.doe@gmail.com | 879558797034 | 2025-02-15 | 2025-02-18 | [Firstname should not be blank]       |
      | John      | Do       | jake.doe@gmail.com | 879558797034 | 2025-02-15 | 2025-02-18 | [size must be between 3 and 30]       |
      | John      | Doe      | jake               | 879558797034 | 2025-02-15 | 2025-02-18 | [must be a well-formed email address] |
      | John      | Doe      | jake.doe@gmail.com | 8795587970   | 2025-02-15 | 2025-02-18 | [size must be between 11 and 21]      |
      | John      | Doe      | jake.doe@gmail.com | 879558797034 |            | 2025-02-18 | [must not be null]                    |
      | John      | Doe      | jake.doe@gmail.com | 879558797034 | 2025-02-15 |            | [must not be null]                    |
