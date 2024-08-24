
//================= Defines the package name=======================//
package BankingManagmentSystem;
//================Imports all the classes in the java.sql package============//
import java.sql.*;
//================ Imports the Scanner class for user input===============//
import java.util.Scanner;

import java.util.Scanner;

import static java.lang.Class.forName;


public class BankingApp {
 //====================Database connection details from code line 16 till 18==========//
    private static final String url = "jdbc:mysql://localhost:3306/banking system";
    private static final String username = "root";
    private static final String password = "nadia2678@";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try
        {
          // Load and register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            //Print the exception message if the driver is not found

            System.out.println(e.getMessage());
        }
        try{
            //Establish connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //Scanner object for user input
            Scanner scanner =  new Scanner(System.in);
            //Create User object with connection and scanner
            User user = new User(connection, scanner);
            // Create Accounts object
            Accounts accounts = new Accounts(connection, scanner);
            // Create AccountManager object
            AccountManager accountManager = new AccountManager(connection, scanner);
           // Declare variable to store email
            String email;
            // Declare variable to store account number
            long account_number;
            // WHILE LOOP
            while(true)
            {
                // Display main menu
                System.out.println("============ WELCOME TO BANKING SYSTEM ================");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                // Read user's choice
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        // Register new user
                        user.register();
                        break;
                    case 2:
                        // Check if the user has an existing account
                        email = user.login();
                        if(email!=null)
                        {
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!accounts.account_exist(email))
                            {
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if (scanner.nextInt() == 1)
                                {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully!");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else
                                {
                                    break;
                                }

                            }
                            // Get the user's account number
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                // Display account management menu
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                // Read user's choice
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        // Debit money from account
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        // Credit money to account
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        // Transfer money to another account
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        // Check account balance
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        // Log out
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }    }

                        }
                        else
                        {
                            // Invalid email or password
                            System.out.println(" Sorry,Incorrect Email or Password!");
                            System.out.println("TRY AGAIN!");
                        }
                    case 3:
                        // Exit the system
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        // Terminate the program
                        return;
                    default:
                        // Invalid choice for main menu
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }
        catch (SQLException e)
        {
            // Print SQL exceptions
            e.printStackTrace();
        }
    }
}





