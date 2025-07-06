//This class can be instantiated for a horror movie object. Also contains
//all getters and setters for this object.
package com.slasherstats.model;
public class horrorMovie {
    //All fields for my horror movie object.
    private String title;
    private String director;
    private int releaseYear;
    private int runtimeMinutes;
    private String streamingPlatform;
    private double rating;
    private String tags;
    private String dateWatched;

    //Constructor
    public horrorMovie(String title, String director, int releaseYear, int runtimeMinutes, String streamingPlatform, double rating, String tags, String dateWatched) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runtimeMinutes = runtimeMinutes;
        this.streamingPlatform = streamingPlatform;
        this.rating = rating;
        this.tags = tags;
        this.dateWatched = dateWatched;
    }
    public horrorMovie() {
        // empty constructor - needed for frameworks like Spring MVC
    }
    //Getters for all fields in my horror movie object
    public String getTitle() {
        return title;
    }
    public String getDirector() {
        return director;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }
    public String getStreamingPlatform() {
        return streamingPlatform;
    }
    public double getRating() {return rating;}
    public String getTags() {
        return tags;
    }
    public String getDateWatched() {
        return dateWatched;
    }

    //Setters for all fields in my horror movie object
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
    public void setRuntimeMinutes(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }
    public void setStreamingPlatform(String streamingPlatform) {
        this.streamingPlatform = streamingPlatform;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setDateWatched(String dateWatched) {
        this.dateWatched = dateWatched;
    }

    //Override the toString to show data in a certain manner
    public String toString() {return "\nTitle: " + title + "\nDirector: " + director + "\nRelease Year: " + releaseYear + "\nRuntime Minutes: "
            + runtimeMinutes + "\nStreaming Platform: " + streamingPlatform + "\nRating: " + rating + "\nTags: " + tags + "\nDate Watched: " + dateWatched;}
}
