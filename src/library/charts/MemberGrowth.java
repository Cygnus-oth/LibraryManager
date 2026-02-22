package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class MemberGrowth implements chartdef {

    @Override
    public String getTitle() { 
        return "New Member Registrations Over Time"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Registration Month");
        yAxis.setLabel("Number of New Members");

        LineChart<String, Number> lc = new LineChart<>(xAxis, yAxis);
        lc.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        /* SQL Logic:
           1. Format the created_at to 'Month Year' (e.g., 'Jan 2024').
           2. Count users per month.
           3. Sort by Year and then Month number to keep chronological order.
        */
        String query = "SELECT DATE_FORMAT(created_at, '%b %Y') as month_year, " +
                       "COUNT(*) as total " +
                       "FROM users " +
                       "GROUP BY YEAR(created_at), MONTH(created_at), month_year " +
                       "ORDER BY YEAR(created_at) ASC, MONTH(created_at) ASC";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                    rs.getString("month_year"), 
                    rs.getInt("total")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        lc.getData().add(series);
        
        // Optional: Style the line to make it stand out
        lc.lookup(".chart-series-line").setStyle("-fx-stroke: #2ecc71; -fx-stroke-width: 3px;");
        
        return lc;
    }
}