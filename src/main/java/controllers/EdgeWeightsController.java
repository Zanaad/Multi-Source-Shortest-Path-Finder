package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class EdgeWeightsController {

    @FXML
    private Button nextButton;

    @FXML
    private GridPane verticesGrid;

    @FXML
    private GridPane weightsGrid;

    @FXML
    void handleNextButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.visualizationChoice);
    }

}
