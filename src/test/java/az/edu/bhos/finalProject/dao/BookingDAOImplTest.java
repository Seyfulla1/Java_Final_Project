package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.Booking;
import az.edu.bhos.finalProject.entity.Passenger;
import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.exception.BookingNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingDAOImplTest {
    private static BookingDAOImpl bookingDAO;
    private static final String testFilePath = "src\\test\\java\\az\\edu\\bhos\\finalProject\\dao\\test_bookings.json";
    @BeforeEach
    void setUp() throws IOException {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        bookingDAO= new BookingDAOImpl(testFilePath);
        List<Booking> bookings=new ArrayList<>();
        Passenger p1=new Passenger("John", "Doe");
        Passenger p2=new Passenger("Jane", "Smith");
        Passenger p3=new Passenger("Alice", "Johnson");
        Passenger p4=new Passenger("Bob", "Brown");
        Passenger p5=new Passenger("Charlie", "Davis");
        Passenger p6=new Passenger("David", "Wilson");
        Passenger p7=new Passenger("Ava", "Garcia");
        Passenger p8=new Passenger("Ethan", "Martinez");
        Passenger p9=new Passenger("Sophia", "Hernandez");
        Passenger p10=new Passenger("Liam", "Lopez");
        Passenger p11=new Passenger("John", "Hernanez");
        Passenger p12=new Passenger("Ageli", "Veliyev");
        Passenger p13=new Passenger("Ali", "Kurcatov");
        Passenger p14=new Passenger("Aysel", "Xanqizi");
        Passenger p15=new Passenger("Zerqelem", "Qaraqurbanli");
        List<Passenger> passengers1 = new ArrayList<>();
        passengers1.add(p1);
        passengers1.add(p2);
        passengers1.add(p3);
        passengers1.add(p4);
        List<Passenger> passengers2 = new ArrayList<>();
        passengers2.add(p5);
        passengers2.add(p6);
        passengers2.add(p3);
        List<Passenger> passengers3 = new ArrayList<>();
        passengers3.add(p7);
        passengers3.add(p8);
        passengers3.add(p9);
        passengers3.add(p10);
        List<Passenger> passengers4 = new ArrayList<>();
        passengers4.add(p1);
        passengers4.add(p6);
        passengers4.add(p11);
        passengers4.add(p12);
        passengers4.add(p13);
        List<Passenger> passengers5 = new ArrayList<>();
        passengers5.add(p14);
        passengers5.add(p15);
        List<Passenger> passengers6 = new ArrayList<>();
        passengers6.add(p1);
        Booking b1=new Booking("1", "F123", passengers1);
        Booking b2=new Booking("2", "F124", passengers2);
        Booking b3=new Booking("3", "F123", passengers3);
        Booking b4=new Booking("4", "F124", passengers4);
        Booking b5=new Booking("5", "F123", passengers5);
        Booking b6=new Booking("6", "F124", passengers6);
        bookings.add(b1);
        bookings.add(b2);
        bookings.add(b3);
        bookings.add(b4);
        bookings.add(b5);
        bookings.add(b6);
        try{
            for (Booking booking : bookings) {
                bookingDAO.insert(booking);
            }
        }catch (IOException e) {
            System.out.println("Error writing booking data: " + e.getMessage());
        }
    }
    @Test
    void getByIdInvalid() {
        String id="2341";
        assertThrows(BookingNotFoundException.class,()-> bookingDAO.getById(id));
    }
    @Test
    void getByValid() {
        String id="3";
        assertEquals(id,bookingDAO.getById(id).getBookingId());
    }

    @Test
    void getAll() {
        List<Booking> bookings = bookingDAO.getAll();
        assertEquals(6, bookings.size());
    }

    @Test
    void insert() throws IOException {
        Passenger p1=new Passenger("John", "Doe");
        Passenger p2=new Passenger("Jane", "Smith");
        Booking b=new Booking("7", "F123", List.of(p1,p2));
        bookingDAO.insert(b);
        assertEquals(bookingDAO.getById(b.getBookingId()), b);
    }

    @Test
    void deleteInvalid() {
        Passenger p1=new Passenger("John", "Doe");
        Passenger p2=new Passenger("Jane", "Smith");
        Booking b=new Booking("7", "F123", List.of(p1,p2));
        assertThrows(BookingNotFoundException.class,()-> bookingDAO.delete(b));
    }

    @Test
    void deleteValid() throws IOException {
        Passenger p1=new Passenger("Alice", "Johnson");
        Passenger p2=new Passenger("Charlie", "Davis");
        Passenger p3=new Passenger("David", "Wilson");
        Booking b=new Booking("2", "F124", List.of(p1,p2,p3));
        bookingDAO.delete(b);
        assertThrows(BookingNotFoundException.class,()-> bookingDAO.getById(b.getBookingId()));

    }

    @Test
    void deleteByIdInvalid() {
        String id="alma";
        assertThrows(BookingNotFoundException.class,()-> bookingDAO.deleteById(id));
    }

    @Test
    void deleteByValid() throws IOException {
        String id="3";
        bookingDAO.deleteById(id);
        assertThrows(BookingNotFoundException.class,()-> bookingDAO.getById(id));
    }

    @Test
    void getByPassenger() {
        Passenger p=new Passenger("John", "Doe");
        List<Booking> bs = bookingDAO.getByPassenger(p.getName(), p.getSurname());
        assertEquals(3, bs.size());

    }
}