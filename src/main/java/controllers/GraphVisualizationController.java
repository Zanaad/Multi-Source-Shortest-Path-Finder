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
    private Button calculatePathsButton;

    @FXML
    private Text resultText;

    @FXML
    private Canvas graphCanvas;

    private Graph graph;
    private int[][] shortestPaths;
    private int numVertices;
    private int radius = 30;  // Radius for nodes
    private double[] nodePositionsX;
    private double[] nodePositionsY;

    private Integer[] highlightedNodes = new Integer[2]; // Array to store two highlighted nodes
    private Integer highlightedNode = null; // Node to be highlighted

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
            shortestPaths = FloydWarshall.findShortestPaths(graph);
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
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (adjacencyMatrix[i][j] != 0 && adjacencyMatrix[i][j] != Integer.MAX_VALUE / 2) {
                    gc.strokeLine(nodePositionsX[i], nodePositionsY[i], nodePositionsX[j], nodePositionsY[j]);
                    // Draw weight label in the middle of the edge
                    gc.fillText(String.valueOf(adjacencyMatrix[i][j]),
                            (nodePositionsX[i] + nodePositionsX[j]) / 2,
                            (nodePositionsY[i] + nodePositionsY[j]) / 2);
                }
            }
        }

        // Draw nodes
        gc.setFill(Color.BLUE);
        for (int i = 0; i < numVertices; i++) {
            if (highlightedNodes[0] != null && highlightedNodes[0] == i ||
                    highlightedNodes[1] != null && highlightedNodes[1] == i) {
                gc.setFill(Color.RED); // Highlight selected nodes in RED
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
                if (highlightedNodes[0] == null) {
                    // Set the first selected node
                    highlightedNodes[0] = i;
                    showNodeInfo(i);
                } else if (highlightedNodes[1] == null && highlightedNodes[0] != i) {
                    // Set the second selected node
                    highlightedNodes[1] = i;
                    showPathBetweenNodes(highlightedNodes[0], i);
                } else {
                    // Reset both selections
                    highlightedNodes[0] = null;
                    highlightedNodes[1] = null;
                    resultText.setText("");
                }
                renderGraph(graph.getAdjacencyMatrix()); // Re-render the graph
                break;
            }
        }
    }


    private void showNodeInfo(int nodeIndex) {
        if (shortestPaths == null) {
            resultText.setText("Shortest paths not calculated yet.");
            return;
        }

        // Display shortest paths from the selected node
        String nodeLabel = String.valueOf((char) ('A' + nodeIndex));
        StringBuilder sb = new StringBuilder("Shortest paths from node " + nodeLabel + ":\n");

        for (int i = 0; i < numVertices; i++) {
            if (i != nodeIndex) {
                sb.append("To ").append((char) ('A' + i))
                        .append(": ").append(shortestPaths[nodeIndex][i]).append("\n");
            }
        }

        resultText.setText(sb.toString());
    }

    private void showPathBetweenNodes(int startNode, int endNode) {
        if (shortestPaths == null) {
            resultText.setText("Shortest paths not calculated yet.");
            return;
        }

        String startNodeLabel = String.valueOf((char) ('A' + startNode));
        String endNodeLabel = String.valueOf((char) ('A' + endNode));
        int pathLength = shortestPaths[startNode][endNode];

        if (pathLength == Integer.MAX_VALUE / 2) {
            resultText.setText("No path exists between " + startNodeLabel + " and " + endNodeLabel);
        } else {
            resultText.setText("Shortest path from " + startNodeLabel + " to " + endNodeLabel + ": " + pathLength);
        }
    }
}

