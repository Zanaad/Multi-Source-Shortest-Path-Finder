package models;

import utils.Alerts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
    private int numVertices;
    private List<Edge> edges;
    private int[][] adjacencyMatrix;

    private static Graph instance;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
        this.adjacencyMatrix = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(adjacencyMatrix[i], Integer.MAX_VALUE / 2);
            adjacencyMatrix[i][i] = 0;
        }
        instance = this;
    }

    public static Graph getInstance() {
        if (instance == null) {
            Alerts.errorMessage("Graph is not initialized");
        }
        return instance;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        updateEdgesFromMatrix();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getVertices() {
        return numVertices;
    }

    private void updateEdgesFromMatrix() {
        edges.clear(); // Clear any existing edges
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j && adjacencyMatrix[i][j] != Integer.MAX_VALUE / 2) {
                    edges.add(new Edge(i, j, adjacencyMatrix[i][j]));
                }
            }
        }
    }

    public static void resetInstance() {
        instance = null;
    }

}