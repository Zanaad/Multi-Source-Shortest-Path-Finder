package algorithms;

import models.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloydWarshallTest {
    //A successful case – Tests the algorithm on a normal graph and verifies correct shortest paths.
    @Test
    void testValidGraph() {
        int numVertices = 4;
        Graph graph = new Graph(numVertices);

        int[][] adjacencyMatrix = {
                {0, 3, Integer.MAX_VALUE / 2, 5},
                {2, 0, Integer.MAX_VALUE / 2, 4},
                {Integer.MAX_VALUE / 2, 1, 0, Integer.MAX_VALUE / 2},
                {Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2, 2, 0}
        };
        graph.setAdjacencyMatrix(adjacencyMatrix);

        FloydWarshall.PathResult result = FloydWarshall.runFloydWarshall(graph, -1);

        assertEquals(0, result.distances[0][0]);
        assertEquals(3, result.distances[0][1]);
        assertEquals(7, result.distances[0][2]);
        assertEquals(5, result.distances[0][3]);
        assertEquals(2, result.distances[1][0]);
        assertEquals(0, result.distances[1][1]);
        assertEquals(6, result.distances[1][2]);
        assertEquals(4, result.distances[1][3]);
        assertEquals(3, result.distances[2][0]);
        assertEquals(1, result.distances[2][1]);
        assertEquals(0, result.distances[2][2]);
        assertEquals(5, result.distances[2][3]);
        assertEquals(5, result.distances[3][0]);
        assertEquals(3, result.distances[3][1]);
        assertEquals(2, result.distances[3][2]);
        assertEquals(0, result.distances[3][3]);
    }

    //An edge case - Tests the algorithm on a graph where all vertices are disconnected.
    @Test
    void testGraphWithNoEdges() {
        int numVertices = 3;
        Graph graph = new Graph(numVertices);

        int[][] adjacencyMatrix = {
                {0, Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2},
                {Integer.MAX_VALUE / 2, 0, Integer.MAX_VALUE / 2},
                {Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2, 0}
        };
        graph.setAdjacencyMatrix(adjacencyMatrix);

        FloydWarshall.PathResult result = FloydWarshall.runFloydWarshall(graph, -1);

        assertEquals(0, result.distances[0][0]);
        assertEquals(Integer.MAX_VALUE / 2, result.distances[0][1]);
        assertEquals(Integer.MAX_VALUE / 2, result.distances[0][2]);
        assertEquals(Integer.MAX_VALUE / 2, result.distances[1][0]);
        assertEquals(0, result.distances[1][1]);
        assertEquals(Integer.MAX_VALUE / 2, result.distances[1][2]);
        assertEquals(Integer.MAX_VALUE / 2, result.distances[2][0]);
        assertEquals(Integer.MAX_VALUE / 2, result.distances[2][1]);
        assertEquals(0, result.distances[2][2]);
    }

    //An error case -  Ensures the algorithm correctly detects and throws an exception for a negative weight cycle.
    @Test
    void testNegativeWeightCycle() {
        int numVertices = 3;
        Graph graph = new Graph(numVertices);

        int[][] adjacencyMatrix = {
                {0, 1, Integer.MAX_VALUE / 2},
                {Integer.MAX_VALUE / 2, 0, -2},
                {-1, Integer.MAX_VALUE / 2, 0}
        };
        graph.setAdjacencyMatrix(adjacencyMatrix);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            FloydWarshall.runFloydWarshall(graph, -1);
        });

        assertTrue(exception.getMessage().contains("Negative weight cycle detected"));
    }
}
