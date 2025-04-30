package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.entity.Flight;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    List<Flight> getAllFlights();

    Flight getFlightById(String flightID);

    List<Flight> searchAvailableFlights(String departure, String destination, LocalDate date, int requestedSeats);

    boolean bookFlight(String flightID, int requestedSeats) throws IOException;

    boolean cancelBooking(String flightID, int seatsToCancel) throws IOException;
}
