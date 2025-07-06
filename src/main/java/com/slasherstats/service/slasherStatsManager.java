package com.slasherstats.service;
import org.springframework.stereotype.Service;
import com.slasherstats.model.horrorMovie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

//Main class that handles CRUD operations
@Service
public class slasherStatsManager {
    //Stores all horror movies the user adds in a list
    private List<horrorMovie> movies;
    //Tracks points as movies are added
    private int accountPoints;

    //Constructor
    public slasherStatsManager() {
        this.movies = new ArrayList<>();
        //Starts account points at 0
        this.accountPoints = 0;
    }
    //Getter for account points
    public int getAccountPoints() {
        return accountPoints;
    }
    //Returns a copy of the movie list
    public List<horrorMovie> viewMovies() {
        return new ArrayList<>(movies);
    }
    //Adds a movie and if the movie is null it will return false
    public boolean addMovie(horrorMovie movie) {
        if (movie == null) return false;
        //Ensures that the input date is in the valid format. If the date is not right it will prompt the user to enter it again.
        if (!isValidDate(movie.getDateWatched())) {
            System.out.println("Invalid date format. Please use MM-DD-YYYY.");
            return false;
        }
        //Ensures that the rating is also within the valid range. Returns a message and prompts the user to try again.
        if (movie.getRating() < 0.0 || movie.getRating() > 10.0) {
            System.out.println("Invalid rating. Must be between 0.0 and 10.0.");
            return false;
        }
        //If all conditions are met add the movie to the database and add 10 points to account points
        movies.add(movie);
        accountPoints += 10;
        return true;
    }
    //This is the addBulkMovies method. This is the web version.
    public List<horrorMovie> addBulkMovies(MultipartFile file) {
        List<horrorMovie> added = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                //recognizes that each field is separated by a comma
                String[] fields = line.split(",");
                if (fields.length != 8) continue;
                try {
                    //Adds all fields and separates them into their respective fields
                    String title = fields[0].trim();
                    String director = fields[1].trim();
                    int year = Integer.parseInt(fields[2].trim());
                    int runtime = Integer.parseInt(fields[3].trim());
                    String platform = fields[4].trim();
                    double rating = Double.parseDouble(fields[5].trim());
                    String tags = fields[6].trim();
                    String date = fields[7].trim();
                    horrorMovie movie = new horrorMovie(title, director, year, runtime, platform, rating, tags, date);
                    //If the date and rating are valid, add the movie to the list
                    if (isValidDate(date) && rating >= 0.0 && rating <= 10.0) {
                        movies.add(movie);
                        accountPoints += 10;
                        added.add(movie);
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.out.println("Error reading MultipartFile: " + e.getMessage());
        }
        return added;
    }
    //This is the addBulkMovies method. This is the CLI version.
    public List<horrorMovie> addBulkMovies(String filename) {
        List<horrorMovie> added = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                //recognizes that each field is separated by a comma
                String[] fields = line.split(",");
                if (fields.length != 8) continue;
                try {
                    //Separates the fields into their respective areas
                    String title = fields[0].trim();
                    String director = fields[1].trim();
                    int year = Integer.parseInt(fields[2].trim());
                    int runtime = Integer.parseInt(fields[3].trim());
                    String platform = fields[4].trim();
                    double rating = Double.parseDouble(fields[5].trim());
                    String tags = fields[6].trim();
                    String date = fields[7].trim();
                    horrorMovie movie = new horrorMovie(title, director, year, runtime, platform, rating, tags, date);
                    //If the date and rating are valid, add the movie to the list
                    if (isValidDate(date) && rating >= 0.0 && rating <= 10.0) {
                        movies.add(movie);
                        accountPoints += 10;
                        added.add(movie);
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return added;
    }

    //Method used to update a movie by searching for it by title
    public boolean updateMovieField(String title, int fieldChoice, String newValue) {
        //Looks for the movie in the database
        horrorMovie movie = findMovie(title);
        //If the movie does not exist return false
        if (movie == null) return false;
        //If the movie is found to exist, allow the user to choose which field to update
        try {
            //Allows a user to select a number to indicate which field to update
            switch (fieldChoice) {
                //Choice to set new title
                case 1 -> movie.setTitle(newValue);
                //Choice to set new director
                case 2 -> movie.setDirector(newValue);
                //Choice to set a new release year
                case 3 -> movie.setReleaseYear(Integer.parseInt(newValue));
                //Choice to set new runtime minutes
                case 4 -> movie.setRuntimeMinutes(Integer.parseInt(newValue));
                //Choice to set new streaming platform
                case 5 -> movie.setStreamingPlatform(newValue);
                //Choice to set new user rating
                case 6 -> {
                    double rating = Double.parseDouble(newValue);
                    if (rating < 0.0 || rating > 10.0) return false;
                    movie.setRating(rating);
                }
                //Choice to set new tags
                case 7 -> movie.setTags(newValue);
                //Choice to set new date
                case 8 -> {
                    //Ensures that the date is valid
                    if (!isValidDate(newValue)) return false;
                    movie.setDateWatched(newValue);
                }
                //If none of the appropriate case values are picked return false
                default -> {
                    return false;
                }
            }
            //If a case is chosen return true
            return true;
            //If an exception is caught return false
        } catch (Exception e) {
            return false;
        }
    }
    public boolean updateMovie(horrorMovie updatedMovie) {
        if (updatedMovie == null) return false;
        horrorMovie existingMovie = findMovie(updatedMovie.getTitle());
        if (existingMovie == null) return false;

        // Validate date and rating before updating
        if (!isValidDate(updatedMovie.getDateWatched())) return false;
        if (updatedMovie.getRating() < 0.0 || updatedMovie.getRating() > 10.0) return false;

        // Update all fields (except title because it's the key)
        existingMovie.setDirector(updatedMovie.getDirector());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        existingMovie.setRuntimeMinutes(updatedMovie.getRuntimeMinutes());
        existingMovie.setStreamingPlatform(updatedMovie.getStreamingPlatform());
        existingMovie.setRating(updatedMovie.getRating());
        existingMovie.setTags(updatedMovie.getTags());
        existingMovie.setDateWatched(updatedMovie.getDateWatched());

        return true;
    }
    //Method to delete a movie given its title
    public boolean deleteMovie(String title) {
        //Searches for the movie by title
        horrorMovie movie = findMovie(title);
        //If movie does not exist return false
        if (movie == null) return false;
        //If the movie is found by title then delete the movie from the database
        movies.remove(movie);
        //When the movie is deleted then subtract 10 points from account points
        accountPoints -= 10;
        return true;
    }
    //Searches for a horror movie by its title and ignores case rules
    public horrorMovie findMovie(String title) {
        for (horrorMovie movie : movies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null;
    }
    //Method to check if the date is valid
    public static boolean isValidDate(String date) {
        try {
            //Requires that the date be returned in this specific format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
