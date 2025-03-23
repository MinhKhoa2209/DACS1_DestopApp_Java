package DAO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    public Connection conn;
    private final String url = "jdbc:mysql://localhost:3306/bank_management";
    private final String username = "root";
    private final String password = "minhkhoa22092005";
    private final String driver = "com.mysql.cj.jdbc.Driver";

    public DBConn() {
        try {
            this.conn = getConnection(url, username, password, driver);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối đến database");
        }
    }

    public static void main(String[] args) {
        new DBConn();
    }

    public Connection getConn() {
        return conn;
    }

    public Connection getConnection(String dbURL, String userName, String password, String driver) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(dbURL, userName, password);
    }
}