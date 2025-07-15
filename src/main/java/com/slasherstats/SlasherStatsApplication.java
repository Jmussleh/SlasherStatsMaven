package com.slasherstats;
//Imports spring application
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//Makes this the main entry point for the spring boot application
/**
 * Main class and entry point for the SlasherStats Spring Boot application.
 * This class bootstraps the application using Spring Boot's auto-configuration.
 */
@SpringBootApplication
//Main class for the application
public class SlasherStatsApplication {
    //Main method to start the application
    /**
     * Launches the Spring Boot application.
     *
     * @param args command-line arguments passed at runtime
     */
    public static void main(String[] args) {
        SpringApplication.run(SlasherStatsApplication.class, args);
    }
}
