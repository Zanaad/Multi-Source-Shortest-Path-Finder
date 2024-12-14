package controllers;

import algorithms.FloydWarshall;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import models.Graph;
import utils.Alerts;

public class GraphVisualizationController {

    @FXML
    private Button calculateButton;

    @FXML
    private Button nextButton;

    @FXML
    private Pane graphPane;

    @FXML
    private Text resultText;

    private int[][][] stepMatrices; // Step-by-step matrices
    private int currentStep = 0;
    private int numVertices;

    public void initialize() {
        Graph graph = Graph.getInstance();
        numVertices = graph.getVertices();

        // Get step-by-step matrices for visualization
        stepMatrices = FloydWarshall.findShortestPathsStepByStep(graph);

        if (stepMatrices == null || stepMatrices.length == 0) {
            Alerts.errorMessage("Error initializing graph. Please check the input.");
            return;
        }

        // Draw the initial graph (step 0)
        drawGraph(stepMatrices[0], null, -1);

        // Configure result text
        resultText.setLayoutX(10);
        resultText.setLayoutY(420);
        resultText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        graphPane.getChildren().add(resultText);
    }

    @FXML
    void handleNextButton(ActionEvent event) {
        if (currentStep >= stepMatrices.length - 1) {
            Alerts.successMessage("Algorithm completed. No more steps.");
            nextButton.setDisable(true);
            return;
        }

        // Draw the graph for the next step
        drawGraph(stepMatrices[currentStep + 1], stepMatrices[currentStep], currentStep);
        currentStep++;
    }

    @FXML
    void handleCalculateButton(ActionEvent event) {
        Graph graph = Graph.getInstance();
        int[][] finalMatrix = FloydWarshall.findShortestPaths(graph);

        // Check for negative weight cycles
        if (hasNegativeCycle(finalMatrix)) {
            resultText.setText("Graph contains a negative weight cycle. Cannot calculate shortest paths.");
            Alerts.errorMessage("Negative weight cycle detected!");
            return;
        }

        // Display shortest paths for all pairs
        StringBuilder results = new StringBuilder("Shortest paths: ");
        for (int source = 0; source < numVertices; source++) {
            for (int target = 0; target < numVertices; target++) {
                if (finalMatrix[source][target] == Integer.MAX_VALUE / 2) {
                    results.append("No path from ").append((char) ('A' + source))
                            .append(" to ").append((char) ('A' + target)).append("\n");
                } else {
                    results.append("Distance from ").append((char) ('A' + source))
                            .append(" to ").append((char) ('A' + target)).append(": ")
                            .append(finalMatrix[source][target]).append("\n");
                }
            }
        }

        resultText.setText(results.toString());
        Alerts.successMessage("Calculation completed.");
    }
