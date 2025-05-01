import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import az.edu.bhos.finalProject.service.FlightService;
import az.edu.bhos.finalProject.service.UserService;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.entity.Flight;

import java.time.LocalDate;
import java.util.List;

class FlightControllerTest {

    private FlightController flightController;
    private FlightService mockFlightService;
    private UserService mockUserService;
    private LoggingService mockLoggingService;

    @BeforeEach
    void setUp() {
        mockFlightService = mock(FlightService.class);
        mockUserService = mock(UserService.class);
        mockLoggingService = mock(LoggingService.class);

        flightController = new FlightController(mockFlightService, mockUserService, mockLoggingService);

        when(mockUserService.isAuthenticated()).thenReturn(true); // Mock authenticated user
        when(mockUserService.getCurrentUser())
                .thenReturn(new az.edu.bhos.finalProject.entity.User("testUser", null));
    }

    @Test
    void testViewAllFlights_NoFlightsFound() {
        when(mockFlightService.getAllFlights()).thenReturn(List.of()); // Simulate no flights

        flightController.viewAllFlights();

        verify(mockLoggingService).logAction("User testUser viewed all flights.");
    }

    @Test
    void testSearchAvailableFlights_NoResults() {
        when(mockFlightService.searchAvailableFlights("Baku", "London", LocalDate.now(), 2))
                .thenReturn(List.of()); // Simulate no available flights

        flightController.searchAvailableFlights("Baku", "London", LocalDate.now(), 2);

        verify(mockLoggingService).logAction("User testUser searched for flights.");
    }

    @Test
    void testBookFlight_Success() {
        when(mockFlightService.bookFlight("FL456", 2)).thenReturn(true);

        flightController.bookFlight("FL456", 2);

        verify(mockLoggingService).logAction("User testUser booked flight FL456");
    }
}