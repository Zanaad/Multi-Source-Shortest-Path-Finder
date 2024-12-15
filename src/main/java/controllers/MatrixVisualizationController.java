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

import static utils.SharedData.selectedAlgorithm;

public class MatrixVisualizationController {

    public Button backButton;
    @FXML
    private Button nextButton;

    @FXML
    private GridPane weightsGrid;

    private int[][][] stepMatrices;
    private int[][] johnsonsResult;
    private int numVertices;
    private int currentStep = 0;
    private TextField[][] matrixFields;
    private boolean isFloydWarshall = true; // Default to Floyd-Warshall

    public void initialize() {
        Graph graph = Graph.getInstance();
        numVertices = graph.getVertices();

        if ("Floyd-Warshall".equalsIgnoreCase(selectedAlgorithm)) {
            isFloydWarshall = true;
            stepMatrices = FloydWarshall.findShortestPathsStepByStep(graph);
            matrixFields = new TextField[numVertices][numVertices];
            populateGrid(stepMatrices[0], null, -1);
        } else if ("Johnson".equalsIgnoreCase(selectedAlgorithm)) {
            isFloydWarshall = false;
            johnsonsResult = JohnsonsAlgorithm.findShortestPaths(graph);
            matrixFields = new TextField[numVertices][numVertices];
            populateGrid(johnsonsResult, null, -1);
            nextButton.setDisable(true);
        } else {
            Alerts.errorMessage("Unknown algorithm selected.");
        }
    }

    private void populateGrid(int[][] matrix, int[][] previousMatrix, int focusedIndex) {
        weightsGrid.getChildren().clear(); // Clear existing grid elements
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                TextField field = new TextField();
                field.setPrefHeight(30);
                field.setPrefWidth(30);
                field.setEditable(false);

                // Set the text of the cell
                String valueText = matrix[i][j] == Integer.MAX_VALUE / 2 ? "∞" : String.valueOf(matrix[i][j]);
                field.setText(valueText);

                // If there is a previous matrix and the value has changed, apply a color
                if (previousMatrix != null && previousMatrix[i][j] != matrix[i][j]) {
                    field.setStyle("-fx-background-color: #FFCC00;"); // Yellow color for changed cells
                } else {
                    field.setStyle(""); // Reset the style for unchanged cells
                }

                // Apply a distinct color to the row and column of the current focused vertex (k)
                if (focusedIndex != -1) {
                    if (i == focusedIndex || j == focusedIndex) {
                        field.setStyle("-fx-background-color: #ADD8E6;"); // Light Blue for focused row/column
                    }
                }

                weightsGrid.add(field, j, i);
                matrixFields[i][j] = field;
            }
        }
    }

    @FXML
    void handleNextButton(ActionEvent event) {
        if (currentStep >= numVertices) {
            Alerts.successMessage("Algorithm completed. No more steps.");
            nextButton.setDisable(true);
            return;
        }

        // Get the current and previous matrices
        int[][] currentMatrix = stepMatrices[currentStep + 1];
        int[][] previousMatrix = stepMatrices[currentStep];

        // Display the matrix for the current step, highlighting the changes and focusing on the current row/column
        populateGrid(currentMatrix, previousMatrix, currentStep);

        currentStep++;
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.visualizationChoice);
    }
}