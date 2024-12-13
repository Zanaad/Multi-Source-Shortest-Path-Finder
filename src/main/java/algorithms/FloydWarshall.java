package algorithms;

import models.Graph;

public class FloydWarshall {
    public static int[][] findShortestPaths(Graph graph) {
        int numVertices = graph.getVertices();
        int[][] dist = graph.getAdjacencyMatrix();

        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        return dist;
    }

    public static int[][][] findShortestPathsStepByStep(Graph graph) {
        int numVertices = graph.getVertices();
        int[][] dist = graph.getAdjacencyMatrix();
        int[][][] steps = new int[numVertices + 1][numVertices][numVertices];

        // Initialize step 0 with the original adjacency matrix
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                steps[0][i][j] = dist[i][j];
            }
        }

        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }

            // Save the matrix after this step
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    steps[k + 1][i][j] = dist[i][j];
                }
            }
        }

        return steps;
    }
}