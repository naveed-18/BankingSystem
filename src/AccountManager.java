import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private final Connection connection;
    private final Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public Boolean creditMoney(long accountNumber, double amount, String securityPin) {
        try {
            connection.setAutoCommit(false);
            String accNumQuery = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(accNumQuery);
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(creditQuery);
                preparedStatement1.setDouble(1, amount);
                preparedStatement1.setLong(2, accountNumber);
                int affectedRows = preparedStatement1.executeUpdate();
                if (affectedRows > 0) {
                    connection.commit();
                    connection.setAutoCommit(true);
                    return true;
                } else {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int debitMoney(long accountNumber, double amount, String securityPin) {
        try {
            connection.setAutoCommit(false);
            String accNumQuery = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(accNumQuery);
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("balance");
                if (currentBalance >= amount) {
                    String creditQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(creditQuery);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, accountNumber);
                    int affectedRows = preparedStatement1.executeUpdate();
                    if (affectedRows > 0) {
                        connection.commit();
                        connection.setAutoCommit(true);
                        return 1; //successfull
                    } else {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        return 0; //failed
                    }
                } else {
                    return 3; //insufficient balance
                }
            } else {
                return 2; //invalid acc num or pin
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void transferMoney (long senderAccountNumber, long receiverAccountNumber, double amount, String securityPin) {
        try {
            connection.setAutoCommit(false);
            String fetchDetailQuery = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchDetailQuery);
            preparedStatement.setLong(1, senderAccountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("balance");
                if (currentBalance >= amount) {
                    String debitQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                    String creditQuery = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                    PreparedStatement debitPreparedStatement = connection.prepareStatement(debitQuery);
                    PreparedStatement creditPreparedStatement = connection.prepareStatement(creditQuery);

                    debitPreparedStatement.setDouble(1, amount);
                    debitPreparedStatement.setLong(2, senderAccountNumber);

                    creditPreparedStatement.setDouble(1, amount);
                    creditPreparedStatement.setLong(2, receiverAccountNumber);

                    int debitAffectedRows = debitPreparedStatement.executeUpdate();
                    int creditAffectedRows = creditPreparedStatement.executeUpdate();

                    if (debitAffectedRows > 0 && creditAffectedRows > 0) {
                        System.out.println("Transaction Successful!");
                        System.out.println("Rs."+amount+" Transferred Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                    } else {
                        System.out.println("Transaction Failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                }
            } else {
                System.out.println("Invalid Security Pin!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double checkBalance(long accountNumber, String securityPin) {
        String checkBalQuery = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(checkBalQuery)) {
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
