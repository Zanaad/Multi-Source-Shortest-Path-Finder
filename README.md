# Multi-Source Shortest Path Finder

## Overview

This project implements the **Floyd-Warshall algorithm** to solve the multi-source shortest path problem in weighted
graphs. It features an interactive visualization tool using **JavaFX**, with modes for matrix updates and graph
representations, offering an intuitive way to explore the algorithm’s functionality and results.


---
## Features

### 🔢 Matrix Visualization

- Step-by-step updates of the algorithm’s adjacency matrix.
- Highlights changes during each iteration to demonstrate the algorithm's logic.
- Displays intermediate states of shortest path calculations.

### 📊 Graph Visualization

- Graphical representation of nodes and edges with weights.
- Displays shortest paths between nodes once the algorithm processes the graph.
- **Interactive Features**:
    - Select a node to view its shortest paths to all other nodes.
    - Highlight paths between two selected nodes.

---

## 🚀 Installation

### Prerequisites

- **Java 17 or later**: Ensure Java is installed and configured on your system.
- **JavaFX SDK**: Add JavaFX to your development environment.
- **Maven or Gradle**: Dependency management tools for building the project.

  ### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/Zanaad/DAA

---



## How Floyd-Warshall Algorithm Works

The **Floyd-Warshall algorithm** is a dynamic programming approach used to find the shortest paths between all pairs of
nodes in a weighted graph.

### Steps:

1. **Initialization**:
    - Represent the graph with an adjacency matrix `dist[][]`, where `dist[i][j]` contains the weight of the edge
      between node `i` and node `j`.
    - Set `dist[i][j]` to infinity if there is no direct edge between `i` and `j`, and `dist[i][i] = 0` for all nodes.

2. **Iterative Updates**:
    - For each intermediate node `k`, check if a shorter path exists between nodes `i` and `j` via `k`. Update the
      distance matrix as:
      ```plaintext
      dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
      ```

3. **Final Result**:
    - After running through all possible intermediate nodes, `dist[i][j]` contains the shortest path from node `i` to
      node `j`.

### Time and Space Complexity:

- **Time Complexity**: O(V^3), where V is the number of nodes.
- **Space Complexity**: O(V^2), as it requires maintaining a distance matrix.


---

## Project Flow

1. **User Input**:
    - Input the number of vertices in the graph.
    - Add weights for the edges between vertices.
2. **Algorithm Execution**:
    - The Floyd-Warshall algorithm calculates the shortest paths and iteratively updates the adjacency matrix.
3. **Visualization**:
    - ***Matrix Visualization***: Observe step-by-step updates to the adjacency matrix.
    - ***Graph Visualization***: Interact with the graph, select nodes, and explore shortest paths.
4. **User Interaction**:
    - Switch between visualization modes.
    - Click on nodes to view the shortest paths and weights.

---

## Vizualization

Before selecting the type of visualization, we first specify the algorithm and the number of vertices, followed by defining the weights for the edges. 
The steps are outlined below:


![e para](https://github.com/user-attachments/assets/706bf341-4cfa-458a-9968-3728fcb45033)

![dytaa](https://github.com/user-attachments/assets/90d4cac8-f471-474d-920a-6f254b23bc68)

![image](https://github.com/user-attachments/assets/03addefd-bd43-4abf-b25f-0051c3ae3970)






[Video](https://github.com/user-attachments/assets/ed449e40-4e76-48d8-9d3b-cbaa2dbb73a7)



[Video](https://github.com/user-attachments/assets/8332a301-f624-4d9b-a094-e65ac31b19f5)



# Confidential

This project is developed by the authors below with all rights reserved.


# Authors

This project is developed by:

[Kaltrina Kurtaj](https://github.com/kaltrinakurtaj)

[Leonita Sinani](https://github.com/leonitaas)

[Yllka Kastrati](https://github.com/Yllka5)

[Zana Ademi](https://github.com/Zanaad)

---


