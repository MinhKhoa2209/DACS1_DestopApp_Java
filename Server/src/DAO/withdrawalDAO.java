package DAO;
import DTO.Withdrawal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class withdrawalDAO {
    public boolean insertWithdrawalInformation(double amount, double balance, String accountNumber) {
        String sql = "INSERT INTO withdrawal (amount, balance, account_number) VALUES (?, ?, ?)";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setDouble(2, balance);
            ps.setString(3, accountNumber);
            int rowAffected = ps.executeUpdate();
            if (rowAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<Withdrawal> getWithdrawalHistory(String accountNumber) {
        ArrayList<Withdrawal> withdrawals = new ArrayList<>();
        String sql = "SELECT * FROM withdrawal WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Double amount = resultSet.getDouble("amount");
                Double balance = resultSet.getDouble("balance");
                String account_number = resultSet.getString("account_number");
                Timestamp date = resultSet.getTimestamp("created_at");
                Withdrawal withdrawal = new Withdrawal(amount, balance, account_number, date);
                withdrawals.add(withdrawal);
            }
            return withdrawals;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
