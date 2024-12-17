package algorithms;

import models.Graph;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall {
    public static class PathResult {
        public int[][] distances;
        public int[][] next;
    }

    public static PathResult findShortestPaths(Graph graph) {
        int numVertices = graph.getVertices();
        int[][] dist = copyMatrix(graph.getAdjacencyMatrix());
        int[][] next = new int[numVertices][numVertices];

        // Initialize next matrix: next[i][j] = j if there's a direct edge, otherwise -1
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (dist[i][j] != Integer.MAX_VALUE / 2 && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE / 2 && dist[k][j] != Integer.MAX_VALUE / 2) {
                        if (dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                            next[i][j] = next[i][k]; // Update next to follow the intermediate node
                        }
                    }
                }
            }
        }

        PathResult result = new PathResult();
        result.distances = dist;
        result.next = next;
        return result;
    }

    public static class StepResult {
        public int[][] matrix;
        public List<int[]> changedPositions;
    }

    // Generate the matrix at a specific step
    public static StepResult generateStep(Graph graph, int step) {
        int numVertices = graph.getVertices();
        int[][] dist = copyMatrix(graph.getAdjacencyMatrix());
        List<int[]> changes = new ArrayList<int[]>();

        for (int k = 0; k <= step; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    int newValue = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    if (newValue != dist[i][j]) {
                        dist[i][j] = newValue;
                        changes.add(new int[]{i, j});
                    }
                }
            }
        }

        StepResult result = new StepResult();
        result.matrix = dist;
        result.changedPositions = changes;
        return result;
    }

    public static int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone(); // Copies each row separately
        }
        return copy;
    }
}

