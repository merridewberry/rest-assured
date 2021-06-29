package RestfulBooker;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static RestfulBooker.AuthData.*;

public class Booker {
    static String url = "https://restful-booker.herokuapp.com";

    public static Response ping() {
        return given()
                .baseUri(url)
                .basePath("/ping")
                .get();
    }

    public static Response authenticate(String username, String password) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode authBody = mapper.createObjectNode();
        authBody.put("username", username);
        authBody.put("password", password);

        return given()
                .baseUri(url)
                .basePath("/auth")
                .header("Content-Type", "application/json")
                .body(String.valueOf(authBody))
                .post()
                .then().extract().response();
    }

    public static Response createBooking(String file) {
        File bookingBody;
        if (file.equals("0")) {
            bookingBody = new File("src/test/resources/BookingJohn.json");
        } else {
            bookingBody = new File(file);
        }
        return given()
                .baseUri(url)
                .basePath("/booking")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bookingBody)
                .post()
                .then().extract().response();
    }

    public static int getId() {
        return createBooking("0").then().extract().path("bookingid");
    }

    public static Response getBookingIds(String[] parameters) {
        RequestSpecification req = given()
                .baseUri(url)
                .basePath("/booking")
                .header("Content-Type", "application/json");
        String[] fields = {"firstname", "lastname", "checkin", "checkout"};
        for (String parameter : parameters) {
            if (parameter != null) {
                req.queryParam(fields[ArrayUtils.indexOf(parameters, parameter)], parameter);
            }
        }
        return req.get().then().extract().response();
    }

    public static Response getBooking() {
        return given()
                .baseUri(url)
                .basePath("/booking/" + getId())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .get()
                .then().extract().response();
    }

    public static Response updateBooking(String token) {
        File bookingBody = new File("src/test/resources/BookingJohnUpdated.json");
        RequestSpecification req = given()
                .baseUri(url)
                .basePath("/booking/" + getId())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        if (!token.equals("0")) {
            req.header("Cookie", token);
        }
        return req
                .body(bookingBody)
                .put()
                .then().extract().response();
    }

    public static Response partialUpdateBooking(String token) {
        File bookingBody = new File("src/test/resources/BookingJohnPartiallyUpdated.json");
        RequestSpecification req = given()
                .baseUri(url)
                .basePath("/booking/" + getId())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        if (!token.equals("0")) {
            req.header("Cookie", token);
        }
        return req
                .body(bookingBody)
                .put()
                .then().extract().response();
    }

    public static Response deleteAllBookings(String token) {
        String[] parameters = {"John", "Johnson", "2021-05-06", "2021-05-10"};
        Response response = getBookingIds(parameters);
        int size = response.then().extract().jsonPath().getList("$").size();
        for (int i = 0; i < size; i++) {
            RequestSpecification req = given()
                    .baseUri(url)
                    .basePath("/booking/" + getId())
                    .header("Content-Type", "application/json");
            if (!token.equals("0")) {
                req.header("Cookie", token);
            }
            response = req
                    .delete()
                    .then().extract().response();
        }
        return response;
    }

    public static String getToken() {
        return authenticate(USERNAME, PASSWORD)
                .then().extract().path("token");
    }
}
