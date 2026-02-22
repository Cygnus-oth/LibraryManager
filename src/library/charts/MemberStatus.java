package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class MemberStatus implements chartdef {

    @Override
    public String getTitle() { 
        return "Member Account Statue"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        PieChart pc = new PieChart();
        pc.setLabelsVisible(true);


        String query = "SELECT status, COUNT(*) as total FROM users GROUP BY status";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("total");
                
                // Add the data to the pie chart
                pc.getData().add(new PieChart.Data(status + " (" + count + ")", count));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pc;
    }
}