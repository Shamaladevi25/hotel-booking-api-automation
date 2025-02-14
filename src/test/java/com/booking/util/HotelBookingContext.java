package com.booking.util;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class HotelBookingContext {

    private static final String CONTENT_TYPE = PropertyFile.getProperty("content.type");
    public Map<String, Object> session = new HashMap<>();

    public RequestSpecification requestSetup() {
        RestAssured.reset();
        Options options = Options.builder().logStacktrace().build();
        RestAssuredConfig config = CurlRestAssuredConfigFactory.createConfig(options);
        RestAssured.baseURI = PropertyFile.getProperty("hotel.booking.baseURL");
        return RestAssured.given()
                .log().all()
                .config(config)
                .contentType(CONTENT_TYPE)
                .accept(CONTENT_TYPE);
    }
}
