package az.edu.bhos.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Booking {
    private final String bookingId;
    private final String flightId;
    private final List<Passenger> passengers;
    @JsonCreator
    public Booking(@JsonProperty("bookingID") String bookingId,
                   @JsonProperty("flightID") String flightId,
                   @JsonProperty("passengers") List<Passenger> passengers) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.passengers = passengers;
    }
    public String getBookingId() {
        return bookingId;
    }
    public String getFlightId() {
        return flightId;
    }
    public List<Passenger> getPassengers() {
        return passengers;
    }
    @JsonIgnore
    public int getNumberOfPassengers() {
        return passengers.size();
    }
    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
                ", Flight ID: " + flightId +
                ", Passengers: " + passengers;
    }
    @Override
    public boolean equals(Object that){
        if(this==that){
            return true;
        }
        if(!(that instanceof Booking)){
            return false;
        }
        Booking thatBooking=(Booking) that;
        return this.bookingId.equals(thatBooking.bookingId);
    }
    @Override
    public int hashCode() {
        return bookingId.hashCode();
    }

}
