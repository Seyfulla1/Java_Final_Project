package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.dao.FlightDAOImpl;
import az.edu.bhos.finalProject.entity.Flight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

public class FlightServiceImplTest {

    private FlightServiceImpl flightService;
    private FlightDAOImpl flightDAO;
    private final String originalFilePath = "src/test/java/az/edu/bhos/finalProject/service/flight.json";
    private final String tempFilePath = "src/test/java/az/edu/bhos/finalProject/service/temp_flight.json";

    @BeforeEach
    public void setUp() throws IOException {
        Files.copy(Path.of(originalFilePath), Path.of(tempFilePath), StandardCopyOption.REPLACE_EXISTING);
        flightDAO = new FlightDAOImpl(tempFilePath);
        flightService = new FlightServiceImpl(flightDAO);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(tempFilePath));
    }

    @Test
    public void testGetAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        assertNotNull(flights);
        assertFalse(flights.isEmpty());
    }

    @Test
    public void testGetFlightById() {
        Flight flight = flightService.getFlightById("F001");
        assertNotNull(flight);
        assertEquals("F001", flight.getFlightID());
    }

    @Test
    public void testSearchAvailableFlights() {
        LocalDate date = LocalDate.of(2025, 5, 15);
        List<Flight> availableFlights = flightService.searchAvailableFlights("Baku", "New York", date, 2);
        assertNotNull(availableFlights);
        assertFalse(availableFlights.isEmpty());
    }

    @Test
    public void testBookFlight() throws IOException {
        boolean isBooked = flightService.bookFlight("F001", 2);
        assertTrue(isBooked);

        Flight flight = flightService.getFlightById("F001");
        assertEquals(8, flight.getAvailableSeats());
    }

    @Test
    public void testCancelBooking() throws IOException {
        boolean isCancelled = flightService.cancelBooking("F001", 3);
        assertTrue(isCancelled);

        Flight flight = flightService.getFlightById("F001");
        assertEquals(13, flight.getAvailableSeats());
    }

    @Test
    public void testBookingNotPossibleDueToInsufficientSeats() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.bookFlight("F001", 20);
        });
        assertEquals("Not enough available seats for the requested booking.", exception.getMessage());
    }

    @Test
    public void testCancelBookingWithInvalidSeats() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flightService.cancelBooking("F001", -1);
        });
        assertEquals("Number of seats to cancel must be positive.", exception.getMessage());
    }
}
