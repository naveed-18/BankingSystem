import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class BankingApp {
    // TODO: Move DB credentials to config file or environment variables
    private static final String url = "...";
    private static final String username = "...";
    private static final String password = "u..";

    public static void main (String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts account = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

//          USER MENU
            userMenu(scanner, user, account, accountManager);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void userMenu(Scanner scanner, User user, Accounts account, AccountManager accountManager) {
        String email;
        long accountNumber = 0;

        while (true) {
//          Menu List for Users
            System.out.println("\n---WELCOME TO BANK---\n");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int optionForUsers;
            System.out.println("Choose an Option : ");
            try {
                optionForUsers = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Option!");
                continue;
            }

            switch (optionForUsers) {
//              REGISTER
                case 1 -> {
                    boolean res = user.registerUser();
                    System.out.println(res ? "Registration Successfull!" : "Registration Failed!");
                }

//              LOGIN
                case 2 -> {
                    email = user.loginUser();
                    if (email != null) { //Logged In
                        System.out.println("\n---User Logged In---\n");
                        int flag = bankAccountMenu(scanner, user, account, accountManager, email, accountNumber);
                        if (flag == 0) bankAccountMenu(scanner, user, account, accountManager, email, accountNumber); //User Wrong Option Entered
                    } else {
                        System.out.println("Invalid Email or Password");
                    }
                }

//              EXIT
                case 3 -> {
                    System.out.println("---THANK YOU FOR USING BANKING SYSTEM!!!---");
                    System.out.println("---Exiting System!---");
                    return;
                }
            }
        }
    }

    public static int bankAccountMenu(Scanner scanner, User user, Accounts account, AccountManager accountManager, String email, long accountNumber) {
        if (!account.accountExists(email)) { //Checking bank account exists or not
            System.out.println("\n1. Create a Bank Account");
            System.out.println("2. Exit");
            int option;
            System.out.print("Choose an Option : ");
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Option!, Choose correct Option");
                return 0;
            }
//          OPENING BANK ACCOUNT
            if (option == 0 || option > 2) return 0;
            if (option == 1) {

                System.out.print("Enter Full Name: ");
                String fullName = scanner.nextLine();
                System.out.print("Enter Initial Amount: ");
                double balance = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Enter Security Pin: ");
                String securityPin = scanner.nextLine();
                long accNumber = account.generateAccountNumber();

                accountNumber = account.openAccount(accNumber, fullName, email, balance, securityPin);

                System.out.println("Account Created Successfully");
                System.out.println("Your Account Number is: " + accountNumber);
            } else {
                return 1;
            }
        }

        accountNumber = account.getAccountNumber(email);
        while (true) {
//          Menu List for Bank Account Holders
            System.out.println("\n1. Debit Money");
            System.out.println("2. Credit Money");
            System.out.println("3. Transfer Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Log Out");

//          Choosing Options
            int optionsForBankAccountHolders;
            System.out.print("Choose an Option : ");
            try {
                optionsForBankAccountHolders = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Option!");
                System.out.println("Choose Correct Option");
                continue;
            }

            switch (optionsForBankAccountHolders) {
                case 1 -> {
                    System.out.println("TODO : Add Debit Money Feature");
                }
                case 2 -> {
                    System.out.println("TODO : Add Credit Money Feature");
                }
                case 3 -> {
                    System.out.println("TODO : Add Transfer Money Feature");
                }
                case 4 -> {
                    System.out.println("TODO : Add Check Balance Feature");
                }
                case 5 -> {
                    return 1;
                }
                default -> {
                    System.out.println("Invalid Option!");
                    System.out.println("Choose Correct Option");
                }
            } //Switch case close

        } //While close
    }
}