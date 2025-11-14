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
        
        List<String> route = new ArrayList<>(); // Si se elimina, no se podrá almacenar la ruta construida
        
        // Conjunto de ciudades pendientes por visitar (inicialmente todas).
        // Costo de creación: O(V).
        Set<String> unvisited = new HashSet<>(graph.getVertices()); // Si se elimina, no se podrá rastrear las ciudades no visitadas
        
        int totalDistance = 0; // Si se elimina, no se podrá calcular la distancia total de la ruta
        String currentCity = startCity; // Si se elimina, no se podrá iniciar desde la ciudad inicial
        
        // Agregamos el punto de partida a la ruta y lo sacamos de pendientes.
        route.add(startCity); // Si se elimina, la ciudad inicial no se incluirá en la ruta
        unvisited.remove(startCity); // Si se elimina, la ciudad inicial podría ser visitada nuevamente

        // --- 2. Bucle Principal (Construcción de la Ruta) ---
        
        // Mientras queden ciudades sin visitar...
        while (!unvisited.isEmpty()) { // Si se elimina, no se recorrerán las ciudades no visitadas
            
            String nearestCity = null; // Si se elimina, no se podrá determinar la ciudad más cercana
            int minDistance = Integer.MAX_VALUE; // Si se elimina, no se podrá comparar las distancias

            // Paso A: (Greedy)
            // Buscamos la ciudad no visitada que esté más cerca de nuestra posición actual.
            // Este bucle se hace más pequeño con cada iteración externa.
            for (String candidateCity : unvisited) { // Si se elimina, no se evaluarán las ciudades no visitadas
                
                // Obtenemos el peso (distancia) entre la ciudad actual y la candidata.
                int distance = graph.getWeight(currentCity, candidateCity); // Si se elimina, no se calculará la distancia a la ciudad candidata
                
                // Si encontramos una más cercana que la actual "mejor", la guardamos.
                if (distance < minDistance) { // Si se elimina, no se actualizará la ciudad más cercana
                    minDistance = distance;
                    nearestCity = candidateCity;
                }
            }

            // Paso B: Transición
            // Nos movemos a la ciudad elegida.
            if (nearestCity != null) { // Si se elimina, no se avanzará a la siguiente ciudad
                route.add(nearestCity); // Si se elimina, la ciudad más cercana no se incluirá en la ruta
                unvisited.remove(nearestCity); // Si se elimina, la ciudad más cercana podría ser visitada nuevamente
                totalDistance += minDistance; // Si se elimina, no se acumulará la distancia total
                currentCity = nearestCity; // Si se elimina, no se actualizará la ciudad actual
            }
        }

        // 3. Cierre del Ciclo
        
        // El TSP requiere volver al punto de partida al final.
        int returnDistance = graph.getWeight(currentCity, startCity); // Si se elimina, no se calculará la distancia de retorno
        totalDistance += returnDistance; // Si se elimina, no se incluirá la distancia de retorno en el total
        route.add(startCity); // Si se elimina, no se cerrará el ciclo en la ruta

        return Map.of(
            "route", route, // Si se elimina, no se devolverá la ruta construida
            "totalDistance", totalDistance // Si se elimina, no se devolverá la distancia total
        );
    }
}