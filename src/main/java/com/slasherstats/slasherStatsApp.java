//Declares the package
package com.slasherstats;

import com.slasherstats.model.HorrorMovieSQL;
import com.slasherstats.repository.HorrorMovieRepository;
import com.slasherstats.service.slasherStatsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

//This class is marked as a spring bean so it can be discovered by spring.
//This class will run a method when the app starts.
@Component
public class slasherStatsApp implements CommandLineRunner {
    //Used to access the CRUD operations and reading input from the user console.
    private final slasherStatsManager appManager;
    private final Scanner scanner;
    //Sccanner is initialized.
    @Autowired
    public slasherStatsApp(slasherStatsManager appManager) {
        this.appManager = appManager;
        this.scanner = new Scanner(System.in);
    }
    //Method is run once the application starts
    @Override
    public void run(String... args) {
        //Begins a loop until terminated by the user
        boolean running = true;
        while (running) {
            //Displays the menu in the CLI
            displayMenu();
            String input = scanner.nextLine();
            //Will choose a case based on user choice
            switch (input) {
                //Add a single movie
                case "1":
                    HorrorMovieSQL newMovie = movieFields();
                    if (newMovie != null) {
                        appManager.addMovie(newMovie);
                        System.out.println("Horror movie added...");
                    }
                    break;
                //Add a txt file of bulk movies
                case "2":
                    System.out.print("Enter filename: ");
                    String filename = scanner.nextLine();
                    List<HorrorMovieSQL> bulkAdded = appManager.addBulkMovies(filename);
                    System.out.println("Added " + bulkAdded.size() + " movies.");
                    break;
                //View all movies
                case "3":
                    List<HorrorMovieSQL> allMovies = appManager.viewMovies();
                    if (allMovies.isEmpty()) {
                        System.out.println("No movies in the database.");
                    } else {
                        allMovies.forEach(System.out::println);
                    }
                    break;
                //Update a movie. Any field is able to be updated.
                case "4":
                    System.out.print("Enter the title of the movie you want to update: ");
                    String titleToUpdate = scanner.nextLine();
                    HorrorMovieSQL existing = appManager.findMovie(titleToUpdate);
                    //If the movie searched for is not found return this messaged.
                    if (existing == null) {
                        System.out.println("Movie not found.");
                        break;
                    }
                    //Choices to be printed in the CLI
                    System.out.println("Select the field to update:");
                    System.out.println("1. Title");
                    System.out.println("2. Director");
                    System.out.println("3. Release Year");
                    System.out.println("4. Runtime Minutes");
                    System.out.println("5. Streaming Platform");
                    System.out.println("6. Rating");
                    System.out.println("7. Tags");
                    System.out.println("8. Date Watched");
                    System.out.print("Enter choice: ");
                    int field = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter the new field: ");
                    //Scans user input
                    String newValue = scanner.nextLine();
                    switch (field) {
                        case 1 -> existing.setTitle(newValue);
                        case 2 -> existing.setDirector(newValue);
                        case 3 -> existing.setReleaseYear(Integer.parseInt(newValue));
                        case 4 -> existing.setRuntimeMinutes(Integer.parseInt(newValue));
                        case 5 -> existing.setStreamingPlatform(newValue);
                        case 6 -> existing.setRating(Double.parseDouble(newValue));
                        case 7 -> existing.setTags(newValue);
                        case 8 -> existing.setDateWatched(newValue);
                        //If any inputs are invalid show this message
                        default -> {
                            System.out.println("Invalid field.");
                            continue;
                        }
                    }
                    boolean updateFieldSuccess = appManager.updateMovie(existing);
                    System.out.println(updateFieldSuccess ? "Movie updated." : "Update failed.");
                    break;
                //For movie to be deleted by searching for title
                case "5":
                    System.out.print("Enter title of movie to delete: ");
                    //Scans user input
                    String deleteTitle = scanner.nextLine();
                    boolean deleted = appManager.deleteMovie(deleteTitle);
                    //Prints a message depending on if movie is found or not.
                    System.out.println(deleted ? "Movie deleted." : "Movie not found.");
                    break;
                //Show account points
                case "6":
                    System.out.println("Account points: " + appManager.getAccountPoints());
                    break;
                //Sets running to false and closes the application.
                case "7":
                    running = false;
                    System.out.println("System is powering down...");
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
    //The SlasherStats app menu displayed in the CLI when the app runs
    private void displayMenu() {
        System.out.println("\n---*Welcome to the SlasherStats App*---");
        System.out.println("1. Create Horror Movie Entry");
        System.out.println("2. Add bulk Horror Movie Entries");
        System.out.println("3. View All Movies");
        System.out.println("4. Update Movie Entry");
        System.out.println("5. Delete Movie Entry");
        System.out.println("6. View Account Points");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }
    //Fields that are shown for user to input when adding a single movie.
    private HorrorMovieSQL movieFields() {
        try {
            System.out.print("Enter movie title: ");
            String title = scanner.nextLine();
            System.out.print("Enter movie director: ");
            String director = scanner.nextLine();
            System.out.print("Enter year of release: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter runtime minutes: ");
            int runtimeMinutes = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter streaming platform: ");
            String platform = scanner.nextLine();
            System.out.print("Enter rating (0.0 - 10.0): ");
            //Input validation for rating. Must be between 0.0 and 10.0 or get a message.
            double rating = Double.parseDouble(scanner.nextLine());
            if (rating < 0.0 || rating > 10.0) {
                System.out.println("Invalid rating range.");
                return null;
            }
            System.out.print("Enter tags: ");
            String tags = scanner.nextLine();
            System.out.print("Enter date watched (MM-DD-YYYY): ");
            //Input validation for date. If date is incorrect show this message.
            String date = scanner.nextLine();
            if (!slasherStatsManager.isValidDate(date)) {
                System.out.println("Invalid date format.");
                return null;
            }
            //Creates a horrormoviesql object if all fields are valid.
            HorrorMovieSQL movie = new HorrorMovieSQL();
            movie.setTitle(title);
            movie.setDirector(director);
            movie.setReleaseYear(year);
            movie.setRuntimeMinutes(runtimeMinutes);
            movie.setStreamingPlatform(platform);
            movie.setRating(rating);
            movie.setTags(tags);
            movie.setDateWatched(date);

            return movie;
        //If there is an invalid field this message is shown.
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return null;
        }
    }
}
