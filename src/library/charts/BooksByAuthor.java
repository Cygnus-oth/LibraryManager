package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class BooksByAuthor implements chartdef {

    @Override
    public String getTitle() { 
        return "Top Authors by Book Count"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        // CategoryAxis for names and NumberAxis for counts
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setLegendVisible(false);
        xAxis.setLabel("Author Name");
        yAxis.setLabel("Number of Books");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // SQL query to count books per author from your 'books' table
        String query = "SELECT auteur, COUNT(*) as total FROM books " +
                       "GROUP BY auteur " +
                       "HAVING total > 1 " + // Only show authors with more than 1 book to keep it clean
                       "ORDER BY total DESC";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("auteur"), rs.getInt("total")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        bc.getData().add(series);
        return bc;
    }
}