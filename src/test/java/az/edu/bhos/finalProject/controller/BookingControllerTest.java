package az.edu.bhos.finalProject.controller;

import az.edu.bhos.finalProject.dao.BookingDAOImpl;
import az.edu.bhos.finalProject.dao.UserDAOImpl;
import az.edu.bhos.finalProject.entity.Booking;
import az.edu.bhos.finalProject.entity.Passenger;
import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.service.BookingService;
import az.edu.bhos.finalProject.service.BookingServiceImpl;
import az.edu.bhos.finalProject.service.UserService;
import az.edu.bhos.finalProject.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookingControllerTest {

    private BookingController bookingController;
    private BookingService bookingService;
    private UserService userService;
    private LoggingService loggingService;

    private final String testBookingFile = "src\\test\\java\\az\\edu\\bhos\\finalProject\\controller\\test_bookings.json";
    private final String testUserFile = "src\\test\\java\\az\\edu\\bhos\\finalProject\\controller\\test_users.json";

    @BeforeEach
    void setUp() throws IOException {
        new File(testBookingFile).delete();
        new File(testUserFile).delete();

        BookingDAOImpl bookingDAO = new BookingDAOImpl(testBookingFile);
        bookingService = new BookingServiceImpl(bookingDAO);

        UserDAOImpl userDAO = new UserDAOImpl(testUserFile);
        loggingService = new LoggingService();
        userService = new UserServiceImpl(userDAO, loggingService);

        Passenger passenger = new Passenger("John", "Doe");
        User user = new User(passenger, "johndoe", "pass123");
        userDAO.insert(user);
        userService.authenticate("johndoe", "pass123");

        bookingController = new BookingController(bookingService, userService, loggingService);
    }

    @Test
    void testCreateAndViewBooking() {
        Passenger passenger = userService.getCurrentUser().getPassenger();
        bookingController.createBooking("FL001", List.of(passenger));

        List<Booking> bookings = bookingService.getBookingsByPassenger("John", "Doe");
        assertEquals(1, bookings.size());
        assertEquals("FL001", bookings.get(0).getFlightId());
    }

    @Test
    void testDeleteBooking() throws IOException {
        Passenger passenger = userService.getCurrentUser().getPassenger();
        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(bookingId, "FL002", List.of(passenger));
        bookingService.createBooking(booking);

        assertEquals(1, bookingService.getAllBookings().size());

        bookingController.deleteBooking(bookingId);

        assertEquals(0, bookingService.getAllBookings().size());
    }

    @Test
    void testViewBookingsWhenEmpty() {
        bookingController.viewBookings();
        assertTrue(bookingService.getBookingsByPassenger("John", "Doe").isEmpty());
    }
}
