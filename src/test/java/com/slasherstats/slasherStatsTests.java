package com.slasherstats;
//There are also classes to write test data to a file and handle lists
import com.slasherstats.model.horrorMovie;
import com.slasherstats.service.slasherStatsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//Class used to initiate JUnit testing in intelliJ
public class slasherStatsTests {

    //Variable to declare what is being tested
    private slasherStatsManager appManager;

    //Runs before each test and creates a new instance of slasherStatsManager
    @BeforeEach
    public void setUp() {
        appManager = new slasherStatsManager();
    }

    @Test
    //Test case for successfully adding a movie
    public void testAddMovieSuccess() {
        //This creates a valid movie with all fields
        horrorMovie movie = new horrorMovie("Scream", "Wes Craven", 1996, 111, "HBO Max", 7.8, "slasher", "10-30-2021");
        boolean result = appManager.addMovie(movie);
        //Asserts that addMovie returned True
        assertTrue(result);
        assertEquals(1, appManager.viewMovies().size());
    }

    @Test
    //Test case for failure to add a movie
    public void testAddMovieFail() {
        //Does not add a movie and passes null
        boolean result = (appManager.addMovie(null));
        //Asserts that it will return false and not crash the program
        assertFalse(result);
    }

    @Test
    //Test case for successfully deleting a movie from the database
    public void testDeleteMovieSuccess() {
        //Adds a valid movie to the database
        horrorMovie movie = new horrorMovie("The Thing", "John Carpenter", 1982, 109, "Shudder", 8.2, "creature", "11-05-2020");
        appManager.addMovie(movie);
        //Prompts to delete the movie that was added
        boolean result = appManager.deleteMovie("The Thing");
        //Asserts that it will return True which means there is no movie in the database
        assertTrue(result);
        //Expects that the size of the movies list will be zero
        assertEquals(0, appManager.viewMovies().size());
    }

    @Test
    //Test case for failing to delete a movie from the database
    public void testDeleteMovieFail() {
        //Tries to delete a movie when there is no movie in the database
        boolean result = appManager.deleteMovie("Nonexistent");
        //Asserts that the result will be false because there is no movie to delete
        assertFalse(result);
    }
    //Test case for successfully updating a movie field
    @Test
    public void testUpdateMovieField_Success() {
        //Adds a valid movie to the database
        horrorMovie movie = new horrorMovie("Halloween", "John Carpenter", 1978, 91, "Shudder", 7.9, "slasher", "10-31-2022");
        appManager.addMovie(movie);

        // Chooses to update the director field
        boolean updatedDirector = appManager.updateMovieField("Halloween", 2, "Rob Zombie");
        assertTrue(updatedDirector);
        //Expects that the new update for director will be Rob Zombie
        assertEquals("Rob Zombie", appManager.viewMovies().get(0).getDirector());

        // Chooses to update rating
        boolean updatedRating = appManager.updateMovieField("Halloween", 6, "8.5");
        assertTrue(updatedRating);
        //Expects that the new updated rating will reflect 8.5
        assertEquals(8.5, appManager.viewMovies().get(0).getRating());
    }
    //Test case for failure to update a movie field
    @Test
    public void testUpdateMovieField_Failure() {
        //Adds a new movie to the database
        horrorMovie movie = new horrorMovie("Halloween", "John Carpenter", 1978, 91, "Shudder", 7.9, "slasher", "10-31-2022");
        appManager.addMovie(movie);

        //Tries to update a movie that is not in the database
        boolean updateNonexistent = appManager.updateMovieField("Ghostbusters", 2, "New Director");
        assertFalse(updateNonexistent);

        //Tries to set an invalid rating
        boolean invalidRating = appManager.updateMovieField("Halloween", 6, "12.0");
        assertFalse(invalidRating);

        //Tries to set an invalid date
        boolean invalidDate = appManager.updateMovieField("Halloween", 8, "13-45-2020");
        assertFalse(invalidDate);
    }

    @Test
    //Test case for successfully adding a bulk movie file
    public void testbulkMoviesSuccess() throws Exception {
        //Looks for a test .txt file
        String filename = "test_bulk_movies.txt";
        //Writes a test file with two movies added
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("It Follows,David Robert Mitchell,2014,100,Netflix,6.8,supernatural,10-29-2021");
            writer.println("The Babadook,Jennifer Kent,2014,94,Hulu,6.9,psychological,10-30-2021");
        }
        //Adds the movies to the list
        List<horrorMovie> added = appManager.addBulkMovies(filename);
        //Expects that there will be two movies in the database
        assertEquals(2, added.size());
    }

    @Test
    //Test case for failure to add a bulk movie file
    public void testbulkMoviesFailure() {
        //Tries to add a file that does not exist
        List<horrorMovie> added = appManager.addBulkMovies("randomfile.txt");
        //Expects that the size of the database will be zero because no movies were successfully added
        assertEquals(0, added.size());
    }

    @Test
    //Test case for correctly adding points to a user account when a movie is added to the database
    public void testAccountPointsAdd() {
        //Expects that accountPoints will start with 0
        assertEquals(0, appManager.getAccountPoints());
        //Adds a movie
        appManager.addMovie(new horrorMovie("Hereditary", "Ari Aster", 2018, 127, "Max", 7.3, "supernatural", "10-15-2022"));
        //Expects that account points will be at 10
        assertEquals(10, appManager.getAccountPoints());
        //Adds a second movie
        appManager.addMovie(new horrorMovie("Midsommar", "Ari Aster", 2019, 148, "Hulu", 7.1, "folk", "10-16-2022"));
        //Expects accountPoints to equal 20
        assertEquals(20, appManager.getAccountPoints());
        //Deletes a movie
        appManager.deleteMovie("Hereditary");
        //Expects accountPoints to be back at 10
        assertEquals(10, appManager.getAccountPoints());
    }

    @Test
    //Test case for removing points from a user account when a movie is deleted from the database
    public void testAccountPointsSubtract() {
        //Add a random movie
        appManager.addMovie(new horrorMovie("Movie", "Director", 2025, 100, "Platform", 5.0, "tag", "06-25-2025"));
        //Delete the random movie that was added
        appManager.deleteMovie("Movie");
        //Expects that accountPoints will equal zero
        assertEquals(0, appManager.getAccountPoints());
        //Attempts to delete a movie that does not exist
        appManager.deleteMovie("Nonexistent");
        //Expects that accountPoints will equal zero
        assertEquals(0, appManager.getAccountPoints());
    }
}