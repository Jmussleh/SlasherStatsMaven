package com.slasherstats;

import com.slasherstats.model.HorrorMovieSQL;
import com.slasherstats.repository.HorrorMovieRepository;
import com.slasherstats.service.slasherStatsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link slasherStatsManager} service using mocked {@link HorrorMovieRepository}.
 */
public class slasherStatsTests {

    private HorrorMovieRepository mockRepository;
    private slasherStatsManager appManager;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @BeforeEach
    public void setUp() {
        mockRepository = mock(HorrorMovieRepository.class);
        appManager = new slasherStatsManager(mockRepository);
    }
    //Unit test for add movie success
    @Test
    public void testAddMovieSuccess() {
        LocalDate dateWatched = LocalDate.parse("10-30-2021", formatter);
        HorrorMovieSQL movie = new HorrorMovieSQL("Scream", "Wes Craven", 1996, 111, "HBO Max", 7.8, "slasher", dateWatched);
        boolean result = appManager.addMovie(movie);
        verify(mockRepository, times(1)).save(movie);
        assertTrue(result);
    }
    //Unit test for add movie fail
    @Test
    public void testAddMovieFail() {
        boolean result = appManager.addMovie(null);
        assertFalse(result);
    }
    //Unit test for delete movie success
    @Test
    public void testDeleteMovieSuccess() {
        LocalDate dateWatched = LocalDate.parse("11-05-2020", formatter);
        HorrorMovieSQL movie = new HorrorMovieSQL("The Thing", "John Carpenter", 1982, 109, "Shudder", 8.2, "creature", dateWatched);
        when(mockRepository.findByTitleIgnoreCase("The Thing")).thenReturn(movie);

        boolean result = appManager.deleteMovie("The Thing");
        verify(mockRepository, times(1)).delete(movie);
        assertTrue(result);
    }
    //Unit test for delete movie fail
    @Test
    public void testDeleteMovieFail() {
        when(mockRepository.findByTitleIgnoreCase("Nonexistent")).thenReturn(null);
        boolean result = appManager.deleteMovie("Nonexistent");
        assertFalse(result);
    }
    //Unit test for testing successful bulk movie add
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
    //Unit test for bulk movie add failure
    @Test
    public void testBulkMoviesFailure() {
        List<HorrorMovieSQL> added = appManager.addBulkMovies("randomfile.txt");
        assertEquals(0, added.size());
    }
    //Unit test for adding and subtraction account points
    @Test
    public void testAccountPointsAddAndSubtract() {
        LocalDate date1 = LocalDate.parse("10-15-2022", formatter);
        LocalDate date2 = LocalDate.parse("10-16-2022", formatter);
        HorrorMovieSQL movie1 = new HorrorMovieSQL("Hereditary", "Ari Aster", 2018, 127, "Max", 7.3, "supernatural", date1);
        HorrorMovieSQL movie2 = new HorrorMovieSQL("Midsommar", "Ari Aster", 2019, 148, "Hulu", 7.1, "folk", date2);

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
    //Unit test for adding and subtraction account points
    @Test
    public void testAccountPointsSubtract() {
        LocalDate dateWatched = LocalDate.parse("06-25-2025", formatter);
        HorrorMovieSQL movie = new HorrorMovieSQL("Movie", "Director", 2025, 100, "Platform", 5.0, "tag", dateWatched);
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