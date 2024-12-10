package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VisualizationChoiceController {

    @FXML
    private Button graphVisualizationButton;

    @FXML
    private Button matrixVisualizationButton;

    @FXML
    void handleGraphVisualization(ActionEvent event) {
        Navigator.navigate(event, Navigator.graphVisualization);

    }

    @FXML
    void handleMatrixVisualization(ActionEvent event) {
        Navigator.navigate(event, Navigator.matrixVisualization);
    }

}
