package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.entity.Flight;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {
    Flight getFlightById(String flightID);

    List<Flight> getAllFlights();

    List<Flight> searchAvailableFlights(String departure, String destination, LocalDateTime date, int requestedSeats);

    boolean bookFlight(String flightID, int requestedSeats) throws IOException;

    boolean cancelBooking(String flightID, int seatsToCancel) throws IOException;
}
