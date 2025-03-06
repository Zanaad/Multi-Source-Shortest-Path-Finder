package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void testGraphInitializationCorrectly() {
        int numVertices = 4;
        Graph graph = new Graph(numVertices);

        assertNotNull(graph);
        assertEquals(numVertices, graph.getVertices());

        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i == j) {
                    assertEquals(0, adjacencyMatrix[i][j], "Self-loops should be 0.");
                } else {
                    assertEquals(Integer.MAX_VALUE / 2, adjacencyMatrix[i][j],
                            "Unconnected nodes should have max weight.");
                }
            }
        }
    }

    @Test
    void testGraphHandlesSingleVertex() {
        Graph graph = new Graph(1);

        assertEquals(1, graph.getVertices());

        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        assertEquals(0, adjacencyMatrix[0][0]);

        assertTrue(graph.getEdges().isEmpty(), "Graph with one vertex should not have any edges.");
    }

    @Test
    void testGetInstanceWithoutInitialization() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Graph graph = Graph.getInstance();
        });
        assertTrue(exception.getMessage().contains("Graph is not initialized"));
    }
    @BeforeEach
    void setUp() {
        Graph.resetInstance();
    }
}
