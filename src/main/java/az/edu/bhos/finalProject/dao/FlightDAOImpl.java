package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.Flight;
import az.edu.bhos.finalProject.util.Json;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightDAOImpl implements FlightDAO {
    private final String filePath;
    private List<Flight> flightList;

    public FlightDAOImpl(String filePath) throws IOException {
        this.filePath = filePath;
        flightList = Json.readJsonFile(this.filePath, new TypeReference<List<Flight>>(){});
    }

    @Override
    public Flight getById(String id) throws RuntimeException { // i will change this to flightnotfound ex
        return flightList.stream()
                .filter(f -> f.getFlightID().equals(id))
                .findFirst()
                .orElseThrow(() ->new RuntimeException("Flight not found"));
    }

    @Override
    public ArrayList<Flight> getAll(){
        return new ArrayList<>(flightList);
    }

    @Override
    public void save() throws IOException {
        Json.writeJsonFile(filePath, flightList);
    }

    @Override
    public boolean insert(Flight flight) throws IOException { //i might add duplicateflight ex
        if(flightList.contains(flight)) {
            return false;
        }
        flightList.add(flight);
        save();
        return true;
    }


    @Override
    public boolean delete(Flight flight) throws IOException, RuntimeException { // i will change runtime ex to flightnotfound ex
        boolean success=flightList.remove(flight);
        if(success) {
            save();
        }
        return success;
    }

    @Override
    public boolean deleteById(String id) throws IOException, RuntimeException { // i will change runtime ex to flightnotfound ex
        return delete(getById(id));
    }

    @Override
    public List<Flight> getAvailableFlights(String departure, String destination, LocalDate date, int requestedSeats){
        return flightList.stream().
                filter(f->f.getDeparture().equals(departure)&&
                        f.getDestination().equals(destination)&&
                        f.getDepartureDate().equals(date)&&
                        f.getAvailableSeats()>=requestedSeats).toList();
    }
}