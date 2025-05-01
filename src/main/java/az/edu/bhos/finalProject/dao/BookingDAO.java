package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.Booking;

import java.util.List;

public interface BookingDAO extends DAO<Booking> {
    List<Booking> getByPassenger(String name, String surname);
}
