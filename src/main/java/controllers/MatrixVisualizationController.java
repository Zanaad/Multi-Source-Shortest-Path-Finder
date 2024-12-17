package controllers;

import algorithms.FloydWarshall;
import algorithms.JohnsonsAlgorithm;
import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.Graph;
import utils.Alerts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.SharedData.selectedAlgorithm;

public class MatrixVisualizationController {

    public Button backButton;
    @FXML
    private Button nextButton;

    @FXML
    private GridPane weightsGrid;

    private int[][] previousMatrix;
    private int numVertices;
    private int currentStep = 0;
    private TextField[][] matrixFields;
    private Graph graph;

    public void initialize() {
        graph = Graph.getInstance();
        numVertices = graph.getVertices();
        matrixFields = new TextField[numVertices][numVertices];

        if ("Floyd-Warshall".equalsIgnoreCase(selectedAlgorithm)) {
            // Generate the initial matrix (step 0)
            previousMatrix = graph.getAdjacencyMatrix();
            populateGrid(previousMatrix, null, -1);
        } else if ("Johnson".equalsIgnoreCase(selectedAlgorithm)) {
            int[][] johnsonResult = JohnsonsAlgorithm.findShortestPaths(graph);
            populateGrid(johnsonResult, null, -1);
            nextButton.setDisable(true);
        } else {
            Alerts.errorMessage("Unknown algorithm selected.");
        }
    }

    private void populateGrid(int[][] matrix, List<int[]> changedPositions, int focusedIndex) {
        weightsGrid.getChildren().clear(); // Clear existing grid elements
        Set<String> changesSet = new HashSet<>(); // Set for fast lookup of changes

        if (changedPositions != null) {
            for (int[] pos : changedPositions) {
                changesSet.add(pos[0] + "," + pos[1]);
            }
        }

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                TextField field = new TextField();
                field.setPrefHeight(30);
                field.setPrefWidth(30);
                field.setEditable(false);

                // Set the text of the cell
                String valueText = matrix[i][j] == Integer.MAX_VALUE / 2 ? "∞" : String.valueOf(matrix[i][j]);
                field.setText(valueText);

                // Highlight changed cells
                if (changesSet.contains(i + "," + j)) {
                    field.setStyle("-fx-background-color: #FFCC00;"); // Yellow for changed cells
                } else {
                    field.setStyle(""); // Reset style
                }

                // Highlight focused row/column
                if (focusedIndex != -1 && (i == focusedIndex || j == focusedIndex)) {
                    field.setStyle("-fx-background-color: #ADD8E6;"); // Light blue
                }

                weightsGrid.add(field, j, i);
                matrixFields[i][j] = field;
            }
        }
    }

    @FXML
    void handleNextButton() {
        if (currentStep >= numVertices) {
            Alerts.successMessage("Algorithm completed. No more steps.");
            nextButton.setDisable(true);
            return;
        }

        // Generate the matrix for the current step
        FloydWarshall.StepResult result = FloydWarshall.generateStep(graph, currentStep);

        // Display the matrix, highlighting changes
        populateGrid(result.matrix, result.changedPositions, currentStep);

        // Update the previous matrix and step
        currentStep++;
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.visualizationChoice);
    }
}
