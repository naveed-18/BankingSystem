import java.sql.*;
import java.util.*;
public class User {
    private final Connection connection;
    private final Scanner scanner;

    public User (Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public boolean registerUser() {
        throw new UnsupportedOperationException("registerUser not implemented yet");
    }

    public String loginUser() {
        throw new UnsupportedOperationException("loginUser not implemented yet");
    }

    public boolean userExists(String userEmail) {
        throw new UnsupportedOperationException("userExists not implemented yet");
    }
}