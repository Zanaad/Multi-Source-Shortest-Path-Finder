package controllers;

import algorithms.FloydWarshall;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.Graph;
import utils.Alerts;

public class MatrixVisualizationController {

    @FXML
    private Button nextButton;

    @FXML
    private GridPane weightsGrid;

    private int[][][] stepMatrices;
    private int numVertices;
    private int currentStep = 0;
    private TextField[][] matrixFields;

    public void initialize() {
        Graph graph = Graph.getInstance();
        numVertices = graph.getVertices();

        // Get step-by-step matrices
        stepMatrices = FloydWarshall.findShortestPathsStepByStep(graph);
        matrixFields = new TextField[numVertices][numVertices];

        // Initialize grid with the first matrix (step 0)
        populateGrid(stepMatrices[0]);
    }

    private void populateGrid(int[][] matrix) {
        weightsGrid.getChildren().clear(); // Clear existing grid elements
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                TextField field = new TextField();
                field.setPrefHeight(30);
                field.setPrefWidth(30);
                field.setEditable(false);
                field.setText(matrix[i][j] == Integer.MAX_VALUE / 2 ? "∞" : String.valueOf(matrix[i][j]));
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

        // Display the matrix for the current step
        populateGrid(stepMatrices[currentStep + 1]);

        currentStep++;
    }
}