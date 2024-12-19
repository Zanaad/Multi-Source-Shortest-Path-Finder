package controllers;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.Graph;
import utils.Alerts;

public class EdgeWeightsController {
    @FXML
    private GridPane verticesGrid;

    @FXML
    private GridPane weightsGrid;

    private TextField[][] weightFields;
    private Graph graph;
    private int numVertices;

    public void initialize() {
        populateGrid();
    }

    private void populateGrid() {
        graph = Graph.getInstance();
        numVertices = graph.getVertices();
        weightFields = new TextField[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {

            TextField vertexField = new TextField();
            vertexField.setText("V" + (i + 1));
            vertexField.setPrefHeight(30);
            vertexField.setPrefWidth(30);
            verticesGrid.add(vertexField, 0, i);

            for (int j = 0; j < numVertices; j++) {
                TextField weightField = new TextField();
                weightField.setPromptText("∞");
                weightField.setPrefHeight(30);
                weightField.setPrefWidth(30);
                if (i == j) {
                    weightField.setText("0");
                    weightField.setEditable(false);
                }
                weightsGrid.add(weightField, j, i);
                weightFields[i][j] = weightField;
            }
        }

    }

    @FXML
    void handleNextButton(ActionEvent event) {
        int[][] adjacencyMatrix = new int[numVertices][numVertices];
        try {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    String input = weightFields[i][j].getText().trim();
                    adjacencyMatrix[i][j] = input.isEmpty() || input.equals("∞") ? Integer.MAX_VALUE / 2 : Integer.parseInt(input);
                }
            }
            graph.setAdjacencyMatrix(adjacencyMatrix);
            Navigator.navigate(event, Navigator.visualizationChoice);
        } catch (NumberFormatException e) {
            Alerts.errorMessage("Please enter valid weights (integers or '∞').");
        }

    }

    public void handleBackButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.nrVertices);
    }
}