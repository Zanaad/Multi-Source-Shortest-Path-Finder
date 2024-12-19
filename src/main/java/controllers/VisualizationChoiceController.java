package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VisualizationChoiceController {
    @FXML
    void handleGraphVisualization(ActionEvent event) {
        Navigator.navigate(event, Navigator.graphVisualization);

    }

    @FXML
    void handleMatrixVisualization(ActionEvent event) {
        Navigator.navigate(event, Navigator.matrixVisualization);
    }

    public void handleBackButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.inputWeights);
    }
}
