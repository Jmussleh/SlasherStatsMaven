package com.slasherstats.model;

import jakarta.persistence.*;

@Entity
@Table(name = "horror_movies")
public class HorrorMovieSQL {
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

    public HorrorMovieSQL() {
        // Default constructor for JPA
    }

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
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public String getDateWatched() { return dateWatched; }
    public void setDateWatched(String dateWatched) { this.dateWatched = dateWatched; }
}
