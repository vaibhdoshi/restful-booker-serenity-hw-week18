package com.restful.booker.bookinginfo;

import com.restful.booker.testbase.TestBaseBooking;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SerenityRunner.class)
public class BookingCrudTest extends TestBaseBooking {
    static String firstname ="Reena" +TestUtils.getRandomValue();
    static String lastname = "Patel"+ TestUtils.getRandomValue();
    static int totalprice = 500;
    static boolean depositpaid = true;
    static String additionalneeds = "Bed and Breakfast";
    static int bookingId;
    @Steps
    BookingSteps bookingSteps;

    @Title("This will create new booking")
    @Test
    public void test001() {
        HashMap<String, Object> bookingdates = new HashMap<>();
        bookingdates.put("checkin", "2023-01-01");
        bookingdates.put("checkout", "2023-01-07");
        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        response.statusCode(200).log().all();
    }

    @Title("getting all booking by Ids")
    @Test
    public void test002(){
        SerenityRest.given().log().all()
                .when()
                .get()
                .then()
                .statusCode(200)
                .log().all();
    }

    @Title("finding new booking by Id and verify if it exists")
    @Test
    public void test003(){
       ValidatableResponse response= bookingSteps.findNewRecordById(bookingId);
       response.statusCode(200).log().all();
        System.out.println(bookingId);
    }

    @Title("Updating the newly created record with ID and verifying it by extracting id " +
            "with updated firstname as query parameter")
    @Test
    public void test004() {
        //updating the record by amending the first name using method from bookingsteps
        firstname = firstname + "updated";
        HashMap<String, Object> bookingdates = new HashMap<>();
        bookingdates.put("checkin", "2023-01-01");
        bookingdates.put("checkout", "2023-01-07");
        ValidatableResponse response = bookingSteps.updateBookingRecordById
                (firstname, lastname, totalprice, depositpaid,bookingdates, additionalneeds,bookingId).log().all().statusCode(405);
        List<Integer> id = bookingSteps.getSingleBookingRecordByFirstName(firstname,bookingId);
        //System.out.println("Actual id is : " + id.get(0));
        //System.out.println("Expected id is : " +bookingId);
        Assert.assertThat(id.get(0),equalTo(bookingId));
    }
    @Title("Delete the newly created record with ID")
    @Test
    public void test005(){
        bookingSteps.deleteBooking(bookingId);
        ValidatableResponse response = bookingSteps.findNewRecordById(bookingId);
        response.statusCode(200).log().all();
    }


}
