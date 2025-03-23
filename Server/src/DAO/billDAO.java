package DAO;
import DTO.Bill;
import java.sql.*;
import java.util.ArrayList;

public class billDAO {
    public Bill getBill(String id_card, String company) {
        String sql = "SELECT * FROM bills WHERE receiver_id = ? AND type = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, id_card);
            ps.setString(2, company);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int billId = rs.getInt("bill_id");
                    String receiver_id= rs.getString("receiver_id");
                    String type = rs.getString("type");
                    double amount = rs.getDouble("amount");
                    String status = rs.getString("status");
                    Timestamp date = rs.getTimestamp("created_at");
                  return new Bill(billId,receiver_id,type,amount,status,date);

                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updateBillStatus(String billId, String status) {
        String sql = "UPDATE bills SET status = ? WHERE bill_id = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, billId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<Bill> getPaidBill(String id_card) {
        ArrayList<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE receiver_id = ? AND status = 'paid'";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, id_card);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
              int billId = resultSet.getInt("bill_id");
              String receiver_id= resultSet.getString("receiver_id");
              String type = resultSet.getString("type");
              double amount = resultSet.getDouble("amount");
              String status = resultSet.getString("status");
              Timestamp timestamp = resultSet.getTimestamp("created_at");
              Bill bill = new Bill(billId,receiver_id,type,amount,status,timestamp);
              bills.add(bill);
            }
            return bills;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
