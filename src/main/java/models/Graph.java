package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
    private int vertices;
    private List<Edge> edges;
    private int[][] adjacencyMatrix;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.adjacencyMatrix = new int[vertices][vertices];

        for (int i = 0; i < vertices; i++) {
            Arrays.fill(adjacencyMatrix[i], Integer.MAX_VALUE / 2);
            adjacencyMatrix[i][i] = 0;
        }
    }

    public void addEdge(int source, int destination, int weight) {
        edges.add(new Edge(source, destination, weight));
        adjacencyMatrix[source][destination] = weight;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getVertices() {
        return vertices;
    }
}