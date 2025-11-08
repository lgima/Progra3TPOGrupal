package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GraphBasicAlgorithms {
    public List<String> bfs(Graph graph, String start) {
        List<String> visited = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : graph.getNeighbors(current).keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

    public List<String> dfs(Graph graph, String start) {
        List<String> visited = new ArrayList<>();
        dfsRecursive(graph, start, visited);
        return visited;
    }

    private void dfsRecursive(Graph graph, String current, List<String> visited) {
        visited.add(current);
        for (String neighbor : graph.getNeighbors(current).keySet()) {
            if (!visited.contains(neighbor)) {
                dfsRecursive(graph, neighbor, visited);
            }
        }
    }
}
