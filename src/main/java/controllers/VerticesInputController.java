package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Graph;
import utils.Alerts;

public class VerticesInputController {
    @FXML
    private TextField numVerticesTextField;
    private Graph graph;
    private int numVertices;

    public void handleNextButton(ActionEvent event) {
        try {
            numVertices = Integer.parseInt(numVerticesTextField.getText());
            if (numVertices < 2) {
                Alerts.errorMessage("Number of vertices must be at least 2");
                return;
            }
            graph = new Graph(numVertices);
            Navigator.navigate(event, Navigator.inputWeights);
        } catch (NumberFormatException e) {
            Alerts.errorMessage("Please enter a valid number of vertices");
        }
    }
}