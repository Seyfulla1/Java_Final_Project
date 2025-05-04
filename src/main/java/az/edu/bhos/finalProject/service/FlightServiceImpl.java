package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.dao.FlightDAO;
import az.edu.bhos.finalProject.entity.Flight;
import az.edu.bhos.finalProject.exception.FlightNotFoundException;
import az.edu.bhos.finalProject.exception.NotEnoughSeatsException;
import az.edu.bhos.finalProject.exception.InvalidSeatsCancellationException;


import java.io.IOException;
import java.time.LocalDateTime;
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
            throw new FlightNotFoundException("Flight not found with ID: " + flightID);
        }
    }

    @Override
    public List<Flight> searchAvailableFlights(String departure, String destination, LocalDateTime date, int requestedSeats) {
        return flightDAO.getAvailableFlights(departure, destination, date, requestedSeats);
    }

    @Override
    public boolean bookFlight(String flightID, int requestedSeats) throws IOException {
        Flight flight = getFlightById(flightID);
        if (flight.getAvailableSeats() < requestedSeats) {
            throw new NotEnoughSeatsException("Not enough available seats for the requested booking.");
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
            throw new InvalidSeatsCancellationException("Number of seats to cancel must be positive.");
        }
        int bookedSeats = flight.getCapacity() - flight.getAvailableSeats();
        if (seatsToCancel > bookedSeats) {
            throw new InvalidSeatsCancellationException("Number of seats to cancel must be less than booked seats.");
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
