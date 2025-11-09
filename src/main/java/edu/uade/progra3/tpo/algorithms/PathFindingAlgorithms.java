package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class PathFindingAlgorithms {
    public Map<String, Object> dijkstra(Graph graph, String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
            Comparator.comparingInt(distances::get));
        Set<String> visited = new HashSet<>();

        // Initialize distances
        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.offer(start);

        // Main algorithm
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) {
                break;
            }
            
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
                String next = neighbor.getKey();
                int newDistance = distances.get(current) + neighbor.getValue();

                if (newDistance < distances.get(next)) {
                    distances.put(next, newDistance);
                    previousNodes.put(next, current);
                    queue.offer(next);
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(0, current);
            current = previousNodes.get(current);
        }

        return Map.of(
            "path", path,
            "distance", distances.get(end),
            "explored", visited
        );
    }
}
