package library;



import net.sf.jasperreports.engine.*;
import java.io.InputStream;
import java.sql.Connection;

import library.charts.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StatsWindow {

    private VBox chartDisplayArea = new VBox(); 
    private Label chartTitle = new Label("Analytics Dashboard");
    private GridPane filterGrid = new GridPane();
    private String currentCategory = "Books";

    public void display(DatabaseHandler db) {
        Stage window = new Stage();
        window.setTitle("LMS");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // --- SIDEBAR ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(30, 15, 30, 15));
        sidebar.setPrefWidth(200);
        sidebar.getStyleClass().add("sidebar");

        Label logo = new Label("LMS");
        logo.getStyleClass().add("logo");
        
        Button booksBtn = createSidebarButton("Books", true);
        Button clientBtn = createSidebarButton("Client", false);
        Button testingBtn = createSidebarButton("Testing", false);

        // Sidebar Actions
        booksBtn.setOnAction(e -> {
            updateSidebarStyle(sidebar, booksBtn);
            currentCategory = "Books";
            refreshFilterGrid(db);
        });

        clientBtn.setOnAction(e -> {
            updateSidebarStyle(sidebar, clientBtn);
            currentCategory = "Client";
            refreshFilterGrid(db);
        });
        
        testingBtn.setOnAction(e -> {
            updateSidebarStyle(sidebar, testingBtn);
            generateJasperReport(db);
        });
        
        sidebar.getChildren().addAll(logo, new Separator(), booksBtn, clientBtn, testingBtn);
        root.setLeft(sidebar);

        // --- MAIN CONTENT ---
        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(30));
        VBox.setVgrow(mainContent, Priority.ALWAYS); 

        // --- TOP FILTER GRID ---
        filterGrid.setHgap(15); 
        filterGrid.setVgap(10); 
        filterGrid.setPadding(new Insets(20));
        filterGrid.getStyleClass().add("card");
        
        refreshFilterGrid(db);
        
        // --- CHART CONTAINER ---
        VBox chartCard = new VBox(10);
        chartCard.setPadding(new Insets(20));
        chartCard.getStyleClass().add("card");
        VBox.setVgrow(chartCard, Priority.ALWAYS); 

        chartTitle.getStyleClass().add("chart-title");
        VBox.setVgrow(chartDisplayArea, Priority.ALWAYS);

        chartCard.getChildren().addAll(chartTitle, chartDisplayArea);
        
        // Initial view
        updateView("By Genre", db, null, null);

        mainContent.getChildren().addAll(filterGrid, chartCard);
        root.setCenter(mainContent);
        
        // --- INTERNAL CSS ---
        Scene scene = new Scene(root, 1100, 750);
        String myCss = 
            ".root-pane { -fx-background-color: #F0F2F5; }" +
            ".sidebar { -fx-background-color: #4A90E2; }" +
            ".logo { -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; }" +
            ".card { -fx-background-color: white; -fx-background-radius: 15; " +
            "        -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0); }" +
            ".sidebar-button { -fx-background-color: transparent; -fx-text-fill: #D1E3F8; -fx-background-radius: 8; }" +
            ".sidebar-button-active { -fx-background-color: rgba(255, 255, 255, 0.2); " +
            "                         -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; }" +
            ".filter-button { -fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-text-fill: #495057; -fx-padding: 8 15; }" +
            ".filter-button-active { -fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; }" +
            ".chart-title { -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; }"+
            ".default-color0.chart-bar { -fx-bar-fill: #4A90E2; }";

        scene.getStylesheets().add("data:text/css," + myCss.replace(" ", "%20"));
        window.setScene(scene);
        window.show();
    }
    
    
 // Generate JasperReports PDF
    private void generateJasperReport(DatabaseHandler db) {
        try {
            // Load the .jasper file from src/resources
            InputStream reportStream = getClass().getResourceAsStream("/resources/books_report.jasper");

            if (reportStream == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Report Error");
                alert.setHeaderText(null);
                alert.setContentText("Report template not found!\nPlease ensure books_report.jasper is in src/resources/");
                alert.showAndWait();
                return;
            }

            // Get database connection
            Connection conn = db.getConnection();

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, null, conn);

            // Export to PDF on Desktop
            String outputPath = System.getProperty("user.home") + "/Desktop/Library_Statistics_Report.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report Generated");
            alert.setHeaderText("Success!");
            alert.setContentText("The report has been saved to:\n" + outputPath);
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Report Generation Failed");
            alert.setHeaderText("Error");
            alert.setContentText("Failed to generate report:\n" + e.getMessage());
            alert.showAndWait();
        }
    }


    private void refreshFilterGrid(DatabaseHandler db) {
        filterGrid.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            String btnText;
            if (currentCategory.equals("Books")) {
                btnText = getBookButtonName(i);
            } else {
                btnText = getClientButtonName(i);
            }

            Button btn = new Button(btnText);
            btn.getStyleClass().add("filter-button");
            btn.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(btn, Priority.ALWAYS);

            btn.setOnAction(e -> updateView(btnText, db, filterGrid, btn));
            filterGrid.add(btn, i % 4, i / 4);
        }
    }

    private String getBookButtonName(int i) {
        switch (i) {
            case 0: return "By Genre";
            case 1: return "By Author";
            case 2: return "Author Versatility";
            case 3: return "Dead Inventory";
            case 4: return "Author Popularity";
            case 5: return "Monthly Trends";
            default: return "Test " + i;
        }
    }

    private String getClientButtonName(int i) {
        switch (i) {
            case 0: return "Member Growth";
            case 1: return "Member Status";
            case 2: return "Top Borrowers";
            case 3: return "Inactive Members";
            case 4: return "Late Returns";
            case 5: return "User Roles";
            default: return "Client Test " + i;
        }
    }

    private void updateView(String type, DatabaseHandler db, GridPane grid, Button activeBtn) {
        chartdef provider = null;

        switch (type) {
            // Books
            case "By Genre": provider = new livrepargenre(); break;
            case "By Author": provider = new BooksByAuthor(); break;
            case "Author Versatility": provider = new AuthorVersatility(); break;
            case "Dead Inventory": provider = new neverBorowed(); break;
            case "Author Popularity": provider = new AuthorPopularity(); break;
            case "Monthly Trends": provider = new MonthlyTrends(); break;
            // Clients
            case "Member Growth": provider = new MemberGrowth(); break;
            case "Member Status": provider = new MemberStatus(); break;
            case "Top Borrowers": provider = new TopBorrowers(); break;
            case "Inactive Members": provider = new InactiveMembers(); break;
            case "Late Returns": provider = new LateReturns(); break;
            case "User Roles": provider = new UserRoleDistribution(); break;
        }

        if (provider != null) {
            chartTitle.setText(provider.getTitle());
            chartDisplayArea.getChildren().setAll(provider.getChart(db));
        } else {
            chartDisplayArea.getChildren().setAll(new Label("Analytics for " + type + " coming soon!"));
            chartTitle.setText("Placeholder: " + type);
        }

        if (grid != null && activeBtn != null) {
            grid.getChildren().forEach(n -> n.getStyleClass().remove("filter-button-active"));
            activeBtn.getStyleClass().add("filter-button-active");
        }
    }

    private void updateSidebarStyle(VBox sidebar, Button activeBtn) {
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.getStyleClass().remove("sidebar-button-active");
                node.getStyleClass().add("sidebar-button");
            }
        });
        activeBtn.getStyleClass().remove("sidebar-button");
        activeBtn.getStyleClass().add("sidebar-button-active");
    }

    private Button createSidebarButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add(active ? "sidebar-button-active" : "sidebar-button");
        return btn;
    }
}