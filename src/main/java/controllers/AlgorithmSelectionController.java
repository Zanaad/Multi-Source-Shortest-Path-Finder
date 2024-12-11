package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import utils.Alerts;

import static utils.SharedData.numVertices;

public class AlgorithmSelectionController {
    @FXML
    private ComboBox<String> selectedAlgorithmComboBox;
    @FXML
    private TextField numVerticesTextField;
    @FXML
    private Button nextButton;
    private String selectedAlgorithm;

    public void handleNextButton(ActionEvent event) {
        try {
            numVertices = Integer.parseInt(numVerticesTextField.getText());
            selectedAlgorithm = selectedAlgorithmComboBox.getValue();

            if (selectedAlgorithm == null) {
                Alerts.errorMessage("Please select an algorithm");
                return;
            }
            if (numVertices < 2) {
                Alerts.errorMessage("Number of vertices must be at least 2");
                return;
            }
            Navigator.navigate(event, Navigator.inputWeights);
        } catch (NumberFormatException e) {
            Alerts.errorMessage("Please enter a valid number of vertices");
        }
    }
}
