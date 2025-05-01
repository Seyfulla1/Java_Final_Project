package az.edu.bhos.finalProject.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Flight {

    private final String flightID;
    private final String departure;

    private final String destination;

    private final LocalDateTime departureDate;

    private final int capacity;
    @JsonProperty("availableSeats")
    private int availableSeats;

    @JsonCreator
    public Flight(@JsonProperty("flightID") String flightID,
                  @JsonProperty("departure") String departure,
                  @JsonProperty("destination") String destination,
                  @JsonProperty("departureDate") LocalDateTime departureDate,
                  @JsonProperty("capacity") int capacity) {

        this.flightID = flightID;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.availableSeats = capacity;
    }

    public String getFlightID() {
        return flightID;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Flight ID: " + flightID +
                ", Departure: " + departure +
                ", Arrival: " + destination +
                ", Date: " + departureDate +
                ", Capacity: " + capacity +
                ", Available Seats: " + availableSeats;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof Flight)) {
            return false;
        }
        Flight thatFlight = (Flight) that;
        return this.flightID.equals(thatFlight.flightID);
    }

    @Override
    public int hashCode() {
        return flightID.hashCode();
    }
}
