package RestfullBookerTest;

import RestfulBooker.Booker;
import static RestfulBooker.BookerCases.*;

import com.sun.org.glassfish.gmbal.Description;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;

public class BookerTest {

    @BeforeEach
    public void deleteAllTheJohns() throws IOException {
        Booker.deleteAllBookings("token=" + Booker.getToken());
    }

    @Test
    @Description("Testing if anything is working at all")
    public void pingTest() {
        pingStatus();
    }

    @Test
    @Description("Testing authentication with default login and password")
    public void authenticationResponseTest() throws IOException {
        validAuthenticationResponse();
    }

//Not sure if failed authentication should give code 200, but, well, here it is.
    @ParameterizedTest
    @CsvFileSource(resources = "/InvalidAuthData.csv")
    @Description("Testing authentication with invalid login and/or password")
    public void invalidAuthenticationTest(String username, String password) throws IOException {
        invalidAuthenticationResponse(username, password);
    }

    @Test
    @Description("Testing booking creation")
    public void createBookingTest() throws IOException {
        createBookingValid();
    }

    @Test
    @Description("Testing booking creation with check in day later than check out day")
    public void createBookingWrongCheckinDayTest() throws IOException {
        createBookingWrongCheckinDay();
    }

    @Test
    @Description("Testing booking creation with check in month later than check out month")
    public void createBookingWrongCheckinMonthTest() throws IOException {
        createBookingWrongCheckinMonth();
    }

    @Test
    @Description("Testing booking creation with no actual dates")
    public void createBookingWrongDateFormat() throws IOException {
        createBookingWrongDateFormat();
    }

/*Looks like there is some bug in API, and although documentation says that searching by check-in date returns
* all the results with same or greater value, in fact it only returns results with greater value. So I made check-in
* date in json greater than in all the search queries. */
    @ParameterizedTest
    @CsvFileSource(resources = "/BookingIdsQuery.csv")
    @Description("Testing if the id of searched booking is present in the search results")
    public void getBookingIdTest(String firstname, String lastname, String checkin, String checkout) throws IOException {
        getBookingId(firstname, lastname, checkin, checkout);
    }

    @Test
    @Description("Testing if searching by booking id gives correct result")
    public void getBookingTest() throws IOException {
        getBookingInfo();
    }

/*I thought about writing tests with wrong dates for full and partial update, but they obviously are going to fail,
because it looks like restful booker utilizes dates as a string, so I decided not to bother.*/

    @Test
    @Description("Testing booking update")
    public void updateBookingTest() throws IOException {
        updateBookingValid();
    }

    @Test
    @Description("Testing booking update with no cookie")
    public void updateBookingNoCookieTest() throws IOException {
        updateBookingNoCookie();
    }

    @Test
    @Description("Testing booking update with wrong cookie")
    public void updateBookingWrongCookieTest() throws IOException {
        updateBookingWrongCookie();
    }

    @Test
    @Description("Testing partial booking update")
    public void partialUpdateTest() throws IOException {
        partialUpdateBookingValid();
    }

    @Test
    @Description("Testing partial booking update with no cookie")
    public void partialUpdateNoCookieTest() throws IOException {
        partialUpdateBookingNoCookie();
    }

    @Test
    @Description("Testing partial booking update with wrong cookie")
    public void partialUpdateWrongCookieTest() throws IOException {
        partialUpdateBookingWrongCookie();
    }

    @Test
    @Description("Testing booking deletion")
    public void deleteBookingTest() throws IOException {
        deleteBookingValid();
    }

    @Test
    @Description("Testing booking deletion with no cookie")
    public void deleteBookingNoCookieTest() throws IOException {
        deleteBookingNoCookie();
    }

    @Test
    @Description("Testing booking deletion with wrong cookie")
    public void deleteBookingWrongCookieTest() throws IOException {
        deleteBookingWrongCookie();
    }
}
