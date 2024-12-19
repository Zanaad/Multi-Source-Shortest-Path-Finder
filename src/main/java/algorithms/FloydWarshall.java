package algorithms;

import models.Graph;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall {
    public static class PathResult {
        public int[][] distances;       // Shortest distances matrix
        public int[][] next;            // Next node for path reconstruction
        public List<int[]> changedPositions; // List of positions updated in the last step
    }

    public static PathResult runFloydWarshall(Graph graph, int stepLimit) {
        int numVertices = graph.getVertices();
        int[][] dist = copyMatrix(graph.getAdjacencyMatrix());
        int[][] next = new int[numVertices][numVertices];
        List<int[]> changes = new ArrayList<>();

        // Initialize 'next' matrix
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (dist[i][j] != Integer.MAX_VALUE / 2 && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        // Perform Floyd-Warshall, limiting to the current step
        int endStep = (stepLimit == -1) ? numVertices : stepLimit;
        for (int k = 0; k <= endStep && k < numVertices; k++) {
            changes.clear(); // Reset changes for this step
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE / 2 && dist[k][j] != Integer.MAX_VALUE / 2) {
                        int newValue = dist[i][k] + dist[k][j];
                        if (dist[i][j] > newValue) {
                            dist[i][j] = newValue;
                            next[i][j] = next[i][k];
                            changes.add(new int[]{i, j});
                        }
                    }
                }
            }
        }

        PathResult result = new PathResult();
        result.distances = dist;
        result.next = next;
        result.changedPositions = new ArrayList<>(changes); // Pass only the changes from the last step
        return result;
    }

    private static int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
} 