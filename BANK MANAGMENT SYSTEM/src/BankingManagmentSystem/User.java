package BankingManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    // Instance variables to hold the connection to the database and a Scanner object
    private Connection connection;
    private Scanner scanner;

    // Constructor to initialize the connection and scanner
    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to register a new user
    public void register() {
        // Clear the scanner buffer
        scanner.nextLine();

        // Prompt the user to enter their full name
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();

        // Prompt the user to enter their email
        System.out.print("Email: ");
        String email = scanner.nextLine();

        // Prompt the user to enter their password
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Check if a user with the given email already exists
        if (user_exist(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }

        // Query to insert a new user into the User table
        String register_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?)";

        try {
            // Prepare the query with the user's details
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            // Execute the query and check if the insertion was successful
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Registration Successful!");
            } else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to login a user
    public String login() {
        // Clear the scanner buffer
        scanner.nextLine();

        // Prompt the user to enter their email
        System.out.print("Email: ");
        String email = scanner.nextLine();

        // Prompt the user to enter their password
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Query to check if the email and password match a user in the User table
        String login_query = "SELECT * FROM User WHERE email = ? AND password = ?";

        try {
            // Prepare the query with the provided email and password
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query and check if a matching user exists
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // If a match is found, return the email
                return email;
            } else {
                // If no match is found, return null
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If an exception occurs, return null
        return null;
    }

    // Method to check if a user with the given email already exists
    public boolean user_exist(String email) {
        // Query to check if an email already exists in the User table
        String query = "SELECT * FROM user WHERE email = ?";

        try {
            // Prepare the query with the provided email
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a matching email is found, return true
            if (resultSet.next()) {
                return true;
            } else {
                // If no matching email is found, return false
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If an exception occurs, return false
        return false;
    }
}
