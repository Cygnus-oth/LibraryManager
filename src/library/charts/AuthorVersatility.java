package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class AuthorVersatility implements chartdef {

    @Override
    public String getTitle() { 
        return "Author Versatility (Genres per Author)"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setLegendVisible(false);
        xAxis.setLabel("Author");
        yAxis.setLabel("Number of Unique Genres");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        /* SQL Logic:
           1. JOIN books and book_genres.
           2. COUNT(DISTINCT genre_id) to see how many different genres 
              each author is associated with.
        */
        String query = "SELECT b.auteur, COUNT(DISTINCT bg.genre_id) as genre_count " +
                       "FROM books b " +
                       "JOIN book_genres bg ON b.id = bg.book_id " +
                       "GROUP BY b.auteur " +
                       "HAVING genre_count > 1 " + // Show authors with at least 2 genres
                       "ORDER BY genre_count DESC";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                    rs.getString("auteur"), 
                    rs.getInt("genre_count")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        bc.getData().add(series);
        return bc;
    }
}