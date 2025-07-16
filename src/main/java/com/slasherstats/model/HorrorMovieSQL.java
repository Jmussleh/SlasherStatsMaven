package com.slasherstats.model;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entity class representing a horror movie record stored in the SQL database.
 * This class is mapped to the {@code horror_movies} table using JPA annotations.
 */
@Entity
@Table(name = "horror_movies")
public class HorrorMovieSQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Requires a title
    @NotBlank(message = "Title is required")
    private String title;
    //Requires a director field
    @NotBlank(message = "Director is required")
    private String director;
    //Requires a year after 1888
    @Min(value = 1888, message = "Year must be after 1888")
    private int releaseYear;
    //Requires a runtime of greater than 0
    @Min(value = 1, message = "Runtime must be greater than 0")
    private int runtimeMinutes;
    //Requires something to be written in platform
    @NotBlank(message = "Platform is required")
    private String streamingPlatform;
    //Requires the rating to be above 0.0 and under 10.0
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must be at most 10.0")
    private double rating;

    private String tags;
    //Requires the date to be in the correct format and to be a real date
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @NotNull(message = "Date is required and must be in MM-DD-YYYY format.")
    private LocalDate dateWatched;

    // Constructor for JPA.
    public HorrorMovieSQL() {}

    //Constructs a HorrorMovieSQL object with these fields
    public HorrorMovieSQL(String title, String director, int releaseYear, int runtimeMinutes,
                          String streamingPlatform, double rating, String tags, LocalDate dateWatched) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runtimeMinutes = runtimeMinutes;
        this.streamingPlatform = streamingPlatform;
        this.rating = rating;
        this.tags = tags;
        this.dateWatched = dateWatched;
    }

    // Getters and Setters for each field
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public int getRuntimeMinutes() { return runtimeMinutes; }
    public void setRuntimeMinutes(int runtimeMinutes) { this.runtimeMinutes = runtimeMinutes; }

    public String getStreamingPlatform() { return streamingPlatform; }
    public void setStreamingPlatform(String streamingPlatform) { this.streamingPlatform = streamingPlatform; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public LocalDate getDateWatched() { return dateWatched; }
    public void setDateWatched(LocalDate dateWatched) { this.dateWatched = dateWatched; }

}
