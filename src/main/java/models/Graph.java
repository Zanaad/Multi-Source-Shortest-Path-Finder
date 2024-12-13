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
            Alerts.errorMessage("Graph instance not initialized. Call getInstance(int numVertices) first.");
        }
        return instance;
    }


    public void addEdge(int source, int destination, int weight) {
        edges.add(new Edge(source, destination, weight));
        adjacencyMatrix[source][destination] = weight;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getVertices() {
        return numVertices;
    }

    public void setVertices(int numVertices) {
        this.numVertices = numVertices;
    }
}