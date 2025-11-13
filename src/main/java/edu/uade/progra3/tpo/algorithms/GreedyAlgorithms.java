package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GreedyAlgorithms {
    /**
     * greedyTSP(graph, startCity)
     * - Heurística del vecino más cercano: desde startCity selecciona iterativamente
     *   la ciudad no visitada más cercana.
     * - Es una aproximación rápida pero no garantiza óptimo.
     * Complejidad temporal: O(V^2).
     */
    public Map<String, Object> greedyTSP(Graph graph, String startCity) {
        // Crear una lista para almacenar la ruta
        List<String> route = new ArrayList<>(); // Si se elimina, no se podrá construir la ruta
        // Crear un conjunto para las ciudades no visitadas
        Set<String> unvisited = new HashSet<>(graph.getVertices()); // Si se elimina, no se podrá rastrear las ciudades no visitadas
        // Inicializar la distancia total a 0
        int totalDistance = 0; // Si se elimina, no se podrá calcular la distancia total
        // Establecer la ciudad actual como la ciudad inicial
        String currentCity = startCity; // Si se elimina, no se podrá iniciar el recorrido

        // Iniciar ruta y marcar inicio como visitado
        route.add(startCity); // Si se elimina, no se agregará la ciudad inicial a la ruta
        unvisited.remove(startCity); // Si se elimina, la ciudad inicial no será marcada como visitada

        // Iterar hasta visitar todas las ciudades
        while (!unvisited.isEmpty()) { // Si se elimina, no se recorrerán las ciudades no visitadas
            String nearestCity = null; // Si se elimina, no se podrá rastrear la ciudad más cercana
            int minDistance = Integer.MAX_VALUE; // Si se elimina, no se podrá comparar distancias

            // Buscar la ciudad no visitada más cercana al currentCity
            for (String city : unvisited) { // Si se elimina, no se evaluarán las ciudades no visitadas
                int distance = graph.getWeight(currentCity, city); // Si se elimina, no se calculará la distancia a las ciudades
                if (distance < minDistance) { // Si se elimina, no se identificará la ciudad más cercana
                    minDistance = distance;
                    nearestCity = city;
                }
            }

            // Moverse a la ciudad seleccionada, actualizar ruta y distancia total
            route.add(nearestCity); // Si se elimina, no se agregará la ciudad más cercana a la ruta
            unvisited.remove(nearestCity); // Si se elimina, la ciudad más cercana no será marcada como visitada
            totalDistance += minDistance; // Si se elimina, no se actualizará la distancia total
            currentCity = nearestCity; // Si se elimina, no se actualizará la ciudad actual
        }

        // Volver al punto de partida y sumar la distancia de retorno
        totalDistance += graph.getWeight(currentCity, startCity); // Si se elimina, no se calculará la distancia de retorno
        route.add(startCity); // Si se elimina, no se cerrará el ciclo en la ruta

        // Devolver la ruta y la distancia total
        return Map.of("route", route, "totalDistance", totalDistance); // Si se elimina, no se devolverán los resultados
    }
}
