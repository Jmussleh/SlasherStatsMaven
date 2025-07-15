package com.slasherstats;

import com.slasherstats.model.HorrorMovieSQL;
import com.slasherstats.repository.HorrorMovieRepository;
import com.slasherstats.service.slasherStatsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for the {@link slasherStatsManager} service using mocked {@link HorrorMovieRepository}.
 */
public class slasherStatsTests {

    private HorrorMovieRepository mockRepository;
    private slasherStatsManager appManager;

    /**
     * Sets up a mocked HorrorMovieRepository and injects it into the service before each test.
     */
    @BeforeEach
    public void setUp() {
        mockRepository = mock(HorrorMovieRepository.class);
        appManager = new slasherStatsManager(mockRepository);
    }

    /**
     * Tests that a valid movie is added successfully and saved in the repository.
     */
    @Test
    public void testAddMovieSuccess() {
        HorrorMovieSQL movie = new HorrorMovieSQL("Scream", "Wes Craven", 1996, 111, "HBO Max", 7.8, "slasher", "10-30-2021");
        boolean result = appManager.addMovie(movie);
        verify(mockRepository, times(1)).save(movie);
        assertTrue(result);
    }


    /**
     * Tests that attempting to add a null movie fails and returns false.
     */
    @Test
    public void testAddMovieFail() {
        boolean result = appManager.addMovie(null);
        assertFalse(result);
    }

    /**
     * Tests successful deletion of a movie that exists in the repository.
     */
    @Test
    public void testDeleteMovieSuccess() {
        HorrorMovieSQL movie = new HorrorMovieSQL("The Thing", "John Carpenter", 1982, 109, "Shudder", 8.2, "creature", "11-05-2020");
        when(mockRepository.findByTitleIgnoreCase("The Thing")).thenReturn(movie);

        boolean result = appManager.deleteMovie("The Thing");
        verify(mockRepository, times(1)).delete(movie);
        assertTrue(result);
    }

    /**
     * Tests that deleting a nonexistent movie returns false and does not affect the repository.
     */
    @Test
    public void testDeleteMovieFail() {
        when(mockRepository.findByTitleIgnoreCase("Nonexistent")).thenReturn(null);
        boolean result = appManager.deleteMovie("Nonexistent");
        assertFalse(result);
    }

    /**
     * Tests bulk uploading movies from a valid test file.
     *
     * @throws Exception if writing the test file fails
     */
    @Test
    public void testBulkMoviesSuccess() throws Exception {
        String filename = "test_bulk_movies.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("It Follows,David Robert Mitchell,2014,100,Netflix,6.8,supernatural,10-29-2021");
            writer.println("The Babadook,Jennifer Kent,2014,94,Hulu,6.9,psychological,10-30-2021");
        }
        List<HorrorMovieSQL> added = appManager.addBulkMovies(filename);
        assertEquals(2, added.size());
    }

    /**
     * Tests that uploading from a non-existent or invalid file returns an empty list.
     */
    @Test
    public void testBulkMoviesFailure() {
        List<HorrorMovieSQL> added = appManager.addBulkMovies("randomfile.txt");
        assertEquals(0, added.size());
    }

    /**
     * Tests that account points increase correctly when movies are added,
     * and decrease when a movie is deleted.
     */
    @Test
    public void testAccountPointsAddAndSubtract() {
        HorrorMovieSQL movie1 = new HorrorMovieSQL("Hereditary", "Ari Aster", 2018, 127, "Max", 7.3, "supernatural", "10-15-2022");
        HorrorMovieSQL movie2 = new HorrorMovieSQL("Midsommar", "Ari Aster", 2019, 148, "Hulu", 7.1, "folk", "10-16-2022");

        when(mockRepository.findByTitleIgnoreCase("Hereditary")).thenReturn(movie1);
        when(mockRepository.findByTitleIgnoreCase("Midsommar")).thenReturn(movie2);

        assertEquals(0, appManager.getAccountPoints());

        appManager.addMovie(movie1);
        assertEquals(10, appManager.getAccountPoints());

        appManager.addMovie(movie2);
        assertEquals(20, appManager.getAccountPoints());

        appManager.deleteMovie("Hereditary");
        assertEquals(10, appManager.getAccountPoints());
    }

    /**
     * Tests that account points are decremented only when a movie is found and deleted.
     */
    @Test
    public void testAccountPointsSubtract() {
        HorrorMovieSQL movie = new HorrorMovieSQL("Movie", "Director", 2025, 100, "Platform", 5.0, "tag", "06-25-2025");
        when(mockRepository.findByTitleIgnoreCase("Movie")).thenReturn(movie);

        appManager.addMovie(movie);
        assertEquals(10, appManager.getAccountPoints());

        appManager.deleteMovie("Movie");
        assertEquals(0, appManager.getAccountPoints());

        when(mockRepository.findByTitleIgnoreCase("Nonexistent")).thenReturn(null);
        appManager.deleteMovie("Nonexistent");
        assertEquals(0, appManager.getAccountPoints());
    }
}
