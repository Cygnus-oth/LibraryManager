package library;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage {

   
    private static final String TEAL_COLOR = "#00708a";
    private static final String BORDER_GRAY = "#cccccc";

    public void show(Stage stage) {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: white; -fx-font-family: 'Segoe UI', sans-serif;");

      
        VBox leftPane = createLeftPane();

        
        VBox rightPane = createRightPane(stage);

        root.getChildren().addAll(leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 850, 550);
        stage.setTitle("OptiVent - Connexion");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private VBox createLeftPane() {
        VBox pane = new VBox(25);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(40));
        pane.setStyle("-fx-background-color: #f8f9fa;");

        Label brand = new Label("OptiVent");
        brand.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill: #005a70;");

      
        Label illustrationPlaceholder = new Label("[ Image Illustration ]");
        illustrationPlaceholder.setStyle("-fx-text-fill: #adb5bd; -fx-font-style: italic;");

        pane.getChildren().addAll(brand, illustrationPlaceholder);
        return pane;
    }

    private VBox createRightPane(Stage stage) {
        VBox pane = new VBox(20);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(50));

        Label header = new Label("Connexion");
        header.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + TEAL_COLOR + ";");

        VBox fieldsContainer = new VBox(15);
        fieldsContainer.setMaxWidth(320);

        HBox userBox = buildInputGroup("👤", "Nom d'utilisateur", false);
        HBox passBox = buildInputGroup("🔒", "Mot de passe", true);

        Button loginBtn = new Button("Se connecter");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color: " + TEAL_COLOR + "; -fx-text-fill: white; " +
                         "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12; -fx-cursor: hand;");

        Hyperlink forgotLink = new Hyperlink("Mot de passe oublier ?");
        forgotLink.setStyle("-fx-text-fill: " + TEAL_COLOR + "; -fx-underline: false;");

        HBox footer = new HBox(5, new Label("Pas encore de compte ?"), new Hyperlink("S'inscrire"));
        footer.setAlignment(Pos.CENTER);
        ((Hyperlink)footer.getChildren().get(1)).setStyle("-fx-text-fill: " + TEAL_COLOR + "; -fx-font-weight: bold;");

        pane.getChildren().addAll(header, userBox, passBox, loginBtn, forgotLink, footer);
        return pane;
    }

    private HBox buildInputGroup(String icon, String prompt, boolean isPassword) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(8, 12, 8, 12));
        box.setStyle("-fx-border-color: " + BORDER_GRAY + "; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label iconLbl = new Label(icon);
        TextField input = isPassword ? new PasswordField() : new TextField();
        input.setPromptText(prompt);
        input.setPrefWidth(280);
        input.setStyle("-fx-background-color: transparent; -fx-focus-color: transparent; -fx-text-box-border: transparent;");

        box.getChildren().addAll(iconLbl, input);
        return box;
    }
}