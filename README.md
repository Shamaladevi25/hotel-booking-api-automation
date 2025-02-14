# HotelBooking API Automation

This project contains automated tests for the HotelBooking API using Cucumber and RestAssured.

## Prerequisites

- Java - JDK 21
- Build Tool - Maven 3.9.9
- Junit - Test runner
- Cucumber - BDD/Gherkin style feature files
- Rest assured - Rest api verification library

## Local Setup

1. Clone the GIT Repository https://github.com/Shamaladevi25/hotel-booking-api-automation/
2. Load as Maven project

## To run the test using tags:

Go to TestRunner.class(src/test/java/com/example/runner/TestRunner.java)

Update the "tags = @bookingAPI" and run using the command `mvn clean test`

Different tags we can use:

1. @createBooking
2. @getBookingByID
3. @bookingSummary
4. @updateBooking
5. @deleteBooking
6. @noAuth - Use this tag for scenarios where token generation is not required.

### Swagger Definition: https://automationintesting.online/booking/swagger-ui/index.html#

### BDD (Feature file / Step definition)

BDD requires a feature file to invoke the step definitions:

- Create the scenarios in feature file as per the requirements, so each step in feature file has to match a step
  definition in class file;
- Following the BDD practices for coding

### REST API

- This is written in a feature file using Cucumber.
- Each line of the scenario is tied to backend code that actually executes the line (step).

### JSON schema validation

- The main goal is to ensure that the JSON format is correct as well as all data inside it. We are using the Json
  schemafile and with help of Jsonschema validator we validate the response

### Handling Token Generation and Random Room ID
**1. Token Generation for Authenticated Endpoints:**
         For scenarios that require a valid authentication token, a Before hook step is used to ensure the token
     is generated before each scenario runs.
         If a scenario does not require token generation, tag the feature or scenario with @noTokenRequired to skip the
     authentication step.

**2. To avoid a 409 conflict error:** (such as when trying to create bookings with duplicate room IDs), the helper
function (generateRandomRoomId) is used to generate a random room ID between 2000 and 2999:
This ensures each test scenario uses a unique room ID, preventing the issue of attempting to book the same room multiple
times and receiving a 409 error from the API.
