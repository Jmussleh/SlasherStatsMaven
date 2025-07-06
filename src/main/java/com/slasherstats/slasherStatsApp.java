package com.slasherstats;
import com.slasherstats.service.slasherStatsManager;
import com.slasherstats.model.horrorMovie;
import java.util.Scanner;
import java.util.List;

//Declares the main application class
public class slasherStatsApp {
    //Reads user input from the console
    private Scanner scanner;
    //Manages CRUD operations. Logic handler.
    private slasherStatsManager appManager;

    //Initializes scanner and appManager when an object is created
    public slasherStatsApp() {
        this.scanner = new Scanner(System.in);
        this.appManager = new slasherStatsManager();
    }

    //Loop that the main app runs in
    public void run() {
        //Starts the application and continues to run while it remains true
        boolean running = true;
        //Displays the main application menu and waits for user input
        while (running) {
            displayMenu();
            String input = scanner.nextLine();
            //Does certain operations based on user input
            switch (input) {
                //Adds a movie with information entered manually
                case "1":
                    horrorMovie newMovie = movieFields();
                    if (newMovie != null) {
                        appManager.addMovie(newMovie);
                        System.out.println("Horror movie added...");
                    }
                    break;
                //Adds movies from a bulk .txt file
                case "2":
                    System.out.print("Enter filename: ");
                    String filename = scanner.nextLine();
                    List<horrorMovie> bulkAdded = appManager.addBulkMovies(filename); // call local file method
                    System.out.println("Added " + bulkAdded.size() + " movies.");
                    break;
                //Allows user to view all movies in the database
                case "3":
                    List<horrorMovie> allMovies = appManager.viewMovies();
                    //If database is empty, print this line
                    if (allMovies.isEmpty()) {
                        System.out.println("No movies in the database.");
                    //If there are movies print each and their fields
                    } else {
                        allMovies.forEach(System.out::println);
                    }
                    break;
                //Allows user to update any field of a movie in the database. Asks user for title of movie to update.
                case "4":
                    System.out.print("Enter the title of the movie you want to update: ");
                    String titleToUpdate = scanner.nextLine();
                    //Prompts the user to choose which field to update
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
                    //Prompts the user to enter the valur for the field to update
                    System.out.print("Enter the new field: ");
                    //Reads the new value from user input
                    String newValue = scanner.nextLine();
                    //Updates the selected field to the new value
                    boolean updateSuccess = appManager.updateMovieField(titleToUpdate, field, newValue);
                    //Prints a message depending on update success or failure
                    System.out.println(updateSuccess ? "Movie updated." : "Update failed.");
                    break;
                case "5":
                    System.out.print("Enter title of movie to delete: ");
                    String deleteTitle = scanner.nextLine();
                    boolean deleted = appManager.deleteMovie(deleteTitle);
                    //Shows either message which shows success or failure
                    System.out.println(deleted ? "Movie deleted." : "Movie not found.");
                    break;
                case "6":
                    //Gets account points and displays them with a message
                    System.out.println("Account points: " + appManager.getAccountPoints());
                    break;
                case "7":
                    //Sets running to false and shuts down the program due to user input
                    running = false;
                    System.out.println("System is powering down...");
                    break;
                //If none of these cases are met the input is invalid
                default:
                    System.out.println("Invalid input");
            }
        }
    }
    //Method to display the Slasher stats app in the CLI
    private void displayMenu() {
        System.out.println("\n---*Welcome to the SlasherStats App*---");
        System.out.println("1. Create Horror Movie Entry");
        System.out.println("2. Add bulk Horror Movie Entries");
        System.out.println("3. resources.View All Movies");
        System.out.println("4. Update Movie Entry");
        System.out.println("5. Delete Movie Entry");
        System.out.println("6. resources.View Account Points");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }
    //Gathers movie fields from user input
    private horrorMovie movieFields() {
        try {
            //Prompts user to enter the movie title
            System.out.print("Enter movie title: ");
            String title = scanner.nextLine();
            //Prompts the user to enter a movie director
            System.out.print("Enter movie director: ");
            String director = scanner.nextLine();
            //Prompts the user to enter the year of release
            System.out.print("Enter year of release: ");
            int year = Integer.parseInt(scanner.nextLine());
            //Prompts the user to enter the total runtime in minutes
            System.out.print("Enter runtime minutes: ");
            int runtimeMinutes = Integer.parseInt(scanner.nextLine());
            //Prompts the user to enter the streaming platform
            System.out.print("Enter streaming platform: ");
            String platform = scanner.nextLine();
            //Asks for a rating between 0.0 and 10.0
            System.out.print("Enter rating (0.0 - 10.0): ");
            double rating = Double.parseDouble(scanner.nextLine());
            if (rating < 0.0 || rating > 10.0) {
                System.out.println("Invalid rating range.");
                return null;
            }
            //Prompts the user to enter tags associated with the movie
            System.out.print("Enter tags: ");
            String tags = scanner.nextLine();
            //Prompts the user to enter the date watched in the valid mm-dd-yyyy format
            System.out.print("Enter date watched (MM-DD-YYYY): ");
            String date = scanner.nextLine();
            if (!slasherStatsManager.isValidDate(date)) {
                System.out.println("Invalid date format.");
                return null;
            }
            //Returns the new object
            return new horrorMovie(title, director, year, runtimeMinutes, platform, rating, tags, date);
            //If the conditions aren't met for each field then print this message
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return null;
        }
    }
    //Creates an instance of the application and calls the run() loop
    public static void main(String[] args) {
        new slasherStatsApp().run();
    }
}


