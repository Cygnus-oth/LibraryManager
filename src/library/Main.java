package library;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // We initialize the new UI class here
        LibraryDashboard dashboard = new LibraryDashboard();
        dashboard.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}