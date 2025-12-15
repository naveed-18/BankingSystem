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
        throw new UnsupportedOperationException("openAccount not implemented yet");
    }

    public boolean accountExists(String userEmail) {
        throw new UnsupportedOperationException("accountExists not implemented yet");
    }

    public long getAccountNumber(String userEmail) {
        throw new UnsupportedOperationException("getAccountNumber not implemented yet");
    }

    public long generateAccountNumber() {
        throw new UnsupportedOperationException("generateAccountNumber not implemented yet");
    }
}
