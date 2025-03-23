package DAO;
import DTO.Transaction;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class transactionDAO {
    public ArrayList<Transaction> getTransactionHistory(String accountNumber) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE sender_id = ? OR receiver_id = ?";

        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, accountNumber);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int transaction_id = resultSet.getInt("transaction_id");
                String sender_id = resultSet.getString("sender_id");
                String receiver_id = resultSet.getString("receiver_id");
                Double amount = resultSet.getDouble("amount");
                String message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("created_at");
                Transaction transaction = new Transaction(transaction_id, sender_id, receiver_id, amount, message, timestamp);
                transactions.add(transaction);
            }

            return transactions;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}