package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class MonthlyTrends implements chartdef {

    @Override
    public String getTitle() { 
        return "Monthly Borrowing Activity"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        // Setup Axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Month");
        yAxis.setLabel("Total Borrows");

        LineChart<String, Number> lc = new LineChart<>(xAxis, yAxis);
        lc.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        /* SQL Logic:
           1. Extract the month name from the borrow_date.
           2. Count occurrences.
           3. Group and Order by month number (1-12).
        */
        String query = "SELECT MONTHNAME(borrow_date) as month_name, " +
                       "COUNT(*) as total " +
                       "FROM borrowings " +
                       "GROUP BY MONTH(borrow_date), month_name " +
                       "ORDER BY MONTH(borrow_date) ASC";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                    rs.getString("month_name"), 
                    rs.getInt("total")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        lc.getData().add(series);
        
        // Optional: Smooth CSS for the line
        lc.lookup(".chart-series-line").setStyle("-fx-stroke: #4A90E2; -fx-stroke-width: 3px;");
        
        return lc;
    }
}