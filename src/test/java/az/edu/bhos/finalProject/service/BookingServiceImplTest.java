package az.edu.bhos.finalProject.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;
import az.edu.bhos.finalProject.dao.BookingDAOImpl;
import az.edu.bhos.finalProject.entity.Booking;
import az.edu.bhos.finalProject.entity.Passenger;
import az.edu.bhos.finalProject.exception.BookingNotFoundException;
import az.edu.bhos.finalProject.service.BookingServiceImpl;

public class BookingServiceImplTest {

    private BookingServiceImpl bookingService;
    private BookingDAOImpl bookingDAO;
    private final String testFilePath = "test_bookings.json";

    @BeforeEach
    public void setUp() throws IOException {
        bookingDAO = new BookingDAOImpl(testFilePath);
        bookingService = new BookingServiceImpl(bookingDAO);
    }

    @AfterEach
    public void tearDown() {
        File testFile = new File(testFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testCreateBooking() throws IOException {
        Passenger passenger = new Passenger("John", "Doe");
        Booking booking = new Booking("B001", "F001", List.of(passenger));

        boolean isCreated = bookingService.createBooking(booking);
        assertTrue(isCreated);
    }

    @Test
    public void testGetBookingById() throws IOException {
        Passenger passenger = new Passenger("John", "Doe");
        Booking booking = new Booking("B001", "F001", List.of(passenger));
        bookingService.createBooking(booking);

        try {
            Booking retrievedBooking = bookingService.getBookingById("B001");
            assertNotNull(retrievedBooking);
            assertEquals("B001", retrievedBooking.getBookingId());
        } catch (BookingNotFoundException e) {
            fail("Booking not found: " + e.getMessage());
        }
    }

    @Test
    public void testGetBookingsByPassenger() throws IOException {
        Passenger passenger = new Passenger("John", "Doe");
        Booking booking = new Booking("B001", "F001", List.of(passenger));
        bookingService.createBooking(booking);

        List<Booking> bookings = bookingService.getBookingsByPassenger("John", "Doe");

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertEquals("John", bookings.get(0).getPassengers().get(0).getName());
        assertEquals("Doe", bookings.get(0).getPassengers().get(0).getSurname());
    }


    @Test
    public void testDeleteBooking() throws IOException {
        Booking bookingToDelete = new Booking("B002", "F002", List.of(new Passenger("Jane", "Doe")));
        bookingService.createBooking(bookingToDelete);
        boolean isDeleted = bookingService.deleteBooking(bookingToDelete);
        assertTrue(isDeleted);
    }

    @Test
    public void testDeleteBookingById() throws IOException {
        Booking bookingToDelete = new Booking("B003", "F003", List.of(new Passenger("Tom", "Smith")));
        bookingService.createBooking(bookingToDelete);
        boolean isDeleted = bookingService.deleteBookingById("B003");
        assertTrue(isDeleted);
    }
}
