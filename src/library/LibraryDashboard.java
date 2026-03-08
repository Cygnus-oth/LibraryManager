package library;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class LibraryDashboard {

    private DatabaseHandler db = new DatabaseHandler();
    private Label statsLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("Library Management System");

        VBox centerMenu = createMenuButtons();

        BorderPane root = new BorderPane();
        root.setCenter(centerMenu);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0f2027, #203a43, #2a5298); " +
                "-fx-font-family: 'Segoe UI';"
        );

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createMenuButtons() {

        VBox menu = new VBox(15);
        menu.setPadding(new Insets(25));
        menu.setAlignment(Pos.CENTER);
        menu.setStyle(
                "-fx-background-color: rgba(255,255,255,0.15); " +
                "-fx-background-radius: 20;"
        );

        Label title = new Label("Library management");
        title.setStyle(
                "-fx-font-size: 50px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: linear-gradient(to bottom right, #0f2027, #203a43, #2a5298); " +
                "-fx-padding: 10 25 10 25; " +
                "-fx-background-radius: 15;"
        );

        String buttonStyle =
                "-fx-background-color: #4A90E2; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 8; " +
                "-fx-min-width: 600; " +
                "-fx-padding: 12; " +
                "-fx-cursor: hand;";

        Button addBtn = new Button("Add Book ➕");
        Button deleteBtn = new Button("Delete Book 🗑");
        Button searchBtn = new Button("Book Search 🔍");
        Button statbtn = new Button("View statistics 📊");

        addBtn.setStyle(buttonStyle);
        deleteBtn.setStyle(buttonStyle);
        searchBtn.setStyle(buttonStyle);
        statbtn.setStyle(buttonStyle);

        addBtn.setOnMouseEntered(e -> addBtn.setStyle(buttonStyle + "-fx-background-color:#2a5298;"));
        addBtn.setOnMouseExited(e -> addBtn.setStyle(buttonStyle));

        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle(buttonStyle + "-fx-background-color:#2a5298;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(buttonStyle));

        searchBtn.setOnMouseEntered(e -> searchBtn.setStyle(buttonStyle + "-fx-background-color:#2a5298;"));
        searchBtn.setOnMouseExited(e -> searchBtn.setStyle(buttonStyle));

        statbtn.setOnMouseEntered(e -> statbtn.setStyle(buttonStyle + "-fx-background-color:#2a5298;"));
        statbtn.setOnMouseExited(e -> statbtn.setStyle(buttonStyle));

        addBtn.setOnAction(e -> showAjouterLivre());
        deleteBtn.setOnAction(e -> showDeleteLivre());
        searchBtn.setOnAction(e -> showRechercheLivre());
        statbtn.setOnAction(e -> {
            StatsWindow statsPopup = new StatsWindow();
            statsPopup.display(db);
        });

        menu.getChildren().addAll(title, addBtn, deleteBtn, searchBtn, statbtn);

        return menu;
    }

    public void refreshStats() {
        int count = db.getTotalBooksCount();
        statsLabel.setText("Total Books: " + count);
    }

    public void showAjouterLivre() {

        Stage stage = new Stage();
        stage.setTitle("Ajouter un Livre");

        Label title = new Label("Ajouter un livre");
        title.setStyle(
                "-fx-font-size: 22px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: #2ecc71; " +
                "-fx-padding: 12 30 12 30; " +
                "-fx-background-radius: 25;"
        );

        String fieldStyle =
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #27ae60; " +
                "-fx-padding: 10; " +
                "-fx-font-size: 14px;";

        TextField tfTitre = new TextField();
        tfTitre.setPromptText("Titre du livre");
        tfTitre.setStyle(fieldStyle);

        TextField tfAuteur = new TextField();
        tfAuteur.setPromptText("Auteur du livre");
        tfAuteur.setStyle(fieldStyle);

        TextField tfCategory = new TextField();
        tfCategory.setPromptText("Catégorie du livre");
        tfCategory.setStyle(fieldStyle);

        String saveBtnStyle =
                "-fx-background-color: #27ae60; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 25; " +
                "-fx-padding: 10 30 10 30; " +
                "-fx-cursor: hand;";

        String closeBtnStyle =
                "-fx-background-color: #7f8c8d; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 25; " +
                "-fx-padding: 8 25 8 25; " +
                "-fx-cursor: hand;";

        Button btnSave = new Button("Enregistrer");
        btnSave.setStyle(saveBtnStyle);

        Button btnClose = new Button("Fermer");
        btnClose.setStyle(closeBtnStyle);
        btnClose.setOnAction(e -> stage.close());

        VBox root = new VBox(15,
                title,
                tfTitre,
                tfAuteur,
                tfCategory,
                btnSave,
                btnClose
        );

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #e9fbef, #c8f7dc);"
        );

        Scene scene = new Scene(root, 420, 320);
        stage.setScene(scene);
        stage.show();
    }

    public void showDeleteLivre() {

        Stage stage = new Stage();
        stage.setTitle("Supprimer un Livre");

        Label title = new Label("Supprimer un livre");
        title.setStyle(
                "-fx-font-size: 20px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: #e74c3c; " +
                "-fx-padding: 10 20 10 20; " +
                "-fx-background-radius: 15;"
        );

        String fieldStyle =
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #c0392b; " +
                "-fx-padding: 8;";

        TextField tfId = new TextField();
        tfId.setPromptText("ID du livre à supprimer");
        tfId.setStyle(fieldStyle);

        TextField tfTitre = new TextField();
        tfTitre.setPromptText("Titre du livre");
        tfTitre.setStyle(fieldStyle);

        TextField tfAuteur = new TextField();
        tfAuteur.setPromptText("Auteur du livre");
        tfAuteur.setStyle(fieldStyle);

        TextField tfCategory = new TextField();
        tfCategory.setPromptText("Catégorie du livre");
        tfCategory.setStyle(fieldStyle);

        String deleteBtnStyle =
                "-fx-background-color: #e74c3c; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 20; " +
                "-fx-padding: 8 20 8 20; " +
                "-fx-cursor: hand;";

        String closeBtnStyle =
                "-fx-background-color: #7f8c8d; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 20; " +
                "-fx-padding: 8 20 8 20; " +
                "-fx-cursor: hand;";

        Button btnDelete = new Button("Supprimer");
        btnDelete.setStyle(deleteBtnStyle);

        Button btnClose = new Button("Fermer");
        btnClose.setStyle(closeBtnStyle);
        btnClose.setOnAction(e -> stage.close());

        VBox root = new VBox(12,
                title,
                tfId,
                tfTitre,
                tfAuteur,
                tfCategory,
                btnDelete,
                btnClose
        );

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #fdecea, #f9d6d3);"
        );

        Scene scene = new Scene(root, 420, 350);
        stage.setScene(scene);
        stage.show();
    }

    public void showRechercheLivre() {

        Stage stage = new Stage();
        stage.setTitle("Recherche de Livre");

        Label title = new Label("Rechercher un livre");
        title.setStyle(
                "-fx-font-size: 22px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: #1f3c88; " +
                "-fx-padding: 12 30 12 30; " +
                "-fx-background-radius: 25;"
        );

        String fieldStyle =
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #1f3c88; " +
                "-fx-padding: 10; " +
                "-fx-font-size: 14px; " +
                "-fx-background-color: white;";

        TextField tfTitre = new TextField();
        tfTitre.setPromptText("Titre du livre");
        tfTitre.setStyle(fieldStyle);

        TextField tfAuteur = new TextField();
        tfAuteur.setPromptText("Auteur du livre");
        tfAuteur.setStyle(fieldStyle);

        TextField tfCategory = new TextField();
        tfCategory.setPromptText("Catégorie du livre");
        tfCategory.setStyle(fieldStyle);

        String buttonBlueStyle =
                "-fx-background-color: #1f3c88; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 25; " +
                "-fx-padding: 10 30 10 30; " +
                "-fx-cursor: hand;";

        Button btnSearch = new Button("Rechercher");
        btnSearch.setStyle(buttonBlueStyle);

        Button btnClose = new Button("Fermer");
        btnClose.setStyle(buttonBlueStyle);
        btnClose.setOnAction(e -> stage.close());

        VBox root = new VBox(18, title, tfTitre, tfAuteur, tfCategory, btnSearch, btnClose);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #4facfe, #00f2fe);"
        );

        Scene scene = new Scene(root, 420, 320);
        stage.setScene(scene);
        stage.show();
    }
}