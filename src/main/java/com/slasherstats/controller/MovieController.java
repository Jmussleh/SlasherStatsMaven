//Declares class is part of this package
package com.slasherstats.controller;
//imports horrorMovie and slasherStatsManager
import com.slasherstats.model.HorrorMovieSQL;
import com.slasherstats.model.horrorMovie;
import com.slasherstats.service.slasherStatsManager;
//Used for Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//Tells Spring to treat this class as the controller
@Controller
public class MovieController {
    //Injects the slasherStatsManager service into this controller
    @Autowired
    private slasherStatsManager manager;

    //Handles the default routing which adds all movies to the list and returns index.html
    @GetMapping
    public String index(Model model) {
        model.addAttribute("movies", manager.viewMovies());
        model.addAttribute("movie", new horrorMovie());
        model.addAttribute("accountPoints", manager.getAccountPoints());
        return "index";
    }

    //Handles the POST upload request when a movie file is uploaded
    @PostMapping("/upload")
    public String uploadMovies(@RequestParam("file") MultipartFile file, Model model) {
        //If no file is uploaded return an error. Shows index.html again.
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select a file to upload");
            model.addAttribute("movies", manager.viewMovies());
            return "index";
        }
        //Reads and processes the text file
        try {
            manager.addBulkMovies(file); // Use the MultipartFile version of the method
        //If an error occurs display the error and return home
        } catch (Exception e) {
            model.addAttribute("error", "Failed to upload file: " + e.getMessage());
            model.addAttribute("movies", manager.viewMovies());
            //Returns index.html
            return "index";
        }
        //Refreshes the movie list
        return "redirect:/";
    }

    //Adds a single movie manually
    @PostMapping("/addMovie")
    //Form fields are connected to horrorMovie object
    public String addMovie(@ModelAttribute HorrorMovieSQL movie, Model model) {
        manager.addMovie(movie);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }

    @PostMapping("/delete/{title}")
    public String deleteMovie(@PathVariable String title) {
        //deletes the movie by title search
        manager.deleteMovie(title);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }

    @GetMapping("/searchToDelete")
    public String searchToDelete(@RequestParam String title, Model model) {
        HorrorMovieSQL movie = manager.findMovie(title);
        //If the movie isn't found return an error and return home
        if (movie == null) {
            model.addAttribute("error", "Movie not found");
            model.addAttribute("movies", manager.viewMovies());
            return "index";
        }
        //If the movie is found return confirmDelete.html
        model.addAttribute("movie", movie);
        return "confirmDelete";
    }
    //Deletes the title after the user confirms the deletion. Returns home after deletion
    @PostMapping("/deleteConfirmed")
    public String deleteConfirmed(@RequestParam String title) {
        manager.deleteMovie(title);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }
    //Shows update form for movie
    @GetMapping("/update")
    public String showUpdateForm(@RequestParam String title, Model model) {
        //Finds the movie by title
        HorrorMovieSQL movie = manager.findMovie(title);
        //If the movie isn;t found go to home page with an error
        if (movie == null) {
            model.addAttribute("error", "Movie not found");
            model.addAttribute("movies", manager.viewMovies());
            return "index";
        }
        //If the movie is found show the updateMovie.html form
        model.addAttribute("movie", movie);
        return "updateMovie";
    }

    //Accepts the update form
    @PostMapping("/updateMovie")
    public String updateMovie(@ModelAttribute HorrorMovieSQL movie) {
        //Uses this method to update and save the changes
        manager.updateMovie(movie);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }
}