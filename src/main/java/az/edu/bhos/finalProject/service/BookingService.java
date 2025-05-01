package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.entity.Booking;

import java.io.IOException;
import java.util.List;

public interface BookingService {
    Booking getBookingById(String id);

    List<Booking> getAllBookings();

    boolean createBooking(Booking booking) throws IOException;

    boolean deleteBooking(Booking booking) throws IOException;

    boolean deleteBookingById(String id) throws IOException;

    List<Booking> getBookingsByPassenger(String name, String surname);
}
