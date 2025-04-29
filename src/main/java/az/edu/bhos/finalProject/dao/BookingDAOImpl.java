package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.Booking;
import az.edu.bhos.finalProject.util.Json;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    private final String filePath;
    private List<Booking> bookingList;

    public BookingDAOImpl(String filePath) throws IOException {
        this.filePath = filePath;
        bookingList = Json.readJsonFile(this.filePath, new TypeReference<List<Booking>>(){});
    }

    @Override
    public Booking getById(String id) throws RuntimeException{
        return bookingList.stream()
                .filter(b -> b.getBookingId().equals(id))
                .findFirst().orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> getAll() {
        return bookingList;
    }

    @Override
    public void save() throws IOException {
        Json.writeJsonFile(filePath, bookingList);
    }

    @Override
    public boolean insert(Booking booking) throws IOException {
        if(bookingList.contains(booking)) {
            return false;
        }
        bookingList.add(booking);
        save();
        return true;
    }

    @Override
    public boolean delete(Booking booking) throws IOException {
        boolean success=bookingList.remove(booking);
        if(success) {
            save();
        }
        return success;
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        return delete(getById(id));
    }

    @Override
    public List<Booking> getByPassenger(String name, String surname){
        return bookingList.stream()
                .filter(b-> b.getPassengers().stream()
                        .anyMatch(p -> p.getName().equals(name)&& p.getSurname().equals(surname)))
                .toList();
    }

}
