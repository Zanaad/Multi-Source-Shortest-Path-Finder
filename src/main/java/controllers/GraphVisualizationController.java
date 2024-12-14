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
            resultText.setText("Graph contains a negative weight cycle.");
            Alerts.errorMessage("Negative weight cycle!");
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
    private void drawGraph(int[][] matrix, int[][] previousMatrix, int focusedIndex) {
        graphPane.getChildren().clear();

        // Center and radius for circular layout
        double centerX = 300;
        double centerY = 200;
        double radius = 150;

        Circle[] nodes = new Circle[numVertices];
        double[] angles = new double[numVertices];

        // Place nodes in circular positions
        for (int i = 0; i < numVertices; i++) {
            angles[i] = i * 2 * Math.PI / numVertices;
            double x = centerX + radius * Math.cos(angles[i]);
            double y = centerY + radius * Math.sin(angles[i]);

            Circle node = new Circle(x, y, 20);
            node.setStyle("-fx-fill: lightblue; -fx-stroke: black;");
            graphPane.getChildren().add(node);

            Text label = new Text(x - 10, y + 5, String.valueOf((char) ('A' + i)));
            graphPane.getChildren().add(label);

            nodes[i] = node;
        }

        // Draw edges and weights
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                // Skip edges with "infinite" weight or self-loops
                if (matrix[i][j] == Integer.MAX_VALUE / 2 || i == j) {
                    continue;
                }

                double x1 = centerX + radius * Math.cos(angles[i]);
                double y1 = centerY + radius * Math.sin(angles[i]);
                double x2 = centerX + radius * Math.cos(angles[j]);
                double y2 = centerY + radius * Math.sin(angles[j]);

                // Draw edge
                Line edge = new Line(x1, y1, x2, y2);
                edge.setStyle("-fx-stroke: black;");
                graphPane.getChildren().add(edge);

                // Weight label at the midpoint
                double midX = (x1 + x2) / 2;
                double midY = (y1 + y2) / 2;

                String weightText = String.valueOf(matrix[i][j]);
                Text weightLabel = new Text(midX, midY, weightText);

                // Highlight changes
                if (previousMatrix != null && previousMatrix[i][j] != matrix[i][j]) {
                    weightLabel.setStyle("-fx-fill: red; -fx-font-weight: bold;");
                }
                graphPane.getChildren().add(weightLabel);
            }
        }

        // Highlight the focused vertex
        if (focusedIndex != -1) {
            nodes[focusedIndex].setStyle("-fx-fill: yellow; -fx-stroke: black;");
        }
    }

    private boolean hasNegativeCycle(int[][] matrix) {
        for (int i = 0; i < numVertices; i++) {
            if (matrix[i][i] < 0) {
                return true; // Negative cycle detected
            }
        }
        return false;
    }
}

