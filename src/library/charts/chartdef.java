package library.charts;

import library.DatabaseHandler;
import javafx.scene.Node;

public interface chartdef {
	Node getChart(DatabaseHandler db);
    String getTitle();
}

