package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import utils.Alerts;

public class AlgorithmSelectionController {
    @FXML
    private ComboBox<String> selectedAlgorithmComboBox;
    @FXML
    private TextField nrVerticesTextField;
    @FXML
    private Button nextButton;
    private String selectedAlgorithm;
    private Integer nrVertices;

    public void handleNextButton(ActionEvent event) {
        try {
            nrVertices = Integer.parseInt(nrVerticesTextField.getText());
            selectedAlgorithm = selectedAlgorithmComboBox.getValue();

            if (selectedAlgorithm == null) {
                Alerts.errorMessage("Please select an algorithm");
                return;
            }
            if (nrVertices < 2) {
                Alerts.errorMessage("Number of vertices must be at least 2");
                return;
            }
            Navigator.navigate(event, Navigator.inputWeights);
        } catch (NumberFormatException e) {
            Alerts.errorMessage("Please enter a valid number of vertices");
        }
    }
}
