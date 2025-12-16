import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class BankingApp {
    private static final String url = "...";
    private static final String username = "...";
    private static final String password = "...";

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
//        USER MENU START
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
                    System.out.print("Full Name: ");
                    String userFullName = scanner.nextLine();
                    System.out.print("Email: ");
                    String userEmail = scanner.nextLine();
                    System.out.print("Password: ");
                    String userPassword = scanner.nextLine();
                    boolean res = user.registerUser(userFullName, userEmail, userPassword);
                    System.out.println(res ? "Registration Successfull!" : "Registration Failed!");
                }

//              LOGIN
                case 2 -> {
                    System.out.print("Email: ");
                    String enteredEmail = scanner.nextLine();
                    System.out.print("Password: ");
                    String enteredPassword = scanner.nextLine();
                    email = user.loginUser(enteredEmail, enteredPassword);
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

                default -> {
                    System.out.println("Invalid Option");
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
//        BANK ACCOUNT HOLDER FEATURES LIST
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

//                DEBIT MONEY
                case 1 -> {
                    System.out.print("Enter Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter Security Pin: ");
                    String securityPin = scanner.nextLine();

                    int result = accountManager.debitMoney(accountNumber, amount, securityPin);

                    if (result == 0) System.out.println("Transaction Failed!");
                    else if (result == 1) System.out.println("Rs."+amount+" credited Successfully");
                    else if (result == 2) System.out.println("Invalid Security Pin or Account Number");
                    else System.out.println("Insufficient Balance!");
                }

//                CREDIT MONEY
                case 2 -> {
                    System.out.print("Enter Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter Security Pin: ");
                    String securityPin = scanner.nextLine();

                    Boolean result = accountManager.creditMoney(accountNumber, amount, securityPin);

                    if (result == null) System.out.println("Invalid Security Pin or Account Number");
                    else if (result) System.out.println("Rs."+amount+" credited Successfully");
                    else System.out.println("Transaction Failed!");

                }

//                TRANSFER MONEY
                case 3 -> {
                    System.out.print("Enter Receiver Account Number: ");
                    long receiverAccountNumber = scanner.nextLong();
                    System.out.print("Enter Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter Security Pin: ");
                    String securityPin = scanner.nextLine();

                    accountManager.transferMoney(accountNumber, receiverAccountNumber, amount, securityPin);
                }

//                CHECK BALANCE
                case 4 -> {
                    System.out.print("Enter Security Pin: ");
                    String securityPin = scanner.nextLine();
                    Double checkedBalance = accountManager.checkBalance(accountNumber, securityPin);
                    if (checkedBalance != null) {
                        System.out.println("Balance: "+ checkedBalance);
                    } else {
                        System.out.println("Invalid Pin!");
                    }
                }

//                LOG OUT
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