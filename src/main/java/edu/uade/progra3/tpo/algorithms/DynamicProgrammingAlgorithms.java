package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class DynamicProgrammingAlgorithms {
    public List<Map<String, Object>> findAllPaths(Graph graph, String start, String end, int maxLength) {
        List<Map<String, Object>> allPaths = new ArrayList<>();
        Map<String, Integer> memo = new HashMap<>();
        List<String> currentPath = new ArrayList<>();
        
        findPathsRecursive(graph, start, end, maxLength, currentPath, 0, allPaths, memo);
        return allPaths;
    }

    private void findPathsRecursive(Graph graph, String current, String end, int maxLength,
                                  List<String> currentPath, int totalDistance,
                                  List<Map<String, Object>> allPaths, Map<String, Integer> memo) {
        currentPath.add(current);

        if (current.equals(end) && currentPath.size() <= maxLength) {
            allPaths.add(Map.of(
                "route", new ArrayList<>(currentPath),
                "totalDistance", totalDistance
            ));
        } else if (currentPath.size() < maxLength) {
            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
                if (!currentPath.contains(neighbor.getKey())) {
                    findPathsRecursive(graph, neighbor.getKey(), end, maxLength,
                                     currentPath, totalDistance + neighbor.getValue(),
                                     allPaths, memo);
                }
            }
        }

        currentPath.remove(currentPath.size() - 1);
    }
}
