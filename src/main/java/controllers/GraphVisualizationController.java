package controllers;

import algorithms.FloydWarshall;
import app.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import models.Graph;

import java.util.ArrayList;
import java.util.List;

public class GraphVisualizationController {
    @FXML
    private Text resultText;

    @FXML
    private Canvas graphCanvas;
    private FloydWarshall.PathResult pathResult;

    private Graph graph;
    private int[][] shortestPaths;
    private int numVertices;
    private int radius = 30;
    private double[] nodePositionsX;
    private double[] nodePositionsY;
    private List<int[]> highlightedEdges = new ArrayList<>();

    private Integer[] highlightedNodes = new Integer[2];

    public void initialize() {
        try {
            graph = Graph.getInstance();
            numVertices = graph.getVertices();
            int[][] adjacencyMatrix = graph.getAdjacencyMatrix();

            nodePositionsX = new double[numVertices];
            nodePositionsY = new double[numVertices];

            calculateNodePositions();

            if (shortestPaths == null) {
                pathResult = FloydWarshall.runFloydWarshall(graph, -1);
                shortestPaths = pathResult.distances;
            }

            renderGraph(adjacencyMatrix);

        } catch (IllegalStateException e) {
            resultText.setText("Error: " + e.getMessage());
        }
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
                    // Reset selections when a third node is clicked
                    highlightedNodes[0] = i;
                    highlightedNodes[1] = null;
                    highlightedEdges.clear();
                    resultText.setText("");
                    showNodeInfo(i);
                }
                renderGraph(graph.getAdjacencyMatrix());
                break;
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        Navigator.navigate(event, Navigator.visualizationChoice);
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
            for (int j = 0; j < numVertices; j++) {
                if (adjacencyMatrix[i][j] != 0 && adjacencyMatrix[i][j] != Integer.MAX_VALUE / 2) {
                    if (isEdgeHighlighted(i, j)) {
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(3);
                    } else {
                        gc.setStroke(Color.BLACK);
                        gc.setLineWidth(1);
                    }

                    // Draw the line (edge)
                    double startX = nodePositionsX[i];
                    double startY = nodePositionsY[i];
                    double endX = nodePositionsX[j];
                    double endY = nodePositionsY[j];

                    gc.strokeLine(startX, startY, endX, endY);

                    // Draw arrowhead
                    drawArrowhead(gc, startX, startY, endX, endY);


                    double midX = (startX + endX) / 2;
                    double midY = (startY + endY) / 2;

                    // Check for a bidirectional edge
                    if (adjacencyMatrix[j][i] != 0 && adjacencyMatrix[i][j] != 0) {
                        // Adjust positions to avoid overlap for bidirectional edges
                        double offsetX = -(endY - startY) / Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)) * 10;
                        double offsetY = (endX - startX) / Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)) * 10;

                        // Position weight labels
                        gc.setFill(Color.BLACK);
                        gc.fillText(String.valueOf(adjacencyMatrix[i][j]), midX + offsetX, midY + offsetY);
                        gc.fillText(String.valueOf(adjacencyMatrix[j][i]), midX - offsetX, midY - offsetY);
                    } else {
                        gc.setFill(Color.BLACK);
                        gc.fillText(String.valueOf(adjacencyMatrix[i][j]), midX, midY);
                    }

                }
            }
        }

        // Draw nodes
        for (int i = 0; i < numVertices; i++) {
            if (highlightedNodes[0] != null && highlightedNodes[0] == i || highlightedNodes[1] != null && highlightedNodes[1] == i) {
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.BLUE);
            }
            gc.fillOval(nodePositionsX[i] - radius, nodePositionsY[i] - radius, 2 * radius, 2 * radius);

            // Draw node label
            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf((char) ('A' + i)), nodePositionsX[i] - 10, nodePositionsY[i] + 5);
        }
    }

    private boolean isEdgeHighlighted(int from, int to) {
        for (int[] edge : highlightedEdges) {
            if (edge[0] == from && edge[1] == to) {
                return true;
            }
        }
        return false;
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
                    String path = reconstructPath(nodeIndex, i, pathResult.next);
                    sb.append("To ").append((char) ('A' + i)).append(": ").append(distance).append(" via path: ").append(path).append("\n");
                }
            }
        }

        resultText.setText(sb.toString());
    }

    private void showPathBetweenNodes(int startNode, int endNode) {
        if (pathResult == null) {
            resultText.setText("Shortest paths not calculated yet.");
            return;
        }

        String startNodeLabel = String.valueOf((char) ('A' + startNode));
        String endNodeLabel = String.valueOf((char) ('A' + endNode));
        int pathLength = pathResult.distances[startNode][endNode];

        if (pathLength == Integer.MAX_VALUE / 2) {
            resultText.setText("No path exists between " + startNodeLabel + " and " + endNodeLabel);
            highlightedEdges.clear();
        } else {
            highlightedEdges.clear(); // Reset previous highlights
            String path = reconstructPath(startNode, endNode, pathResult.next);

            // Collect edges in the shortest path
            int current = startNode;
            while (current != endNode) {
                int next = pathResult.next[current][endNode];
                highlightedEdges.add(new int[]{current, next});
                current = next;
            }

            resultText.setText("Shortest path from " + startNodeLabel + " to " + endNodeLabel + " is " + pathLength + " via path: " + path);
        }

        renderGraph(graph.getAdjacencyMatrix());
    }

    public static String reconstructPath(int start, int end, int[][] next) {
        if (next[start][end] == -1) {
            return "No path exists";
        }
        StringBuilder path = new StringBuilder();
        int current = start;

        path.append((char) ('A' + current)); // Start node label
        while (current != end) {
            current = next[current][end];
            path.append(" -> ").append((char) ('A' + current));
        }
        return path.toString();
    }

    // Draw an arrowhead at the end of an edge
    private void drawArrowhead(GraphicsContext gc, double startX, double startY, double endX, double endY) {
        double arrowLength = 10;

        // Calculate the angle of the line
        double angle = Math.atan2(endY - startY, endX - startX);

        // Offset the start point to avoid overlap with the node's radius
        double offsetX = Math.cos(angle) * radius;
        double offsetY = Math.sin(angle) * radius;

        // Adjust the start and end positions to account for node radius
        double adjustedEndX = endX - offsetX;
        double adjustedEndY = endY - offsetY;

        // Draw the arrowhead
        double x1 = adjustedEndX - arrowLength * Math.cos(angle - Math.PI / 6);
        double y1 = adjustedEndY - arrowLength * Math.sin(angle - Math.PI / 6);
        double x2 = adjustedEndX - arrowLength * Math.cos(angle + Math.PI / 6);
        double y2 = adjustedEndY - arrowLength * Math.sin(angle + Math.PI / 6);

        gc.setFill(gc.getStroke());
        gc.fillPolygon(new double[]{adjustedEndX, x1, x2}, new double[]{adjustedEndY, y1, y2}, 3);
    }

}


