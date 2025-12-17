import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private final Connection connection;
    private final Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long openAccount(long accountNumber, String fullName, String email, double balance, String securityPin) {
        String openAccountQuery = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(openAccountQuery)) {
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, fullName);
            preparedStatement.setString(3, email);
            preparedStatement.setDouble(4, balance);
            preparedStatement.setString(5, securityPin);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) return accountNumber;
            else throw new RuntimeException("Account Creation failed!!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean accountExists(String userEmail) {
        String existsQuery = "SELECT account_number FROM accounts WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(existsQuery)) {
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getAccountNumber(String userEmail) {
        String getAccNumQuery = "SELECT account_number FROM accounts WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAccNumQuery)) {
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getLong("account_number");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Account Number Doesn't Exist");
    }

    public long generateAccountNumber() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1")
        ) {
            if (resultSet.next()) return resultSet.getLong("account_number") + 1;
            else return 10001000;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
