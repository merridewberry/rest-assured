package RestfulBooker;


import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static RestfulBooker.Booker.*;
import static RestfulBooker.AuthData.*;

public class BookerCases {

    public static void pingStatus() {
        ping()
                .then()
                .assertThat().statusCode(201);
    }

    public static void validAuthenticationResponse() {
        authenticate(USERNAME, PASSWORD)
                .then()
                .assertThat().statusCode(200)
                .body("$", hasKey("token"));
    }

    public static void invalidAuthenticationResponse(String username, String password) {
        authenticate(username, password)
                .then()
                .assertThat().statusCode(200)
                .body("$", hasKey("reason"));
    }

    public static void createBookingValid() {
        createBooking("0")
                .then()
                .assertThat().statusCode(200);
    }

    public static void createBookingWrongCheckinDay() {
        createBooking("src/test/resources/BookingWrongCheckinDay.json")
                .then()
                .assertThat().statusCode(200);
    }

    public static void createBookingWrongCheckinMonth() {
        createBooking("src/test/resources/BookingWrongCheckinMonth.json")
                .then()
                .assertThat().statusCode(200);
    }

    public static void createBookingWrongDateFormat() {
        createBooking("src/test/resources/BookingWrongDateFormat.json")
                .then()
                .assertThat().statusCode(200);
    }

    public static void getBookingId(String firstname, String lastname, String checkin, String checkout) {
        int expectedId = getId();
        String[] parameters = {firstname, lastname, checkin, checkout};
        Response response = getBookingIds(parameters);

        response.then()
                .assertThat().statusCode(200)
                .body("bookingid", hasItem(expectedId));
    }

    public static void getBookingInfo() {
        getBooking()
                .then()
                .assertThat().statusCode(200)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Johnson"))
                .body("bookingdates.checkin", equalTo("2021-05-07"))
                .body("bookingdates.checkout", equalTo("2021-05-10"));
    }

    public static void updateBookingValid() {
        updateBooking("token=" + getToken())
                .then()
                .assertThat().statusCode(200)
                .body("firstname", equalTo("Johnathan"))
                .body("bookingdates.checkout", equalTo("2021-05-20"));
    }

    public static void updateBookingNoCookie() {
        updateBooking("0")
                .then()
                .assertThat().statusCode(403);
    }

    public static void updateBookingWrongCookie() {
        updateBooking("0")
                .then()
                .assertThat().statusCode(403);
    }

    public static void partialUpdateBookingValid() {
        updateBooking("token=" + getToken())
                .then()
                .assertThat().statusCode(200)
                .body("firstname", equalTo("Johnathan"))
                .body("bookingdates.checkout", equalTo("2021-05-20"));
    }

    public static void partialUpdateBookingNoCookie() {
        updateBooking("0")
                .then()
                .assertThat().statusCode(403);
    }

    public static void partialUpdateBookingWrongCookie() {
        updateBooking("0")
                .then()
                .assertThat().statusCode(403);
    }

    public static void deleteBookingValid() {
        createBooking("0");
        deleteAllBookings("token=" + getToken())
                .then()
                .assertThat().statusCode(201);
    }

    public static void deleteBookingNoCookie() {
        createBooking("0");
        deleteAllBookings("0")
                .then()
                .assertThat().statusCode(403);
    }

    public static void deleteBookingWrongCookie() {
        createBooking("0");
        deleteAllBookings("token=WrongToken")
                .then()
                .assertThat().statusCode(403);
    }


}
