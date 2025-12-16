import java.sql.Connection;
import java.util.Scanner;

public class AccountManager {
    private final Connection connection;
    private final Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public int debitMoney(long accountNumber, double amount, String securityPin) {
        throw new UnsupportedOperationException("debitMoney not implemented yet");
    }

    public Boolean creditMoney(long accountNumber, double amount, String securityPin) {
        throw new UnsupportedOperationException("accountNumber not implemented yet");
    }

    public void transferMoney(long accountNumber, long receiverAccountNumber, double amount, String securityPin) {
        throw new UnsupportedOperationException("transferMoney not implemented yet");
    }

    public Double checkBalance(long accountNumber, String securityPin) {
        throw new UnsupportedOperationException("checkBalance not implemented yet");
    }
}
