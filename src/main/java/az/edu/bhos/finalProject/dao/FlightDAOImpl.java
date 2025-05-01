package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.Flight;
import az.edu.bhos.finalProject.exception.FlightNotFoundException;
import az.edu.bhos.finalProject.util.Json;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FlightDAOImpl implements FlightDAO {
    private final String filePath;
    private List<Flight> flightList;

    public FlightDAOImpl(String filePath){
        this.filePath = filePath;
        try{
        flightList = Json.readJsonFile(this.filePath, new TypeReference<List<Flight>>(){});
        }catch(IOException ie){
            System.out.println("Error reading flight data: " + ie.getMessage());
        }
    }

    @Override
    public Flight getById(String id) throws FlightNotFoundException {
        return flightList.stream()
                .filter(f -> f.getFlightID().equals(id))
                .findFirst()
                .orElseThrow(() ->new FlightNotFoundException("No flight found with ID: " + id));
    }

    @Override
    public List<Flight> getAll(){
        return flightList;
    }

    @Override
    public void save() throws IOException {
        Json.writeJsonFile(filePath, flightList);
    }

    @Override
    public boolean insert(Flight flight) throws IOException {
        if(flightList.contains(flight)) {
            return false;
        }
        flightList.add(flight);
        save();
        return true;
    }


    @Override
    public boolean delete(Flight flight) throws IOException, FlightNotFoundException{
        boolean success=flightList.remove(flight);
        if(!success) {
            throw new FlightNotFoundException("Flight not found!");
        }
        save();
        return true;
    }

    @Override
    public boolean deleteById(String id) throws IOException, FlightNotFoundException {
        return delete(getById(id));
    }

    @Override
    public List<Flight> getAvailableFlights(String departure, String destination, LocalDateTime date, int requestedSeats){
        return flightList.stream().
                filter(f->f.getDeparture().equals(departure)&&
                        f.getDestination().equals(destination)&&
                        f.getDepartureDate().equals(date)&&
                        f.getAvailableSeats()>=requestedSeats).toList();
    }
}