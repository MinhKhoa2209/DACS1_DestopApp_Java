package DAO;
import DTO.User;
import java.sql.*;

public class userDAO {
    public User login(String username, String password) {
        String sql = "Select * from users where username = ? and password = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("id_card"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("account_number"),
                            rs.getDouble("balance"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkAccountExists(User user) {
        String sql = "SELECT * FROM users WHERE id_card = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, user.getId_card());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public User register(User user) {
        String sql = "INSERT INTO users (id_card, name, email, phone, account_number,balance, username, password) VALUES (?,?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, user.getId_card());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAccountNumber());
            ps.setDouble(6, user.getBalance());
            ps.setString(7, user.getUsername());
            ps.setString(8, user.getPassword());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserInfo(String accountNumber) {
        String sql = "SELECT * FROM users WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("id_card"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("account_number"),
                            rs.getDouble("balance"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changeInformation(String accountNumber, String phoneNumber, String email, String password) {
        String sql = "UPDATE users SET phone = ?, email = ?, password = ? WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, accountNumber);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deposit(String accountNumber, double amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, accountNumber);
            int rowAffected = ps.executeUpdate();
            if (rowAffected > 0 ) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean withdraw(double amount, String accountNumber) {
            String sql = "UPDATE users SET balance = balance - ? WHERE account_number = ?";
            try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
                ps.setDouble(1,amount);
                ps.setString(2,accountNumber);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
         catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String transfer(String senderAccountNumber, String receiverAccountNumber, double amount, String message) {
        //Check if sender has enough balance, then check receiver account exists

        String sql = "SELECT * FROM users WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, receiverAccountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return "receiverNotFound";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT balance FROM users WHERE account_number = ?";
        double senderBalance = 0;
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, senderAccountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    senderBalance = rs.getDouble("balance");
                    if (senderBalance < amount) {
                        return "notEnoughBalance";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Update balance for sender
        sql = "UPDATE users SET balance = balance - ? WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, senderAccountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Update balance for receiver
        sql = "UPDATE users SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, receiverAccountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Insert transfer history
        sql = "INSERT INTO transactions (sender_id, receiver_id, amount, message) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setString(1, senderAccountNumber);
            ps.setString(2, receiverAccountNumber);
            ps.setDouble(3, amount);
            ps.setString(4, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "transferSuccessfully";
    }
    public boolean updateAccountBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE account_number = ?";
        try (PreparedStatement ps = new DBConn().getConn().prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

