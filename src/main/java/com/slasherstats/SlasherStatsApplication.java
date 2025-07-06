package com.slasherstats;
//Imports spring application
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//Makes this the main entry point for the spring boot application
@SpringBootApplication
//Main class for the application
public class SlasherStatsApplication {
    //Main method to start the application
    public static void main(String[] args) {
        SpringApplication.run(SlasherStatsApplication.class, args);
    }
}
