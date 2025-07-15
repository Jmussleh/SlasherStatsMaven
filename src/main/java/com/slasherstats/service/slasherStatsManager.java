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
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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
        if (!isValidDate(movie.getDateWatched())) return false;
        if (movie.getRating() < 0.0 || movie.getRating() > 10.0) return false;
        repository.save(movie);
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
        List<HorrorMovieSQL> added = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 8) continue;
                try {
                    HorrorMovieSQL movie = new HorrorMovieSQL(fields[0].trim(), fields[1].trim(), Integer.parseInt(fields[2].trim()),
                            Integer.parseInt(fields[3].trim()), fields[4].trim(), Double.parseDouble(fields[5].trim()),
                            fields[6].trim(), fields[7].trim());

                    if (isValidDate(movie.getDateWatched()) && movie.getRating() >= 0.0 && movie.getRating() <= 10.0) {
                        repository.save(movie);
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
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] fields = line.split(",");
                if (fields.length != 8) continue;
                try {
                    HorrorMovieSQL movie = new HorrorMovieSQL(fields[0].trim(), fields[1].trim(), Integer.parseInt(fields[2].trim()),
                            Integer.parseInt(fields[3].trim()), fields[4].trim(), Double.parseDouble(fields[5].trim()),
                            fields[6].trim(), fields[7].trim());

                    if (isValidDate(movie.getDateWatched()) && movie.getRating() >= 0.0 && movie.getRating() <= 10.0) {
                        repository.save(movie);
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

    /**
     * Updates an existing movie's fields based on title matching and saves the changes.
     *
     * @param updatedMovie a HorrorMovieSQL object with updated field values
     * @return true if the movie was found and updated successfully, false otherwise
     */
    public boolean updateMovie(HorrorMovieSQL updatedMovie) {
        HorrorMovieSQL existingMovie = repository.findByTitleIgnoreCase(updatedMovie.getTitle());
        if (existingMovie == null) return false;

        if (!isValidDate(updatedMovie.getDateWatched())) return false;
        if (updatedMovie.getRating() < 0.0 || updatedMovie.getRating() > 10.0) return false;

        existingMovie.setDirector(updatedMovie.getDirector());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        existingMovie.setRuntimeMinutes(updatedMovie.getRuntimeMinutes());
        existingMovie.setStreamingPlatform(updatedMovie.getStreamingPlatform());
        existingMovie.setRating(updatedMovie.getRating());
        existingMovie.setTags(updatedMovie.getTags());
        existingMovie.setDateWatched(updatedMovie.getDateWatched());

        repository.save(existingMovie);
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

    /**
     * Validates a date string to ensure it matches the format MM-dd-yyyy.
     *
     * @param date the date string to validate
     * @return true if the date is valid, false otherwise
     * @throws java.time.format.DateTimeParseException if the date format is invalid or unresolvable
     */
    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
