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
/**
 * Spring MVC controller for handling user interactions related to horror movies.
 * This controller manages movie listings, uploads, additions, deletions, updates, and searches.
 */
@Controller
public class MovieController {
    //Injects the slasherStatsManager service into this controller
    /** Service manager used to interact with the horror movie database. */
    @Autowired
    private slasherStatsManager manager;

    //Handles the default routing which adds all movies to the list and returns index.html
    /**
     * Handles the root ("/") GET request and loads the index page with current movies,
     * an empty movie form, and current account points.
     *
     * @param model the model object for passing data to the view
     * @return the "index" Thymeleaf template
     */
    @GetMapping
    public String index(Model model) {
        model.addAttribute("movies", manager.viewMovies());
        model.addAttribute("movie", new horrorMovie());
        model.addAttribute("accountPoints", manager.getAccountPoints());
        return "index";
    }

    //Handles the POST upload request when a movie file is uploaded
    /**
     * Handles uploading a file containing bulk movie entries.
     *
     * @param file  the uploaded file (CSV or TXT expected)
     * @param model the model object for returning errors if needed
     * @return redirect to "/" on success or return "index" view on error
     */
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
    /**
     * Handles adding a new movie manually via form input.
     *
     * @param movie the movie object submitted via form
     * @param model the model object (not used in this method)
     * @return redirect to the main movie list page
     */
    @PostMapping("/addMovie")
    //Form fields are connected to horrorMovie object
    public String addMovie(@ModelAttribute HorrorMovieSQL movie, Model model) {
        manager.addMovie(movie);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }

    /**
     * Deletes a movie using its title from the path variable.
     *
     * @param title the title of the movie to delete
     * @return redirect to the main page after deletion
     */
    @PostMapping("/delete/{title}")
    public String deleteMovie(@PathVariable String title) {
        //deletes the movie by title search
        manager.deleteMovie(title);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }

    /**
     * Searches for a movie by title and displays the delete confirmation page.
     *
     * @param title the title of the movie to find
     * @param model the model object for passing movie or error data
     * @return "confirmDelete" view if found, otherwise return to "index"
     */
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
    /**
     * Deletes a movie after user confirmation via form.
     *
     * @param title the title of the movie to delete
     * @return redirect to the main movie list page
     */
    @PostMapping("/deleteConfirmed")
    public String deleteConfirmed(@RequestParam String title) {
        manager.deleteMovie(title);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }

    //Shows update form for movie
    /**
     * Displays the movie update form for a specific title.
     *
     * @param title the title of the movie to update
     * @param model the model object for passing movie data
     * @return "updateMovie" view if movie is found, otherwise return to "index"
     */
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
    /**
     * Updates a movie based on form submission.
     *
     * @param movie the updated movie object
     * @return redirect to the main movie list page
     */
    @PostMapping("/updateMovie")
    public String updateMovie(@ModelAttribute HorrorMovieSQL movie) {
        //Uses this method to update and save the changes
        manager.updateMovie(movie);
        //Redirects to homepage and refreshes movie list
        return "redirect:/";
    }
}