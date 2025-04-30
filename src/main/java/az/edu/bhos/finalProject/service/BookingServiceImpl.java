package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.dao.BookingDAO;
import az.edu.bhos.finalProject.entity.Booking;

import java.io.IOException;
import java.util.List;

public class BookingServiceImpl implements BookingSerivce {
    private final BookingDAO bookingDAO;

    public BookingServiceImpl(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Override
    public Booking getBookingById(String id) {
        return bookingDAO.getById(id);
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
        return bookingDAO.delete(booking);
    }

    @Override
    public boolean deleteBookingById(String id) throws IOException {
        return bookingDAO.deleteById(id);
    }

    @Override
    public List<Booking> getBookingsByPassenger(String name, String surname) {
        return bookingDAO.getByPassenger(name, surname);
    }
}
