package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AlgorithmSelectionController {
    @FXML
    private ComboBox<String> selectedAlgorithmComboBox;
    @FXML
    private TextField nrVerticesTextField;
    @FXML
    private Button nextButton;

    public void handleNextButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.representationChoice);
    }
}
