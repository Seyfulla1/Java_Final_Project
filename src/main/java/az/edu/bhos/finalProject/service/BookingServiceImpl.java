package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.dao.BookingDAO;
import az.edu.bhos.finalProject.entity.Booking;
import az.edu.bhos.finalProject.exception.BookingNotFoundException;

import java.io.IOException;
import java.util.List;

public class BookingServiceImpl implements BookingSerivce {
    private final BookingDAO bookingDAO;

    public BookingServiceImpl(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Override
    public Booking getBookingById(String id) {
        Booking booking = bookingDAO.getById(id);
        if (booking == null) {
            throw new BookingNotFoundException("Booking with ID " + id + " not found");
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingDAO.getAll();
    }

    @Override
    public boolean createBooking(Booking booking) throws IOException {
        return bookingDAO.insert(booking);
    }

    @Override
    public boolean deleteBooking(Booking booking) throws IOException {
        boolean result = bookingDAO.delete(booking);
        if (!result) {
            throw new BookingNotFoundException("Booking not found for deletion");
        }
        return true;
    }

    @Override
    public boolean deleteBookingById(String id) throws IOException {
        if (!bookingDAO.deleteById(id)) {
            throw new BookingNotFoundException("Booking with ID " + id + " not found for deletion");
        }
        return true;
    }

    @Override
    public List<Booking> getBookingsByPassenger(String name, String surname) {
        return bookingDAO.getByPassenger(name, surname);
    }
}
