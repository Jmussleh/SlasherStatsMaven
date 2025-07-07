package com.slasherstats.config;

import java.util.Scanner;

public class DbConfig {
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

    public static String getUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter MySQL username: ");
        return scanner.nextLine();
    }

    public static String getPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter MySQL password: ");
        return scanner.nextLine();
    }
}
