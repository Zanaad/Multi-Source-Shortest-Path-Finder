package algorithms;

import models.Graph;

public class FloydWarshall {
    public static int[][] findShortestPaths(Graph graph) {
        int vertices = graph.getVertices();
        int[][] dist = graph.getAdjacencyMatrix();

        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        return dist;
    }
}