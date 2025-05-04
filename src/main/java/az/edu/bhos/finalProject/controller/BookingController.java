package az.edu.bhos.finalProject.controller;

import az.edu.bhos.finalProject.service.*;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.entity.Booking;
import az.edu.bhos.finalProject.entity.Passenger;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final LoggingService loggingService;
    private final FlightService flightService;

    public BookingController(BookingService bookingService, UserService userService, LoggingService loggingService, FlightService flightService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.loggingService = loggingService;
        this.flightService = flightService;
    }

    private boolean ensureAuthenticated(String action) {
        if (!userService.isAuthenticated()) {
            loggingService.logAction("Unauthenticated attempt to " + action);
            System.out.println("Please log in to proceed.");
            return true;
        }
        return false;
    }

    public void viewBookings() {
        if (ensureAuthenticated("view bookings")) return;

        Passenger currentPassenger = userService.getCurrentUser().getPassenger();
        List<Booking> bookings = bookingService.getBookingsByPassenger(currentPassenger.getName(), currentPassenger.getSurname());
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            bookings.forEach(System.out::println);
        }

        loggingService.logAction("User " + userService.getCurrentUser().getUsername() + " viewed their bookings.");
    }

    public void createBooking(String flightID, List<Passenger> passengers) {
        if (ensureAuthenticated("create a booking")) return;

        try {
            String bookingId = UUID.randomUUID().toString();

            Booking booking = new Booking(bookingId, flightID, passengers);
            if (flightService.bookFlight(flightID, passengers.size())) {
                bookingService.createBooking(booking);
                loggingService.logAction("User " + userService.getCurrentUser().getUsername() + " created a booking.");

                System.out.println("Booking created successfully!");
            }
        } catch (IOException e) {
            loggingService.logAction("Error creating booking: " + e.getMessage());
            System.out.println("Error creating booking: " + e.getMessage());
        }
    }

    public void deleteBooking(String bookingId) {
        if (ensureAuthenticated("delete a booking")) return;

        try {
            Booking booking = bookingService.getBookingById(bookingId);
            String flightID = booking.getFlightId();
            int seatsToCancel = booking.getPassengers().size();

            if (bookingService.deleteBookingById(bookingId)) {
                flightService.cancelBooking(flightID, seatsToCancel);
                loggingService.logAction("User " + userService.getCurrentUser().getUsername() + " deleted booking with ID " + bookingId);
                System.out.println("Booking deleted successfully!");
            }
        } catch (IOException e) {
            loggingService.logAction("Error deleting booking with ID " + bookingId + ": " + e.getMessage());
            System.out.println("Error deleting booking: " + e.getMessage());
        }
    }

}

