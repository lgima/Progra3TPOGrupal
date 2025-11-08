package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BacktrackingAlgorithms {
    public List<Map<String, Object>> findAllCycles(Graph graph, String startCity, int maxLength) {
        List<Map<String, Object>> allCycles = new ArrayList<>();
        List<String> currentPath = new ArrayList<>();
        currentPath.add(startCity);
        
        findCyclesBacktrack(graph, startCity, startCity, maxLength, currentPath, 0, allCycles);
        return allCycles;
    }

    private void findCyclesBacktrack(Graph graph, String current, String start, int maxLength,
                                   List<String> path, int totalDistance,
                                   List<Map<String, Object>> allCycles) {
        if (path.size() > 2 && current.equals(start)) {
            allCycles.add(Map.of(
                "cycle", new ArrayList<>(path),
                "totalDistance", totalDistance
            ));
            return;
        }

        if (path.size() >= maxLength) {
            return;
        }

        for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
            String nextCity = neighbor.getKey();
            if (nextCity.equals(start) && path.size() < 3) {
                continue;
            }
            
            if (!path.contains(nextCity) || (nextCity.equals(start) && path.size() > 2)) {
                path.add(nextCity);
                findCyclesBacktrack(graph, nextCity, start, maxLength, path,
                                  totalDistance + neighbor.getValue(), allCycles);
                path.remove(path.size() - 1);
            }
        }
    }
}
