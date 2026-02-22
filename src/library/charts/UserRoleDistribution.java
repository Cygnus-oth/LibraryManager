package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import java.sql.*;

public class UserRoleDistribution implements chartdef {

    @Override
    public String getTitle() { 
        return "User Role Distribution"; 
    }

    @Override
    public Node getChart(DatabaseHandler db) {
        PieChart pc = new PieChart();
        pc.setLabelsVisible(true);
        pc.setLegendVisible(true);

        /* SQL Logic:
           1. Count users from the 'users' table.
           2. Group them by the 'role' column (Admin, Member, etc.).
        */
        String query = "SELECT role, COUNT(*) as total FROM users GROUP BY role";

        try (Connection conn = db.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String roleName = rs.getString("role");
                int count = rs.getInt("total");
                
                // Add data to the PieChart
                pc.getData().add(new PieChart.Data(roleName.toUpperCase() + " (" + count + ")", count));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pc;
    }
}