package com.booking.util;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelBookingContext {

    private static final String CONTENT_TYPE = PropertyFile.getProperty("content.type");
    public Response response;
    public Map<String, Object> session = new HashMap<>();
    @Getter
    private static final List<Integer> roomIds = new ArrayList<>();
    @Getter
    private static final List<Integer> bookingIds = new ArrayList<>();

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

    public static void addRoomId(final int roomId) {
        roomIds.add(roomId);
    }

    public Cookie retriveAuthenticatedCookie() {
        Cookies cookies = response.detailedCookies();
        return cookies.get("token");
    }

    public static void addBookingId(final int bookingId) {
        bookingIds.add(bookingId);
    }
}
