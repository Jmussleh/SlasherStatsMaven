/**
 * Service class that handles core business logic for managing horror movies.
 * Provides CUD methods to manipulate horror movie entries
 * and tracks user account points.
 */
package com.slasherstats.service;

import com.slasherstats.model.HorrorMovieSQL;
import com.slasherstats.repository.HorrorMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Constructor for injecting a HorrorMovieRepository instance.
 *
 * @param repository for accessing horror movie data
 */
@Service
public class slasherStatsManager {

    private final HorrorMovieRepository repository;
    private int accountPoints;

    @Autowired
    public slasherStatsManager(HorrorMovieRepository repository) {
        this.repository = repository;
        this.accountPoints = 0;
    }

    /**
     * Returns the current account points, which increase or decrease based on if movies are added or taken from the database.
     *
     * @return the number of account points
     */
    public int getAccountPoints() {
        return accountPoints;
    }

    /**
     * Retrieves all horror movies from the repository.
     *
     * @return a list of all horror movies
     */
    public List<HorrorMovieSQL> viewMovies() {
        return repository.findAll();
    }

    /**
     * Adds a single horror movie to the repository after validating date and rating fields.
     *
     * @param movie to add
     * @return true if the movie was added successfully, false if not
     */
    public boolean addMovie(HorrorMovieSQL movie) {
        if (movie == null) return false;
        // Validate date
        if (movie.getDateWatched() == null) return false;
        // Validate rating
        if (movie.getRating() < 0.0 || movie.getRating() > 10.0) return false;
        //Add movie to repository
        repository.save(movie);
        //Add points to account points
        accountPoints += 10;
        return true;
    }

    /**
     * Adds multiple horror movies from a TXT formatted MultipartFile.
     *
     * @param file the uploaded TXT file
     * @return list of successfully added movies
     *  * @throws java.io.IOException if reading from the MultipartFile stream fails
     *  * @throws java.lang.NumberFormatException if numeric parsing fails for rating
     */
    public List<HorrorMovieSQL> addBulkMovies(MultipartFile file) {
        //List to hold all added movies
        List<HorrorMovieSQL> added = new ArrayList<>();
        //Define the expected date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            //Reads the file line by line
            while ((line = reader.readLine()) != null) {
                //Splits the lines into its fields using ,
                String[] fields = line.split(",");
                //Skip any lines that do not meet the field count requirement
                if (fields.length != 8) continue;

                try {
                    //Separates all fields
                    String title = fields[0].trim();
                    String director = fields[1].trim();
                    int releaseYear = Integer.parseInt(fields[2].trim());
                    int runtime = Integer.parseInt(fields[3].trim());
                    String platform = fields[4].trim();
                    double rating = Double.parseDouble(fields[5].trim());
                    String tags = fields[6].trim();
                    LocalDate dateWatched = LocalDate.parse(fields[7].trim(), formatter);
                    //Rating validation check
                    if (rating >= 0.0 && rating <= 10.0) {
                        //If validation passes, create horror movie object
                        HorrorMovieSQL movie = new HorrorMovieSQL(
                                title, director, releaseYear, runtime, platform, rating, tags, dateWatched
                        );
                        //save the movie and add points to account
                        repository.save(movie);
                        accountPoints += 10;
                        added.add(movie);
                    }
                //If validation fails, ignore the line
                } catch (Exception ignored) {
                }
            }
        //Handles the file reading errors
        } catch (Exception e) {
            System.out.println("Error reading MultipartFile: " + e.getMessage());
        }
        //Return list of movies
        return added;
    }

    /**
     * Adds multiple horror movies from a file path.
     *
     * @param filename the path to the CSV file
     * @return list of successfully added movies
     * @throws java.io.FileNotFoundException if the file does not exist at the given path
     * @throws java.lang.NumberFormatException if parsing integers or doubles fails
     */
    public List<HorrorMovieSQL> addBulkMovies(String filename) {
        List<HorrorMovieSQL> added = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] fields = line.split(",");
                //Skip any lines that do not meet the field count requirement
                if (fields.length != 8) continue;

                try {
                    //Separates all fields
                    String title = fields[0].trim();
                    String director = fields[1].trim();
                    int releaseYear = Integer.parseInt(fields[2].trim());
                    int runtime = Integer.parseInt(fields[3].trim());
                    String platform = fields[4].trim();
                    double rating = Double.parseDouble(fields[5].trim());
                    String tags = fields[6].trim();
                    LocalDate dateWatched = LocalDate.parse(fields[7].trim(), formatter);
                    //Rating validation check
                    if (rating >= 0.0 && rating <= 10.0) {
                        //If validation passes, create horror movie object
                        HorrorMovieSQL movie = new HorrorMovieSQL(
                                title, director, releaseYear, runtime, platform, rating, tags, dateWatched
                        );
                        //save the movie and add points to account
                        repository.save(movie);
                        accountPoints += 10;
                        added.add(movie);
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        //Return list of movies
        return added;
    }

    /**
     * Updates an existing movie's fields based on title matching and saves the changes.
     *
     * @param updatedMovie a HorrorMovieSQL object with updated field values
     * @return true if the movie was found and updated successfully, false otherwise
     */
    public boolean updateMovie(HorrorMovieSQL updatedMovie) {
        HorrorMovieSQL existingMovie = repository.findByTitleIgnoreCase(updatedMovie.getTitle());
        if (existingMovie == null) {
            System.out.println("Movie not found.");
            return false;
        }

        existingMovie.setDirector(updatedMovie.getDirector());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        existingMovie.setRuntimeMinutes(updatedMovie.getRuntimeMinutes());
        existingMovie.setStreamingPlatform(updatedMovie.getStreamingPlatform());
        existingMovie.setRating(updatedMovie.getRating());
        existingMovie.setTags(updatedMovie.getTags());
        existingMovie.setDateWatched(updatedMovie.getDateWatched());

        repository.save(existingMovie);
        System.out.println("Movie updated successfully.");
        return true;
    }

    public boolean deleteMovie(String title) {
        HorrorMovieSQL movie = repository.findByTitleIgnoreCase(title);
        if (movie == null) return false;
        repository.delete(movie);
        accountPoints -= 10;
        return true;
    }

    /**
     * Deletes a movie from the repository using its title.
     *
     * @param title the title of the movie to delete
     * @return true if deletion was successful, false otherwise
     */
    public HorrorMovieSQL findMovie(String title) {
        return repository.findByTitleIgnoreCase(title);
    }

    public int recalculatePoints() {
        this.accountPoints = repository.findAll().size() * 10;
        return this.accountPoints;
    }

}
