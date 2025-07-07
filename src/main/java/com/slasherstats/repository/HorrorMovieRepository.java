package com.slasherstats.repository;

import com.slasherstats.model.HorrorMovieSQL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorrorMovieRepository extends JpaRepository<HorrorMovieSQL, Integer> {
    HorrorMovieSQL findByTitleIgnoreCase(String title);
}
