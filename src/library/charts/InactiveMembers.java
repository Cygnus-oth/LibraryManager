package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.sql.*;

public class InactiveMembers implements chartdef {

    @Override
    public String getTitle() { 
        return "Inactive Members (Never Borrowed)"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        ListView<String> listView = new ListView<>();
        listView.setPrefHeight(400);

        /* SQL Logic: 
           Find users who do NOT have a matching entry in the borrowings table.
        */
        String query = "SELECT u.full_name FROM users u " +
                       "LEFT JOIN borrowings b ON u.id = b.user_id " +
                       "WHERE b.id IS NULL " +
                       "ORDER BY u.full_name ASC";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                listView.getItems().add(rs.getString("full_name"));
                count++;
            }

            if (count == 0) {
                return new Label("Great! Every member has used the library at least once.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Label("Error: " + e.getMessage());
        }

        VBox container = new VBox(10);
        Label totalLabel = new Label("Total Inactive Members: " + listView.getItems().size());
        totalLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        container.getChildren().addAll(listView, totalLabel);
        return container;
    }
}