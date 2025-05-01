package az.edu.bhos.finalProject.controller;

import az.edu.bhos.finalProject.dao.FlightDAOImpl;
import az.edu.bhos.finalProject.dao.UserDAOImpl;
import az.edu.bhos.finalProject.entity.Flight;
import az.edu.bhos.finalProject.entity.Passenger;
import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.service.FlightService;
import az.edu.bhos.finalProject.service.FlightServiceImpl;
import az.edu.bhos.finalProject.service.UserService;
import az.edu.bhos.finalProject.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlightControllerTest {

    private FlightController flightController;
    private FlightService flightService;
    private UserService userService;
    private LoggingService loggingService;

    private final String testFlightFile = "src\\test\\java\\az\\edu\\bhos\\finalProject\\controller\\test_flights.json";
    private final String testUserFile = "src\\test\\java\\az\\edu\\bhos\\finalProject\\controller\\test_users.json";

    @BeforeEach
    void setUp() throws IOException {
        new File(testFlightFile).delete();
        new File(testUserFile).delete();

        FlightDAOImpl flightDAO = new FlightDAOImpl(testFlightFile);
        flightService = new FlightServiceImpl(flightDAO);

        UserDAOImpl userDAO = new UserDAOImpl(testUserFile);
        loggingService = new LoggingService();
        userService = new UserServiceImpl(userDAO, loggingService);

        Passenger passenger = new Passenger("John", "Doe");
        User user = new User(passenger, "johndoe", "pass123");
        userDAO.insert(user);
        userService.authenticate("johndoe", "pass123");

        Flight flight = new Flight("FL123", "Baku", "London", LocalDateTime.now().withSecond(0).withNano(0).plusHours(2), 100);
        flightDAO.insert(flight);

        flightController = new FlightController(flightService, userService, loggingService);
    }

    @Test
    void testViewAllFlights() {
        flightController.viewAllFlights();
        assertFalse(flightService.getAllFlights().isEmpty());
    }

    @Test
    void testSearchAvailableFlights() {
        LocalDateTime date = LocalDateTime.now().withSecond(0).withNano(0).plusHours(1);
        flightController.searchAvailableFlights("Baku", "London", date, 1);
        List<Flight> results = flightService.searchAvailableFlights("Baku", "London", date, 1);
        assertFalse(results.isEmpty());
    }

    @Test
    void testBookAndCancelFlight() throws IOException {
        String flightId = "FL123";
        int originalSeats = flightService.getFlightById(flightId).getAvailableSeats();

        flightController.bookFlight(flightId, 2);
        assertEquals(originalSeats - 2, flightService.getFlightById(flightId).getAvailableSeats());

        flightController.cancelBooking(flightId, 2);
        assertEquals(originalSeats, flightService.getFlightById(flightId).getAvailableSeats());
    }
}
