package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GreedyAlgorithms {

    /**
     * Algoritmo Greedy para el Problema del Viajante(TSP).
     * Aplicamos la heurística del "Vecino Más Cercano".
     * Análisis de Complejidad Temporal: O(V^2)
     * 
     * Bucle Externo: Se ejecuta V veces (una vez por cada ciudad a visitar).
     * En cada paso, busca entre las ciudades restantes la más cercana.
     * (Primero busca entre V-1, luego V-2, etc.)
     * Cálculo: La suma de operaciones es V + (V-1) + (V-2)... resulta en O(V^2).

     * @param graph El grafo completo (todas las ciudades conectadas con todas).
     * @param startCity Ciudad de inicio.
     * @return Mapa con la ruta aproximada y la distancia total.
     */
    public Map<String, Object> greedyTSP(Graph graph, String startCity) {
        
        // --- 1. Estructuras de Datos e Inicialización ---
        
        List<String> route = new ArrayList<>();
        
        // Conjunto de ciudades pendientes por visitar (inicialmente todas).
        // Costo de creación: O(V).
        Set<String> unvisited = new HashSet<>(graph.getVertices());
        
        int totalDistance = 0;
        String currentCity = startCity;
        
        // Agregamos el punto de partida a la ruta y lo sacamos de pendientes.
        route.add(startCity);
        unvisited.remove(startCity);

        // --- 2. Bucle Principal (Construcción de la Ruta) ---
        
        // Mientras queden ciudades sin visitar...
        while (!unvisited.isEmpty()) {
            
            String nearestCity = null;
            int minDistance = Integer.MAX_VALUE;

            // Paso A: (Greedy)
            // Buscamos la ciudad no visitada que esté más cerca de nuestra posición actual.
            // Este bucle se hace más pequeño con cada iteración externa.
            for (String candidateCity : unvisited) {
                
                // Obtenemos el peso (distancia) entre la ciudad actual y la candidata.
                int distance = graph.getWeight(currentCity, candidateCity);
                
                // Si encontramos una más cercana que la actual "mejor", la guardamos.
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCity = candidateCity;
                }
            }

            // Paso B: Transición
            // Nos movemos a la ciudad elegida.
            if (nearestCity != null) {
                route.add(nearestCity);            // Registrar en la ruta
                unvisited.remove(nearestCity);     // Marcar como visitada
                totalDistance += minDistance;      // Sumar costo
                currentCity = nearestCity;         // Actualizar posición actual
            }
        }

        // 3. Cierre del Ciclo
        
        // El TSP requiere volver al punto de partida al final.
        int returnDistance = graph.getWeight(currentCity, startCity);
        totalDistance += returnDistance;
        route.add(startCity);

        return Map.of(
            "route", route, 
            "totalDistance", totalDistance
        );
    }
}