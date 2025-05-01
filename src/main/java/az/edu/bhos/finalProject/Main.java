package az.edu.bhos.finalProject;

import az.edu.bhos.finalProject.console.ConsoleMenu;
import az.edu.bhos.finalProject.controller.*;
import az.edu.bhos.finalProject.dao.*;
import az.edu.bhos.finalProject.entity.*;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.service.*;
import az.edu.bhos.finalProject.util.Json;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String flightDataPath = "database/flights.json";
        String bookingDataPath = "database/bookings.json";
        String userDataPath = "database/users.json";

        try {
            new FileWriter(flightDataPath, false).close();
            new FileWriter(bookingDataPath, false).close();
            new FileWriter(userDataPath, false).close();
            System.out.println("Cleared flight, booking, and user databases.");
        } catch (IOException e) {
            System.out.println("Error clearing databases: " + e.getMessage());
        }

        List<Flight> generatedFlights = new ArrayList<>();
        LocalDateTime baseTime = LocalDateTime.of(2025, 6, 1, 10, 0);
        double intervalHours = 24.0 / 49;

        for (int i = 0; i < 50; i++) {
            LocalDateTime departureTime = baseTime.plusMinutes((long) (i * intervalHours * 60));
            Flight flight = new Flight(
                    String.format("FL%03d", i + 1),
                    "Kiev",
                    "City" + (i % 10 + 1),
                    departureTime,
                    150
            );
            generatedFlights.add(flight);
        }
        try {
            Json.writeJsonFile(flightDataPath, generatedFlights);
            System.out.println("Generated 50 flights over a 24-hour window.");
        } catch (IOException e) {
            System.out.println("Failed to write sample flight data: " + e.getMessage());
        }

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Passenger passenger = new Passenger("Name" + (i + 1), "Surname" + (i + 1));
            User user = new User(passenger, "user" + (i + 1), "pass" + (i + 1));
            users.add(user);
        }
        try {
            Json.writeJsonFile(userDataPath, users);
            System.out.println("Generated 10 sample users.");
        } catch (IOException e) {
            System.out.println("Failed to write sample user data: " + e.getMessage());
        }

        FlightDAO flightDAO = new FlightDAOImpl(flightDataPath);
        BookingDAO bookingDAO = new BookingDAOImpl(bookingDataPath);
        UserDAO userDAO = new UserDAOImpl(userDataPath);

        FlightService flightService = new FlightServiceImpl(flightDAO);
        BookingService bookingService = new BookingServiceImpl(bookingDAO);
        LoggingService loggingService = new LoggingService();
        UserService userService = new UserServiceImpl(userDAO, loggingService);

        FlightController flightController = new FlightController(flightService, userService, loggingService);
        BookingController bookingController = new BookingController(bookingService, userService, loggingService,flightService);
        UserController userController = new UserController(userService);

        ConsoleMenu menu = new ConsoleMenu(flightController, bookingController, userController, loggingService);
        menu.start();

    }
}