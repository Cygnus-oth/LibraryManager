package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.sql.*;

public class neverBorowed implements chartdef {

    @Override
    public String getTitle() { 
        return "Dead Inventory (Books Never Borrowed)"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        ListView<String> listView = new ListView<>();
        listView.setPrefHeight(400);
        
        // CSS to make the list match your dashboard theme
        listView.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");

        /* SQL Logic: Find books with NO borrowing history */
        String query = "SELECT b.titre FROM books b " +
                       "LEFT JOIN borrowings bb ON b.id = bb.book_id " +
                       "WHERE bb.book_id IS NULL " +
                       "ORDER BY b.titre ASC";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                listView.getItems().add(rs.getString("titre"));
                count++;
            }

            if (count == 0) {
                return new Label("All books have been borrowed at least once! Well done.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Label("Error loading data: " + e.getMessage());
        }

        // Wrap in a VBox to add a count label at the bottom
        VBox container = new VBox(10);
        Label totalLabel = new Label("Total inactive books: " + listView.getItems().size());
        totalLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        
        container.getChildren().addAll(listView, totalLabel);
        return container;
    }
}