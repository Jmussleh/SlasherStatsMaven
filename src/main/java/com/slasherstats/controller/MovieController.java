//Declares class is part of this package
package com.slasherstats.controller;
//imports horrorMovie and slasherStatsManager
import com.slasherstats.model.HorrorMovieSQL;
import com.slasherstats.model.horrorMovie;
import com.slasherstats.service.slasherStatsManager;
//Used for Spring
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        manager.recalculatePoints(); // Add this
        model.addAttribute("movies", manager.viewMovies());
        model.addAttribute("movie", new horrorMovie());
        model.addAttribute("accountPoints", manager.getAccountPoints());
        return "index";
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
    public String addMovie(@Valid @ModelAttribute("movie") HorrorMovieSQL movie,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movies", manager.viewMovies());
            model.addAttribute("accountPoints", manager.getAccountPoints());
            return "index";
        }

        manager.addMovie(movie);
        return "redirect:/";
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

    /**
     * Deletes a movie using its title from the path variable.
     *
     * @param title the title of the movie to delete
     * @return redirect to the main page after deletion
     */
    @GetMapping("/delete")
    public String deleteMovie(@RequestParam("title") String title, Model model) {
        HorrorMovieSQL movie = manager.findMovie(title);

        if (movie == null) {
            model.addAttribute("movies", manager.viewMovies());
            model.addAttribute("movie", new HorrorMovieSQL());
            model.addAttribute("error", "Movie with title '" + title + "' not found.");

            return "index";
        }

        manager.deleteMovie(movie.getTitle());
        model.addAttribute("message", "Movie deleted successfully.");
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
        HorrorMovieSQL found = manager.findMovie(title);

        if (found == null) {
            model.addAttribute("deleteError", "Movie with title '" + title + "' not found.");
            model.addAttribute("movies", manager.viewMovies());
            model.addAttribute("movie", new HorrorMovieSQL()); // prevent Thymeleaf binding error
            return "index";
        }

        manager.deleteMovie(title);
        return "redirect:/";
    }

    //Deletes the title after the user confirms the deletion. Returns home after deletion
    /**
     * Deletes a movie after user confirmation via form.
     *
     * @param title the title of the movie to delete
     * @return redirect to the main movie list page
     */
    @PostMapping("/deleteConfirmed")
    public String deleteConfirmed(@RequestParam String title, Model model) {
        HorrorMovieSQL movie = manager.findMovie(title);
        if (movie == null) {
            model.addAttribute("error", "Movie with title '" + title + "' not found.");
            model.addAttribute("movies", manager.viewMovies());
            return "index";
        }

        manager.deleteMovie(title);
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
        HorrorMovieSQL movie = manager.findMovie(title);
        if (movie == null) {
            model.addAttribute("updateError", "Movie with title '" + title + "' not found.");
            model.addAttribute("movies", manager.viewMovies());

            model.addAttribute("movie", new HorrorMovieSQL());

            return "index";
        }
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
    @PostMapping("/update")
    public String processUpdate(@ModelAttribute("movie") HorrorMovieSQL movie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Binding errors:");
            result.getFieldErrors().forEach(e -> {
                System.out.printf(" - %s: %s%n", e.getField(), e.getDefaultMessage());
            });
        }

        boolean success = manager.updateMovie(movie);

        if (!success) {
            model.addAttribute("error", "Update failed. Check required fields and formatting.");
            model.addAttribute("movie", movie);
            return "updateMovie";
        }

        return "redirect:/";
    }


    //Converts certain fields before processing
    /**
     * Initializes a custom data binder for web requests.
     * <p>
     * This method registers a custom editor to handle the conversion of
     * date strings in the format {@code MM-DD-YYYY} into Localdate
     * objects. If the input string cannot be parsed into a valid date,
     * the value is set to {@code null}, which allows validation annotations
     * such as {@code @NotNull} to trigger appropriate error messages.
     *
     * @param binder allows customization of data binding for web requests
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //Tells the program how the date is formatted
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Converts LocalDate with this logic. String to LocalDate.
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    //Sets LocalDate value if parsing succeeds
                    setValue(LocalDate.parse(text, formatter));
                    //If the date is incorrect it will throw an error.
                } catch (DateTimeParseException e) {
                    // Invalid date will set null
                    setValue(null);
                }
            }
        });
    }
}