package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class TopBorrowers implements chartdef {

    @Override
    public String getTitle() { 
        return "Top 5 Most Active Borrowers"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setLegendVisible(false);
        xAxis.setLabel("Member Name");
        yAxis.setLabel("Total Books Borrowed");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        /* SQL Logic:
           1. JOIN users and borrowings.
           2. COUNT how many borrowing records exist per user.
           3. ORDER by the count to find the 'Top' members.
        */
        String query = "SELECT u.full_name, COUNT(b.id) as borrow_count " +
                       "FROM users u " +
                       "JOIN borrowings b ON u.id = b.user_id " +
                       "GROUP BY u.id, u.full_name " +
                       "ORDER BY borrow_count DESC " +
                       "LIMIT 10";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                    rs.getString("full_name"), 
                    rs.getInt("borrow_count")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        bc.getData().add(series);
        return bc;
    }
}