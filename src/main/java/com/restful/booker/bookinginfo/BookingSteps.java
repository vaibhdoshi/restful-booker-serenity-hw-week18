package com.restful.booker.bookinginfo;

import com.restful.booker.constants.EndPoints;
import com.restful.booker.model.AuthPojo;
import com.restful.booker.model.BookingPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;
import java.util.List;

public class BookingSteps {
    static String token;

    @Step("Creating booking with firstname:{0},lastname:{1},totalprice{2},depositpaid:{3},bookingdates:{4},addtionalneeds:{5}")
    public ValidatableResponse createBooking(String firstname, String lastname, int totalprice, boolean depositpaid, HashMap<String, Object> bookingdates, String additionalneeds) {
        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(bookingPojo)
                .when()
                .post()
                .then();
    }

    @Step("Getting booking information with firstname:{0}")
    public ValidatableResponse findNewRecordById(int bookingId) {
        //find the new record by id
        return SerenityRest.given().log().all()
                .header("Accept", "application/json")
                .pathParam("bookingId", 1751)
                .when()
                .get(EndPoints.GET_SINGLE_BOOKING_WITH_ID)
                .then();
    }

    @Step("updating  booking record by ID with firstname:{0},lastname:{1},totalprice:{2},depositpaid:{3},bookingdate:{4},additionalneeds:{5},bookingid:{6}")
    public ValidatableResponse updateBookingRecordById(String firstname, String lastname, int totalprice,
                                                       boolean depositpaid, HashMap<String, Object> bookingdates,
                                                       String additionalneeds, int bookingId) {

        token = "token=" + AuthPojo.getAuthToken();
        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("cookie", token)
                .pathParam("bookingId",bookingId)
                .body(bookingPojo)
                .when()
                .put(EndPoints.UPDATE_SINGLE_BOOKING_WITH_ID)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Step("Getting the student information with firstName: {0},bookingid:{1}")

    public List<Integer> getSingleBookingRecordByFirstName(String firstname, int bookingid) {
        return SerenityRest.given().log().all()
                .header("Accept", "application/json")
                .queryParams("firstname", firstname)
                .when()
                .get()
                .then()
                .extract()
                .path("bookingid");
    }

    @Step("Deleting the booking with bookingid: {0}")
    public ValidatableResponse deleteBooking(int bookingid) {
        //token generated at the time of update will be used here using static variable
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .header("Cookie", token)
                .pathParam("bookingId", bookingid)
                .when()
                .delete(EndPoints.DELETE_SINGLE_BOOKING_WITH_ID)
                .then();
    }


}