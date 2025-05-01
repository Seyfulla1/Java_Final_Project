package az.edu.bhos.finalProject.controller;

import az.edu.bhos.finalProject.service.FlightService;
import az.edu.bhos.finalProject.service.UserService;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.entity.Flight;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FlightController {

    private final FlightService flightService;
    private final UserService userService;
    private final LoggingService loggingService;

    public FlightController(FlightService flightService, UserService userService, LoggingService loggingService) {
        this.flightService = flightService;
        this.userService = userService;
        this.loggingService = loggingService;
    }

    private boolean ensureAuthenticated(String action) {
        if (!userService.isAuthenticated()) {
            loggingService.logAction("Unauthenticated attempt to " + action);
            System.out.println("Please log in to proceed.");
            return false;
        }
        return true;
    }

    public void viewAllFlights() {
        if (!ensureAuthenticated("view flights")) return;

        List<Flight> flights = flightService.getAllFlights();
        flights.forEach(System.out::println);

        loggingService.logAction("User " + userService.getCurrentUser().getUsername() + " viewed all flights.");
    }

    public void searchAvailableFlights(String departure, String destination, LocalDate date, int requestedSeats) {
        if (!ensureAuthenticated("search for flights")) return;

        List<Flight> availableFlights = flightService.searchAvailableFlights(departure, destination, date, requestedSeats);
        if (availableFlights.isEmpty()) {
            System.out.println("No available flights found.");
        } else {
            availableFlights.forEach(System.out::println);
        }

        loggingService.logAction("User " + userService.getCurrentUser().getUsername() + " searched for flights.");
    }

    public void bookFlight(String flightID, int requestedSeats) {
        if (!ensureAuthenticated("book a flight")) return;

        try {
            if (flightService.bookFlight(flightID, requestedSeats)) {
                loggingService.logAction("User " + userService.getCurrentUser().getUsername() + " booked flight " + flightID);
                System.out.println("Flight booked successfully!");
            }
        } catch (IOException e) {
            loggingService.logAction("Error booking flight " + flightID + ": " + e.getMessage());
            System.out.println("Error booking flight: " + e.getMessage());
        }
    }

    public void cancelBooking(String flightID, int seatsToCancel) {
        if (!ensureAuthenticated("cancel a flight booking")) return;

        try {
            if (flightService.cancelBooking(flightID, seatsToCancel)) {
                loggingService.logAction("User " + userService.getCurrentUser().getUsername() + " canceled booking for flight " + flightID);
                System.out.println("Flight booking canceled successfully!");
            }
        } catch (IOException e) {
            loggingService.logAction("Error canceling booking for flight " + flightID + ": " + e.getMessage());
            System.out.println("Error canceling booking: " + e.getMessage());
        }
    }
}
