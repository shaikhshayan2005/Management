//================= Defines the package name=======================//
package BankingManagmentSystem;

import java.math.BigDecimal;
//================Imports all the classes in the java.sql package============//
import java.sql.*;
//================ Imports the Scanner class for user input===============//
import java.util.Scanner;

public class AccountManager
{
    // Instance variables to hold the connection to the database and a Scanner object
    private Connection connection;
    private Scanner scanner;
    // Constructor to initialize the connection and scanner
    AccountManager(Connection connection, Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;
    }

        // Method to credit money to an account
    public void credit_money(long account_number)throws SQLException
    {
        // Clear the scanner buffer
        scanner.nextLine();
        // Prompt the user to enter the amount
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        // Clear the scanner buffer
        scanner.nextLine();
        // Prompt the user to enter the security pin
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try
        {
            // Turn off auto-commit mode to manage transactions manually
            connection.setAutoCommit(false);
            if(account_number != 0)
            {
                // Prepare and execute a query to verify account number and security pin
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                // Check if the account and pin are valid
                if (resultSet.next())
                {
                    // Prepare a query to update (credit) the account balance
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    // If update is successful, commit the transaction
                    if (rowsAffected > 0)
                    {
                        System.out.println("Rs."+amount+" credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else
                    {
                        // If update fails, rollback the transaction
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else
                {
                    // If the security pin is invalid
                    System.out.println("Invalid Security Pin!");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        // Ensure auto-commit mode is turned back on
        connection.setAutoCommit(true);
    }
    // Method to debit money from an account
    public void debit_money(long account_number) throws SQLException
    {
        // Clear the scanner buffer
        scanner.nextLine();
        // Prompt the user to enter the amount
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        // Clear the scanner buffer
        scanner.nextLine();
        // Prompt the user to enter the security pin
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try
        {
            // Turn off auto-commit mode to manage transactions manually
            connection.setAutoCommit(false);
            if(account_number!=0)
            {
                // Prepare and execute a query to verify account number and security pin
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next())
                {
                    // Prepare a query to update (debit) the acc
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs."+amount+" debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){

                        // Write debit and credit queries
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                        // Debit and Credit prepared Statements
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

                        // Set Values for debit and credit prepared statements
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);
                        // Execute the debit and credit updates
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        // If both updates are successful, commit the transaction
                        if (rowsAffected1 > 0 && rowsAffected2 > 0)
                        {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else
                        {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else
                    {
                        // If there is insufficient balance
                        System.out.println("Insufficient Balance!");
                    }
                }else
                {
                    // If the security pin is invalid
                    System.out.println("Invalid Security Pin!");
                }
            }
            else
            {
                // If the account is invalid
                System.out.println("Invalid account number");
            }
        }
        catch (SQLException e)
        {

            e.printStackTrace();
        }
        // Ensure auto-commit mode is turned back on
        connection.setAutoCommit(true);
    }

        // Method to get the balance of an account
    public void getBalance(long account_number)
    {
        // Clear the scanner buffer
        scanner.nextLine();
        // Prompt the user to enter the security pin
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try
        {
            // Prepare and execute a query to retrieve the balance
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the account and pin are valid
            if(resultSet.next())
            {
                // Retrieve and print the balance
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }
            else
            {
                // If the security pin is invalid
                System.out.println("Invalid Pin!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
