package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.Flight;

import java.time.LocalDate;
import java.util.List;

public interface FlightDAO extends DAO<Flight>{
    List<Flight> getAvailableFlights(String departure, String arrival, LocalDate date, int requestedSeats);//might add custom exception
}
