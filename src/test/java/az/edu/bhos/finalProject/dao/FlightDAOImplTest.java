package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.Flight;
import az.edu.bhos.finalProject.exception.FlightNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightDAOImplTest {
    private static FlightDAOImpl flightDAO;
    private static final String testFilePath = "src\\test\\java\\az\\edu\\bhos\\finalProject\\dao\\test_flights.json";
    private static final LocalDateTime baseTime = LocalDateTime.of(2025, 5, 1, 10, 0);
    @BeforeEach
    void setUp() throws IOException {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        flightDAO = new FlightDAOImpl(testFilePath);

        Flight flight1 = new Flight("F123", "New York", "Los Angeles", baseTime.plusHours(3),100);
        Flight flight2 = new Flight("F124", "Chicago", "Miami", baseTime.plusHours(2),50);
        Flight flight3 = new Flight("F125", "Seattle", "San Francisco", baseTime.plusHours(22),75);
        Flight flight4 = new Flight("F126", "Boston", "Austin", baseTime.plusHours(1),200);
        Flight flight5 = new Flight("F127", "Denver", "Phoenix", baseTime.plusHours(21),150);
        Flight flight6 = new Flight("F128", "Orlando", "Las Vegas", baseTime.plusHours(6),120);
        Flight flight7 = new Flight("F129", "Atlanta", "Dallas", baseTime.plusHours(1),80);
        Flight flight8 = new Flight("F130", "San Diego", "Seattle", baseTime.plusHours(16),60);
        Flight flight9 = new Flight("F131", "Philadelphia", "Newark", baseTime.plusHours(11),90);
        Flight flight10 = new Flight("F132", "Detroit", "Cleveland", baseTime.plusHours(10),110);
        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);
        flights.add(flight3);
        flights.add(flight4);
        flights.add(flight5);
        flights.add(flight6);
        flights.add(flight7);
        flights.add(flight8);
        flights.add(flight9);
        flights.add(flight10);
        try {
            for (Flight flight : flights) {
                flightDAO.insert(flight);
            }
        } catch (IOException e) {
            System.out.println("Error writing flight data: " + e.getMessage());
        }
    }
    @Test
    void getByIdInvalid() {
        String flightId = "F999";
        assertThrows(FlightNotFoundException.class, () -> flightDAO.getById(flightId));
    }
    @Test
    void getByValid() {
        String flightId = "F130";
        assertEquals(flightId, flightDAO.getById(flightId).getFlightID());
    }

    @Test
    void getAll() {
        List<Flight> flights = flightDAO.getAll();
        assertEquals(10, flights.size());
    }

    @Test
    void insert() throws IOException{
        Flight flight= new Flight("J2251","Baku","Nakhchivan",baseTime.plusHours(2),100);
        flightDAO.insert(flight);
        assertEquals(flight.getFlightID(), flightDAO.getById(flight.getFlightID()).getFlightID());
    }

    @Test
    void deleteInvalid() {
        Flight flight = new Flight("F188", "San Diego", "Seattle", baseTime.plusHours(1), 60);
        assertThrows(FlightNotFoundException.class, () -> flightDAO.delete(flight));
    }

    @Test
    void deleteValid() throws IOException {
        Flight flight = new Flight("F129", "Atlanta", "Dallas", baseTime.plusHours(1),80);
        flightDAO.delete(flight);
        assertThrows(FlightNotFoundException.class, () -> flightDAO.getById(flight.getFlightID()));
    }

    @Test
    void deleteByIdInvalid() {
        String flightId = "Essalam Habibi";
        assertThrows(FlightNotFoundException.class, () -> flightDAO.deleteById(flightId));
    }

    @Test
    void deleteByValid() throws IOException {
        String flightId="F126";
        flightDAO.deleteById(flightId);
        assertThrows(FlightNotFoundException.class, () -> flightDAO.getById(flightId));

    }

    @Test
    void getAvailableFlights() throws IOException {
        String departure = "New York";
        String destination = "Los Angeles";
        LocalDateTime date = baseTime.plusHours(3);
        Flight f1= new Flight("A123", "New York", "Los Angeles", date, 63);
        Flight f2= new Flight("A124", "New York", "Los Angeles", date, 45);
        Flight f3= new Flight("A125", "New York", "Los Angeles", date, 50);
        flightDAO.insert(f1);
        flightDAO.insert(f2);
        flightDAO.insert(f3);
        int requestedSeats = 50;
        List<Flight> availableFlights = flightDAO.getAvailableFlights(departure, destination, date, requestedSeats);
        assertEquals(3, availableFlights.size());
    }
}