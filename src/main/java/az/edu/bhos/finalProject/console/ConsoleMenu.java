package az.edu.bhos.finalProject.console;

import az.edu.bhos.finalProject.controller.BookingController;
import az.edu.bhos.finalProject.controller.FlightController;
import az.edu.bhos.finalProject.entity.Flight;
import az.edu.bhos.finalProject.entity.Passenger;
import az.edu.bhos.finalProject.exception.FlightNotFoundException;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.service.UserService;
import java.lang.IllegalArgumentException;
import az.edu.bhos.finalProject.exception.InvalidOptionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final UserService userService;
    private final FlightController flightController;
    private final BookingController bookingController;
    private final LoggingService loggingService;
    private final Scanner scanner;

    public ConsoleMenu(UserService userService,
                       FlightController flightController,
                       BookingController bookingController,
                       LoggingService loggingService) {
        this.userService = userService;
        this.flightController = flightController;
        this.bookingController = bookingController;
        this.loggingService = loggingService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Flight Booking Console App!");

        while (true) {
            if (!userService.isAuthenticated()) {
                authenticate();
                continue;
            }

            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> flightController.viewAllFlights();
                    case "2" -> showFlightInfo();
                    case "3" -> searchAndBookFlight();
                    case "4" -> cancelBooking();
                    case "5" -> bookingController.viewBookings();
                    case "6" -> exitApp();
                    default -> throw new InvalidOptionException("Invalid menu option: " + choice);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                loggingService.logAction("Exception occurred: " + e.getMessage());
            }
        }
    }

    private void authenticate() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            if (userService.authenticate(username, password)) {
                System.out.println("Login successful!\n");
            } else {
                System.out.println("Invalid credentials. Try again.\n");
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Online board (All upcoming flights)");
        System.out.println("2. Show flight info");
        System.out.println("3. Search and book a flight");
        System.out.println("4. Cancel the booking");
        System.out.println("5. My flights");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private void showFlightInfo() {
        System.out.print("Enter Flight ID: ");
        String flightID = scanner.nextLine();
        //flightController.searchAvailableFlights("", "", null, 0);
        System.out.println("Showing info for flight: " + flightID);
        flightController.showFlightInfo(flightID);
    }

    private void searchAndBookFlight() {
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter date (yyyy-MM-ddTHH:mm): ");
        String dateInput = scanner.nextLine();
        System.out.print("Enter number of people: ");
        int people = Integer.parseInt(scanner.nextLine());

        LocalDateTime date = LocalDateTime.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        List<Flight> availableFlights = new ArrayList<>();
        try {
            availableFlights=flightController.searchAvailableFlights("Kiev", destination, date, people);
        }catch (FlightNotFoundException e){
            System.out.println(e.getMessage());
            return;
        }
        if (availableFlights.isEmpty()) {
            return;
        }
        System.out.print("Enter Flight ID to book or 0 to cancel: ");
        String flightID = scanner.nextLine();
        boolean trueFlight=availableFlights.stream().anyMatch(f -> f.getFlightID().equals(flightID));
        if(!trueFlight) {
            System.out.println("Invalid flight ID. Please try again.");
            return;
        }
        if (flightID.equals("0")) return;


        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < people; i++) {
            System.out.print("Enter passenger #" + (i + 1) + " name: ");
            String name = scanner.nextLine();
            System.out.print("Enter passenger #" + (i + 1) + " surname: ");
            String surname = scanner.nextLine();
            passengers.add(new Passenger(name, surname));
        }

        bookingController.createBooking(flightID, passengers);
    }

    private void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");
        String bookingID = scanner.nextLine();
        bookingController.deleteBooking(bookingID);
    }

    private void exitApp() {
        userService.endSession();
        System.out.println("Thank you for using the app. Goodbye!");
        System.exit(0);
    }
}