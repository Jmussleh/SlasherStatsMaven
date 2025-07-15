package com.slasherstats.config;

import java.util.Scanner;
/**
 * Utility class for retrieving MySQL database configuration settings
 * from user input at runtime.
 */
public class DbConfig {

    /**
     * Prompts the user to enter the MySQL host, port, and database name,
     * and constructs a JDBC URL string.
     *
     * @return a formatted JDBC URL string for connecting to the MySQL database
     * @throws java.util.InputMismatchException if user input is not valid
     */
    public static String getJdbcUrl() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter MySQL host (e.g., localhost): ");
        String host = scanner.nextLine();

        System.out.print("Enter MySQL port (default 3306): ");
        String port = scanner.nextLine();

        System.out.print("Enter MySQL database name: ");
        String db = scanner.nextLine();

        return "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true";
    }

    /**
     * Prompts the user to enter their MySQL username.
     *
     * @return the entered MySQL username as a {@code String}
     */
    public static String getUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter MySQL username: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter their MySQL password.
     *
     * @return the entered MySQL password as a {@code String}
     */
    public static String getPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter MySQL password: ");
        return scanner.nextLine();
    }
}
