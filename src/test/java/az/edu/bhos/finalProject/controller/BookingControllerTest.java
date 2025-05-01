import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import az.edu.bhos.finalProject.service.BookingService;
import az.edu.bhos.finalProject.service.UserService;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.entity.Booking;
import az.edu.bhos.finalProject.entity.Passenger;

import java.util.List;

class BookingControllerTest {

    private BookingController bookingController;
    private BookingService mockBookingService;
    private UserService mockUserService;
    private LoggingService mockLoggingService;

    @BeforeEach
    void setUp() {
        mockBookingService = mock(BookingService.class);
        mockUserService = mock(UserService.class);
        mockLoggingService = mock(LoggingService.class);

        bookingController = new BookingController(mockBookingService, mockUserService, mockLoggingService);

        when(mockUserService.isAuthenticated()).thenReturn(true); // Mock authenticated user
        when(mockUserService.getCurrentUser())
                .thenReturn(new az.edu.bhos.finalProject.entity.User("testUser", new Passenger("John", "Doe")));
    }

    @Test
    void testViewBookings_NoBookingsFound() {
        when(mockBookingService.getBookingsByPassenger("John", "Doe"))
                .thenReturn(List.of()); // Simulate no bookings

        bookingController.viewBookings();

        verify(mockLoggingService).logAction("User testUser viewed their bookings.");
    }

    @Test
    void testCreateBooking_Success() {
        List<Passenger> passengers = List.of(new Passenger("Jane", "Doe"));
        Booking booking = new Booking("FL123", passengers);

        when(mockBookingService.createBooking(booking)).thenReturn(true);

        bookingController.createBooking("FL123", passengers);

        verify(mockLoggingService).logAction("User testUser created a booking.");
    }

    @Test
    void testDeleteBooking_Success() {
        when(mockBookingService.deleteBookingById("BK123")).thenReturn(true);

        bookingController.deleteBooking("BK123");

        verify(mockLoggingService).logAction("User testUser deleted booking with ID BK123");
    }
}
