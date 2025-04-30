package az.edu.bhos.finalProject.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class LoggingServiceTest {

    private final String logFile = "user_actions.log";
    private LoggingService loggingService;

    @BeforeEach
    void setUp() throws IOException {
        loggingService = new LoggingService();
        Files.deleteIfExists(Paths.get(logFile));
    }

    @Test
    void testLogActionWritesToFile() throws IOException {
        String action = "Test user login";
        loggingService.logAction(action);

        File file = new File(logFile);
        assertTrue(file.exists(), "Log file should exist");

        String content = Files.readString(file.toPath());
        assertTrue(content.contains(action), "Log file should contain the logged action");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(logFile));
    }
}
