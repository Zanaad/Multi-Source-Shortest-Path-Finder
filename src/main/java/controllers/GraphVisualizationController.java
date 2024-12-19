package controllers;

import algorithms.FloydWarshall;
import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import models.Graph;

public class GraphVisualizationController {
    @FXML
    private Text resultText;

    @FXML
    private Canvas graphCanvas;
    private FloydWarshall.PathResult pathResult;

    private Graph graph;
    private int[][] shortestPaths;
    private int numVertices;
    private int radius = 30;  // Radius for nodes
    private double[] nodePositionsX;
    private double[] nodePositionsY;

    private Integer highlightedNode = null; // Single highlighted node

    public void initialize() {
        graph = Graph.getInstance();
        numVertices = graph.getVertices();
        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();

        nodePositionsX = new double[numVertices];
        nodePositionsY = new double[numVertices];

        // Calculate node positions for a circular layout
        calculateNodePositions();

        // Calculate shortest paths if they are not yet calculated
        if (shortestPaths == null) {
            pathResult = FloydWarshall.runFloydWarshall(graph, -1);
            shortestPaths = pathResult.distances; // For distances only
        }

        renderGraph(adjacencyMatrix);
    }

    private void calculateNodePositions() {
        double angleStep = 2 * Math.PI / numVertices;
        double centerX = graphCanvas.getWidth() / 2;
        double centerY = graphCanvas.getHeight() / 2;

        for (int i = 0; i < numVertices; i++) {
            nodePositionsX[i] = centerX + 150 * Math.cos(i * angleStep);
            nodePositionsY[i] = centerY + 150 * Math.sin(i * angleStep);
        }
    }

    private void renderGraph(int[][] adjacencyMatrix) {
        GraphicsContext gc = graphCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());

        // Draw edges
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) { // Avoid duplicating edges
                if (adjacencyMatrix[i][j] != 0 && adjacencyMatrix[i][j] != Integer.MAX_VALUE / 2) {
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(1);

                    gc.strokeLine(nodePositionsX[i], nodePositionsY[i], nodePositionsX[j], nodePositionsY[j]);
                }
            }
        }

        // Draw nodes
        for (int i = 0; i < numVertices; i++) {
            if (highlightedNode != null && highlightedNode == i) {
                gc.setFill(Color.RED); // Highlight selected node in RED
            } else {
                gc.setFill(Color.BLUE); // Default color
            }
            gc.fillOval(nodePositionsX[i] - radius, nodePositionsY[i] - radius, 2 * radius, 2 * radius);

            // Draw node label
            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf((char) ('A' + i)), nodePositionsX[i] - 10, nodePositionsY[i] + 5);
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.visualizationChoice);
    }

    @FXML
    public void handleCanvasClick(MouseEvent event) {
        for (int i = 0; i < numVertices; i++) {
            double dx = event.getX() - nodePositionsX[i];
            double dy = event.getY() - nodePositionsY[i];
            if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                // Set the clicked node as the highlighted node
                highlightedNode = i;
                showNodeInfo(i);
                renderGraph(graph.getAdjacencyMatrix()); // Re-render the graph
                break;
            }
        }
    }

    private void showNodeInfo(int nodeIndex) {
        if (pathResult == null) {
            resultText.setText("Shortest paths not calculated yet.");
            return;
        }

        String nodeLabel = String.valueOf((char) ('A' + nodeIndex));
        StringBuilder sb = new StringBuilder("Shortest paths from node " + nodeLabel + ":\n");

        for (int i = 0; i < numVertices; i++) {
            if (i != nodeIndex) {
                int distance = pathResult.distances[nodeIndex][i];
                if (distance == Integer.MAX_VALUE / 2) {
                    sb.append("To ").append((char) ('A' + i)).append(": No path exists\n");
                } else {
                    sb.append("To ").append((char) ('A' + i)).append(": ").append(distance).append("\n");
                }
            }
        }

        resultText.setText(sb.toString());
    }
}
