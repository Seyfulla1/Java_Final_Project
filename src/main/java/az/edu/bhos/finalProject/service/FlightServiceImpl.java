package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.dao.FlightDAO;
import az.edu.bhos.finalProject.entity.Flight;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FlightServiceImpl implements FlightService {
    private final FlightDAO flightDAO;

    public FlightServiceImpl(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightDAO.getAll();
    }

    @Override
    public Flight getFlightById(String flightID) {
        try {
            return flightDAO.getById(flightID);
        } catch (RuntimeException e) {
            throw new RuntimeException("Flight not found: " + flightID, e);
        }
    }

    @Override
    public List<Flight> searchAvailableFlights(String departure, String destination, LocalDate date, int requestedSeats) {
        return flightDAO.getAvailableFlights(departure, destination, date, requestedSeats);
    }

    @Override
    public boolean bookFlight(String flightID, int requestedSeats) throws IOException {
        Flight flight = getFlightById(flightID);
        if (flight.getAvailableSeats() < requestedSeats) {
            throw new RuntimeException("Not enough available seats for the requested booking.");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - requestedSeats);

        try {
            flightDAO.save();
            return true;
        } catch (IOException e) {
            throw new IOException("Error saving flight data", e);
        }
    }

    @Override
    public boolean cancelBooking(String flightID, int seatsToCancel) throws IOException {
        Flight flight = getFlightById(flightID);
        if (seatsToCancel <= 0) {
            throw new IllegalArgumentException("Number of seats to cancel must be positive.");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() + seatsToCancel);

        try {
            flightDAO.save();
            return true;
        } catch (IOException e) {
            throw new IOException("Error saving flight data", e);
        }
    }
}
