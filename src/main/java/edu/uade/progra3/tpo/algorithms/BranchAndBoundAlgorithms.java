package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BranchAndBoundAlgorithms {
    private int[][] distanceMatrix;
    private List<String> cities;
    private int n;
    private int finalCost;
    private List<String> finalPath;

    public Map<String, Object> tspBranchAndBound(Graph graph, List<String> citiesToVisit) {
        initialize(graph, citiesToVisit);
        
        boolean[] visited = new boolean[n];
        visited[0] = true;
        List<String> currentPath = new ArrayList<>();
        currentPath.add(cities.get(0));
        
        branchAndBound(visited, currentPath, 0, 1);

        return Map.of(
            "route", finalPath,
            "totalDistance", finalCost
        );
    }

    private void initialize(Graph graph, List<String> citiesToVisit) {
        this.cities = new ArrayList<>(citiesToVisit);
        this.n = cities.size();
        this.distanceMatrix = new int[n][n];
        this.finalCost = Integer.MAX_VALUE;
        this.finalPath = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    distanceMatrix[i][j] = graph.getWeight(cities.get(i), cities.get(j));
                }
            }
        }
    }

    private void branchAndBound(boolean[] visited, List<String> currentPath, 
                              int currentCost, int level) {
        if (level == n) {
            int lastCity = cities.indexOf(currentPath.get(currentPath.size() - 1));
            int returnCost = distanceMatrix[lastCity][0];
            
            if (currentCost + returnCost < finalCost) {
                finalCost = currentCost + returnCost;
                finalPath = new ArrayList<>(currentPath);
                finalPath.add(cities.get(0));
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                int lastCity = cities.indexOf(currentPath.get(currentPath.size() - 1));
                int newCost = currentCost + distanceMatrix[lastCity][i];
                
                if (newCost < finalCost) {
                    visited[i] = true;
                    currentPath.add(cities.get(i));
                    
                    branchAndBound(visited, currentPath, newCost, level + 1);
                    
                    visited[i] = false;
                    currentPath.remove(currentPath.size() - 1);
                }
            }
        }
    }
}
