package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import java.sql.*;

public class livrepargenre implements chartdef {

    @Override
    public String getTitle() { 
        return "Book Distribution by Genre"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        PieChart pc = new PieChart();
        
        String query = "SELECT g.name, COUNT(bg.book_id) as total " +
                       "FROM genres g " +
                       "LEFT JOIN book_genres bg ON g.id = bg.genre_id " +
                       "GROUP BY g.name " +
                       "HAVING total > 0";

        // We ask the 'db' object for the connection, then run the SQL here
        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                String name = rs.getString("name");
	                int count = rs.getInt("total");
	                pc.getData().add(new PieChart.Data(name, count));
	            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Optional: add a label to the chart area if the DB fails
        }

        return pc;
    }
}