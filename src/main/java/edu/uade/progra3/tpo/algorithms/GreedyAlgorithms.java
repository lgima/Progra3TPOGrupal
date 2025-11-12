package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GreedyAlgorithms {
    /**
     * greedyTSP(graph, startCity)
     * - Heurística del vecino más cercano: desde startCity selecciona iterativamente
     *   la ciudad no visitada más cercana.
     * - Es una aproximación rápida pero no garantiza óptimo.
     * Complejidad temporal: O(V^2) (en cada paso se examinan hasta O(V) candidatos).
     */
    public Map<String, Object> greedyTSP(Graph graph, String startCity) {
        List<String> route = new ArrayList<>();
        Set<String> unvisited = new HashSet<>(graph.getVertices());
        int totalDistance = 0;
        String currentCity = startCity;
        
        // Iniciar ruta y marcar inicio como visitado
        route.add(startCity);
        unvisited.remove(startCity);

        // Iterar hasta visitar todas las ciudades
        while (!unvisited.isEmpty()) {
            String nearestCity = null;
            int minDistance = Integer.MAX_VALUE;

            // Buscar la ciudad no visitada más cercana al currentCity
            for (String city : unvisited) {
                int distance = graph.getWeight(currentCity, city);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCity = city;
                }
            }

            // Moverse a la ciudad seleccionada, actualizar ruta y distancia total
            route.add(nearestCity);
            unvisited.remove(nearestCity);
            totalDistance += minDistance;
            currentCity = nearestCity;
        }

        // Volver al punto de partida y sumar la distancia de retorno
        totalDistance += graph.getWeight(currentCity, startCity);
        route.add(startCity);

        return Map.of("route", route, "totalDistance", totalDistance);
    }
}
