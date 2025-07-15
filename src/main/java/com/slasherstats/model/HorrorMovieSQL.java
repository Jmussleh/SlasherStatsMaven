package com.slasherstats.model;

import jakarta.persistence.*;

/**
 * Entity class representing a horror movie record stored in the SQL database.
 * This class is mapped to the {@code horror_movies} table using JPA annotations.
 */
@Entity
@Table(name = "horror_movies")
public class HorrorMovieSQL {
    /** The primary key for the movie record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String director;
    private int releaseYear;
    private int runtimeMinutes;
    private String streamingPlatform;
    private double rating;
    private String tags;
    private String dateWatched;

    /**
     * Default no-argument constructor required by JPA.
     */
    // Default constructor for JPA
    public HorrorMovieSQL() {
    }

    /**
     * Constructs a new {@code HorrorMovieSQL} with all fields except {@code id}.
     *
     * @param title the movie's title
     * @param director the name of the director
     * @param releaseYear the year the movie was released
     * @param runtimeMinutes the duration of the movie in minutes
     * @param streamingPlatform the platform where the movie is available
     * @param rating the rating of the movie (0.0–10.0)
     * @param tags a comma-separated string of tags
     * @param dateWatched the date the movie was watched (MM-DD-YYYY format)
     */
    public HorrorMovieSQL(String title, String director, int releaseYear, int runtimeMinutes,
                          String streamingPlatform, double rating, String tags, String dateWatched) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runtimeMinutes = runtimeMinutes;
        this.streamingPlatform = streamingPlatform;
        this.rating = rating;
        this.tags = tags;
        this.dateWatched = dateWatched;
    }

    // Getters and setters for all fields
    /**
     * Returns the unique ID of the movie.
     * @return the movie ID
     */
    public int getId() { return id; }

    /**
     * Sets the ID of the movie.
     * @param id the movie ID
     */
    public void setId(int id) { this.id = id; }

    /**
     * Returns the title of the movie.
     * @return the movie title
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of the movie.
     * @param title the movie title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Returns the director of the movie.
     * @return the director's name
     */
    public String getDirector() { return director; }

    /**
     * Sets the director of the movie.
     * @param director the director's name
     */
    public void setDirector(String director) { this.director = director; }

    /**
     * Returns the release year of the movie.
     * @return the release year
     */
    public int getReleaseYear() { return releaseYear; }

    /**
     * Sets the release year of the movie.
     * @param releaseYear the year the movie was released
     */
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    /**
     * Returns the runtime of the movie in minutes.
     * @return the runtime in minutes
     */
    public int getRuntimeMinutes() { return runtimeMinutes; }

    /**
     * Sets the runtime of the movie in minutes.
     * @param runtimeMinutes the runtime in minutes
     */
    public void setRuntimeMinutes(int runtimeMinutes) { this.runtimeMinutes = runtimeMinutes; }

    /**
     * Returns the name of the streaming platform where the movie is available.
     * @return the platform name
     */
    public String getStreamingPlatform() { return streamingPlatform; }

    /**
     * Sets the name of the streaming platform where the movie is available.
     * @param streamingPlatform the platform name
     */
    public void setStreamingPlatform(String streamingPlatform) { this.streamingPlatform = streamingPlatform; }

    /**
     * Returns the rating of the movie.
     * @return the movie rating (0.0–10.0)
     */
    public double getRating() { return rating; }

    /**
     * Sets the rating of the movie.
     * @param rating the rating value (should be between 0.0 and 10.0)
     */
    public void setRating(double rating) { this.rating = rating; }

    /**
     * Returns the tags associated with the movie.
     * @return a comma-separated string of tags
     */
    public String getTags() { return tags; }

    /**
     * Sets the tags associated with the movie.
     * @param tags a comma-separated string of tags
     */
    public void setTags(String tags) { this.tags = tags; }

    /**
     * Returns the date the movie was watched.
     * @return the watched date as a string (MM-DD-YYYY format)
     */
    public String getDateWatched() { return dateWatched; }

    /**
     * Sets the date the movie was watched.
     * @param dateWatched the watched date (MM-DD-YYYY format)
     */
    public void setDateWatched(String dateWatched) { this.dateWatched = dateWatched; }
}
