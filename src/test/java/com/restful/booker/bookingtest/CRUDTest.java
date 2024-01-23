package com.restful.booker.bookingtest;

import com.restful.booker.bookinginfo.BookingSteps;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.HashMap;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasKey;

@RunWith(SerenityRunner.class)
public class CRUDTest extends TestBase {
    public static String username = "admin";
    public static String password = "password123";
    public static String firstname = "megha" + TestUtils.getRandomValue();
    public static String lastname = "vij" + TestUtils.getRandomValue();
    public static Integer totalPrice = 1800;
    public static Boolean depositpaid = true;
    public static String additionalneeds = TestUtils.getRandomValue();
    public static int bookingID;
    public static String token;

    @Steps
    BookingSteps steps;

    @Title("This will create Auth token for user")
    @Test
    public void test001() {
        ValidatableResponse response = steps.authUser(username, password);

        response.log().all().statusCode(200);

        HashMap<Object, Object> tokenMap= response.log().all().extract().path("");

        Assert.assertThat(tokenMap,hasKey("token"));
        String jsonString = response.extract().asString();
        token = JsonPath.from(jsonString).get("token");

        System.out.println(token);
    }

    @Title("This will Create a booking user")
    @Test
    public void test002() {

        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2024-05-10");
        bookingsDatesData.put("checkout", "2025-05-10");

        ValidatableResponse response = steps.createBooking(firstname, lastname,totalPrice,
                depositpaid,bookingsDatesData,additionalneeds);

        response.log().all().statusCode(200);
        bookingID= response.log().all().extract().path("bookingid");

        HashMap<Object,Object>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }

    @Title("This will verify user is add booking")
    @Test
    public void test003() {

        ValidatableResponse response = steps.getBookingInfoByID(bookingID);
        response.log().all().statusCode(200);

    }

    @Title("This will Update a booking")
    @Test
    public void test004() {
        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2024-11-18");
        bookingsDatesData.put("checkout", "2025-11-18");

        ValidatableResponse response = steps.updateBooking(bookingID,firstname, lastname,
                totalPrice,depositpaid,bookingsDatesData,additionalneeds);

        response.log().all().statusCode(200);

        HashMap<Object,Object>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }
    @Title("This will partially Update a booking")
    @Test
    public void test005() {
        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2024-06-15");
        bookingsDatesData.put("checkout", "2025-06-15");

        ValidatableResponse response = steps.partialUpdateBooking(bookingID, firstname, lastname,
                totalPrice,depositpaid,bookingsDatesData,additionalneeds);

        response.log().all().statusCode(200);

        HashMap<Object,Object>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }
    @Title("This will verify updated booking with bookingId")
    @Test
    public void test006() {

        ValidatableResponse response = steps.getUpdatedBookingInfoByID(bookingID);
        response.log().all().statusCode(200);
    }

    @Title("This will Deleted with BookingId")
    @Test
    public void test007() {

        ValidatableResponse response = steps.deleteBooking(bookingID);
        response.log().all().statusCode(201);

        ValidatableResponse response1 = steps.getBookingByID(bookingID);
        response1.log().all().statusCode(404);

    }
}