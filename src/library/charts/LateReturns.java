package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class LateReturns implements chartdef {

    @Override
    public String getTitle() { 
        return "Active Borrowing Status"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Month");
        yAxis.setLabel("Books Borrowed");

        StackedBarChart<String, Number> chart =
                new StackedBarChart<>(xAxis, yAxis);

        chart.setTitle("Monthly Borrow Performance");

        XYChart.Series<String, Number> onTimeSeries = new XYChart.Series<>();
        onTimeSeries.setName("Returned On Time");

        XYChart.Series<String, Number> overdueSeries = new XYChart.Series<>();
        overdueSeries.setName("Overdue / Not Returned");

        String query =
            "SELECT DATE_FORMAT(borrow_date, '%Y-%m') AS month, " +
            "SUM(CASE WHEN status = 'returned' " +
            "AND DATEDIFF(return_date, borrow_date) <= 14 " +
            "THEN 1 ELSE 0 END) AS returned_on_time, " +
            "SUM(CASE WHEN (status = 'returned' " +
            "AND DATEDIFF(return_date, borrow_date) > 14) " +
            "OR (status = 'active' " +
            "AND DATEDIFF(CURDATE(), borrow_date) > 14) " +
            "THEN 1 ELSE 0 END) AS overdue_or_not_returned " +
            "FROM borrowings " +
            "GROUP BY month " +
            "ORDER BY month";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                String month = rs.getString("month");

                int onTime = rs.getInt("returned_on_time");
                int overdue = rs.getInt("overdue_or_not_returned");

                onTimeSeries.getData().add(
                        new XYChart.Data<>(month, onTime));

                overdueSeries.getData().add(
                        new XYChart.Data<>(month, overdue));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        chart.getData().add(onTimeSeries);
        chart.getData().add(overdueSeries);
        chart.applyCss();

        for (XYChart.Series<String, Number> s : chart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {

                if (s.getName().equals("Returned On Time")) {
                    d.getNode().setStyle("-fx-bar-fill: #2196F3;"); // Blue
                } else {
                    d.getNode().setStyle("-fx-bar-fill: #E53935;"); // Red
                }
            }
        }


        return chart;
    }


}