package com.booking.util;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class Hooks {

    private final HotelBookingContext context;

    @Before
    public void createAuthToken(Scenario scenario) {
        final String endpoint = "/auth/login";
        if (!scenario.getSourceTagNames().contains("@noAuth")) {
            context.session.put("endpoint", endpoint);
            JSONObject credentials = new JSONObject();
            credentials.put("username", PropertyFile.getProperty("username"));
            credentials.put("password", PropertyFile.getProperty("password"));
            context.response = context.requestSetup().body(credentials.toString()).when()
                    .post(context.session.get("endpoint").toString());
            context.session.put("token", context.retriveAuthenticatedCookie());
            assertThat(Long.valueOf(200)).isEqualTo(Long.valueOf(context.response.getStatusCode()));
        }
    }
}
