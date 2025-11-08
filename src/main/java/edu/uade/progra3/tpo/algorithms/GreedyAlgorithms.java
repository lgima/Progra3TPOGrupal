package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GreedyAlgorithms {
    public Map<String, Object> greedyTSP(Graph graph, String startCity) {
        List<String> route = new ArrayList<>();
        Set<String> unvisited = new HashSet<>(graph.getVertices());
        int totalDistance = 0;
        String currentCity = startCity;
        
        route.add(startCity);
        unvisited.remove(startCity);

        while (!unvisited.isEmpty()) {
            String nearestCity = null;
            int minDistance = Integer.MAX_VALUE;

            for (String city : unvisited) {
                int distance = graph.getWeight(currentCity, city);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCity = city;
                }
            }

            route.add(nearestCity);
            unvisited.remove(nearestCity);
            totalDistance += minDistance;
            currentCity = nearestCity;
        }

        // Add return to start
        totalDistance += graph.getWeight(currentCity, startCity);
        route.add(startCity);

        return Map.of("route", route, "totalDistance", totalDistance);
    }
}
