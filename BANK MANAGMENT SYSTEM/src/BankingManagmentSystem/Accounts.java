//================= Defines the package name=======================//
package BankingManagmentSystem;
import java.sql.*;
import java.util.Scanner;

public class Accounts {
    // Instance variables to hold the connection to the database and a Scanner object
    private Connection connection;
    private Scanner scanner;

    // Constructor to initialize the connection and scanner
    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to open a new account
    public long open_account(String email) {
        // Check if an account already exists for the given email
        if (!account_exist(email)) {
            // SQL query to insert a new account into the Accounts table
            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";

            // Clear the scanner buffer
            scanner.nextLine();

            // Prompt the user to enter their full name
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();

            // Prompt the user to enter the initial deposit amount
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();

            // Prompt the user to enter a security pin
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();

            try {
                // Generate a new account number
                long account_number = generateAccountNumber();

                // Prepare the query with the user's details
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);

                // Execute the query and check if the account was created successfully
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number; // Return the new account number if successful
                } else {
                    throw new RuntimeException("Account Creation failed!!"); // Throw an error if the account creation failed
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Print stack trace if an exception occurs
            }
        }
        throw new RuntimeException("Account Already Exist"); // Throw an error if the account already exists
    }

    // Method to get the account number associated with a given email
    public long getAccount_number(String email) {
        // SQL query to select the account number for the given email
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try {
            // Prepare the query with the provided email
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a matching account is found, return the account number
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace if an exception occurs
        }
        throw new RuntimeException("Account Number Doesn't Exist!"); // Throw an error if no matching account is found
    }

    // Method to generate a new account number
    private long generateAccountNumber() {
        try {
            // Create a statement object
            Statement statement = connection.createStatement();

            // Execute a query to get the last account number, ordered in descending order
            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");

            // If an account number is found, increment it by 1 and return it
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1;
            }
            else
            {
                // If no account number is found, return a default starting account number
                return 10000100;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); // Print stack trace if an exception occurs
        }
        // If an exception occurs, return the default starting account number
        return 10000100;
    }

    // Method to check if an account exists for a given email
    public boolean account_exist(String email) {
        // SQL query to check if an account exists for the given email
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try {
            // Prepare the query with the provided email
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a matching account is found, return true
            if (resultSet.next())
            {
                return true;
            }
            else
            {
                // If no matching account is found, return false
                return false;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); // Print stack trace if an exception occurs
        }
        // If an exception occurs, return false
        return false;
    }
}
