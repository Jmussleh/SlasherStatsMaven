package com.slasherstats.repository;

import com.slasherstats.model.HorrorMovieSQL;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and managing {@link HorrorMovieSQL} entities.
 */
public interface HorrorMovieRepository extends JpaRepository<HorrorMovieSQL, Integer> {
    /**
     * Retrieves a horror movie by its title, ignoring case sensitivity.
     *
     * @param title the title of the movie to find
     * @return the {@link HorrorMovieSQL} object if found, or {@code null} if not found
     */
    HorrorMovieSQL findByTitleIgnoreCase(String title);
}
