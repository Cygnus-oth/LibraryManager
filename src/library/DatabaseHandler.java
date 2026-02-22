package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet; // FIXED: Added this import
import java.sql.SQLException;
import java.sql.Statement; // FIXED: Added this import

public class DatabaseHandler {

    // Database credentials based on your SQL dump
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASS = "root";

    // Method to provide the connection to other classes
    public Connection getConnection() throws SQLException {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public int getTotalBooksCount() {
        String query = "SELECT COUNT(*) FROM books";
        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}