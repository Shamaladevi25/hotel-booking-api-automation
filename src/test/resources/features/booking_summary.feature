@bookingAPI @bookingSummary
Feature: Booking Summary API Tests

  @getBookingSummaryByValidID
  Scenario Outline: Verify booking summary for different room ids
    Given user has access to endpoint "/booking/"
    When the user books a room with the following booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 201
    When the user retrieves booking summary "/booking/summary" with roomid
    Then the response should contain the booking dates "<checkin>" and "<checkout>" for room id and status code 200

    Examples:
      | firstname | lastname | email                | phone        | checkin    | checkout   |
      | Jacob     | Wary     | jacob.wary@gmail.com | 868787687676 | 2025-03-30 | 2025-03-31 |

  @getBookingSummaryByInvalidID
  Scenario: Verify booking summary for invalid room id
    Given user has access to endpoint "/booking/"
    When the user retrieves booking summary "/booking/summary" with invalid roomid 0
    Then user should get the response code 200
    And the response body should contain an empty bookings array