package algorithms;

import models.Edge;
import models.Graph;

import java.util.*;

public class JohnsonsAlgorithm {
    public static int[][] findShortestPaths(Graph graph) {
        int vertices = graph.getVertices();
        Graph augmentedGraph = new Graph(vertices + 1);

        // Copy edges and add zero-weight edges from new vertex to all others
        for (Edge edge : graph.getEdges()) {
            augmentedGraph.addEdge(edge.getSource(), edge.getDestination(), edge.getWeight());
        }

        for (int i = 0; i < vertices; i++) {
            augmentedGraph.addEdge(vertices, i, 0);
        }

        // Run Bellman-Ford to calculate potential (h-values)
        int[] h = new int[vertices + 1];
        if (!bellmanFord(augmentedGraph, vertices, h)) {
            throw new IllegalArgumentException("Graph contains a negative-weight cycle");
        }

        // Reweight edges
        for (Edge edge : graph.getEdges()) {
            int newWeight = edge.getWeight() + h[edge.getSource()] - h[edge.getDestination()];
//            edge = new Edge(edge.getSource(), edge.getDestination(), newWeight);
        }

        // Dijkstra for each vertex
        int[][] shortestPaths = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            shortestPaths[i] = dijkstra(graph, i);
        }

        return shortestPaths;
    }

    private static boolean bellmanFord(Graph graph, int source, int[] dist) {
        Arrays.fill(dist, Integer.MAX_VALUE / 2);
        dist[source] = 0;

        for (int i = 0; i < graph.getVertices() - 1; i++) {
            for (Edge edge : graph.getEdges()) {
                if (dist[edge.getSource()] + edge.getWeight() < dist[edge.getDestination()]) {
                    dist[edge.getDestination()] = dist[edge.getSource()] + edge.getWeight();
                }
            }
        }

        for (Edge edge : graph.getEdges()) {
            if (dist[edge.getSource()] + edge.getWeight() < dist[edge.getDestination()]) {
                return false; // Negative-weight cycle detected
            }
        }

        return true;
    }

    private static int[] dijkstra(Graph graph, int source) {
        int vertices = graph.getVertices();
        int[] dist = new int[vertices];
        boolean[] visited = new boolean[vertices];

        Arrays.fill(dist, Integer.MAX_VALUE / 2);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (visited[u]) continue;
            visited[u] = true;

            for (Edge edge : graph.getEdges()) {
                if (edge.getSource() == u && !visited[edge.getDestination()]) {
                    int v = edge.getDestination();
                    int weight = edge.getWeight();

                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        pq.add(new int[]{v, dist[v]});
                    }
                }
            }
        }

        return dist;
    }
}
