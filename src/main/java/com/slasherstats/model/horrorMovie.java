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
    //empty constructor - needed for frameworks like Spring MVC
    public horrorMovie() {
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

    //Setter for all required fields in my horror movie object
    public void setTitle(String title) {
        this.title = title;
    }

    //Override the toString to show data in a certain manner
    public String toString() {return "\nTitle: " + title + "\nDirector: " + director + "\nRelease Year: " + releaseYear + "\nRuntime Minutes: "
            + runtimeMinutes + "\nStreaming Platform: " + streamingPlatform + "\nRating: " + rating + "\nTags: " + tags + "\nDate Watched: " + dateWatched;}
}
