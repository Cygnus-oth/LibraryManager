package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class AuthorPopularity implements chartdef {

    @Override
    public String getTitle() { 
        return "Author Popularity (Total Borrows)"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setLegendVisible(false);
        xAxis.setLabel("Author Name");
        yAxis.setLabel("Number of Times Borrowed");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        /* SQL Logic:
           1. JOIN books and borrowings.
           2. Group by the author name (auteur).
           3. COUNT how many rows exist in borrowings for that author.
        */
        String query = "SELECT b.auteur, COUNT(bb.id) as borrow_count " +
                       "FROM books b " +
                       "JOIN borrowings bb ON b.id = bb.book_id " +
                       "GROUP BY b.auteur " +
                       "ORDER BY borrow_count DESC " +
                       "LIMIT 10";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Use rs.getString("auteur") to match your DB column
                series.getData().add(new XYChart.Data<>(
                    rs.getString("auteur"), 
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