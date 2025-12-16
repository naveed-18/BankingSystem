import java.sql.*;
import java.util.*;
public class User {
    private final Connection connection;
    private final Scanner scanner;

    public User (Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public boolean registerUser(String userFullName, String userEmail, String userPassword) {

        if (userExists(userEmail)) {
            System.out.println("User already EXISTS for this Email Address!!!");
            return false;
        }

        String registerQuery = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(registerQuery)) {
            preparedStatement.setString(1, userFullName);
            preparedStatement.setString(2, userEmail);
            preparedStatement.setString(3, userPassword);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loginUser(String enteredEmail, String enteredPassword) {

        String loginQuery = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
            preparedStatement.setString(1, enteredEmail);
            preparedStatement.setString(2, enteredPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return enteredEmail;
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userExists(String userEmail) {
        String userExistsQuery = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(userExistsQuery)) {
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}