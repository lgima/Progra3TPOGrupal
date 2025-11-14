package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GreedyAlgorithms {

    /**
     * Algoritmo Greedy para el Problema del Viajante (TSP).
     * Heurística del "Vecino Más Cercano".
     *
     * Complejidad temporal: O(V^2)
     * - Bucle externo: como mucho V iteraciones.
     * - En cada iteración, se recorre el conjunto de ciudades no visitadas.
     *
     * IMPORTANTE:
     * Si el grafo no es completo (no hay aristas entre todas las ciudades),
     * el algoritmo puede quedar en una ciudad desde la que no exista vecino
     * no visitado alcanzable. En ese caso corta el recorrido y devuelve
     * la mejor ruta parcial que encontró, intentando volver al inicio.
     *
     * @param graph grafo de ciudades (no necesariamente completo).
     * @param startCity ciudad de inicio.
     * @return Mapa con la ruta aproximada y la distancia total.
     */
    public Map<String, Object> greedyTSP(Graph graph, String startCity) {
        // --- 1. Inicialización ---
        List<String> route = new ArrayList<>(); // Almacena la ruta construida. Si se elimina, no se podrá devolver el resultado.
        Set<String> unvisited = new HashSet<>(graph.getVertices()); // Conjunto de ciudades no visitadas. Si se elimina, no se podrá rastrear qué ciudades quedan por visitar.
        int totalDistance = 0; // Acumula la distancia total de la ruta. Si se elimina, no se podrá calcular el costo total.
        String currentCity = startCity; // Ciudad actual en el recorrido. Si se elimina, el algoritmo no sabrá desde dónde continuar.

        // Validar que la ciudad inicial exista
        if (!unvisited.contains(startCity)) {
            throw new IllegalArgumentException("La ciudad inicial no existe en el grafo."); // Valida que la ciudad inicial exista. Si se elimina, el algoritmo podría fallar con una entrada inválida.
        }

        route.add(startCity); // Agrega la ciudad inicial a la ruta. Si se elimina, la ruta estará incompleta.
        unvisited.remove(startCity); // Marca la ciudad inicial como visitada. Si se elimina, el algoritmo podría visitar la ciudad inicial varias veces.

        // --- 2. Construcción de la ruta (vecino más cercano) ---
        while (!unvisited.isEmpty()) {
            String nearestCity = null; // Almacena la ciudad más cercana. Si se elimina, el algoritmo no podrá determinar el siguiente paso.
            int minDistance = Integer.MAX_VALUE; // Almacena la distancia mínima encontrada. Si se elimina, el algoritmo no podrá comparar distancias.

            for (String candidateCity : unvisited) {
                int distance = graph.getWeight(currentCity, candidateCity); // Obtiene la distancia a la ciudad candidata. Si se elimina, el algoritmo no podrá evaluar las ciudades.
                if (distance > 0 && distance < minDistance) {
                    minDistance = distance; // Actualiza la distancia mínima. Si se elimina, el algoritmo no encontrará la ciudad más cercana.
                    nearestCity = candidateCity; // Actualiza la ciudad más cercana. Si se elimina, el algoritmo no podrá continuar el recorrido.
                }
            }

            // Si no hay ciudad no visitada alcanzable, cortamos el greedy.
            if (nearestCity == null) {
                break; // Si no hay ciudad alcanzable, termina el recorrido. Si se elimina, el algoritmo podría entrar en un bucle infinito.
            }

            route.add(nearestCity); // Agrega la ciudad más cercana a la ruta. Si se elimina, la ruta estará incompleta.
            unvisited.remove(nearestCity); // Marca la ciudad como visitada. Si se elimina, el algoritmo podría visitar ciudades repetidas.
            totalDistance += minDistance; // Acumula la distancia recorrida. Si se elimina, el costo total será incorrecto.
            currentCity = nearestCity; // Actualiza la ciudad actual. Si se elimina, el algoritmo no podrá continuar el recorrido.
        }

        // --- 3. Cierre del ciclo (si es posible volver al inicio) ---
        int returnDistance = graph.getWeight(currentCity, startCity); // Calcula la distancia de regreso a la ciudad inicial. Si se elimina, el algoritmo no podrá cerrar el ciclo.
        if (returnDistance > 0) {
            totalDistance += returnDistance; // Agrega la distancia de regreso al costo total. Si se elimina, el costo será incorrecto.
            route.add(startCity); // Cierra el ciclo añadiendo la ciudad inicial. Si se elimina, la ruta estará incompleta.
        }
        // Si no hay arista de regreso, simplemente devolvemos la ruta abierta.

        return Map.of(
            "route", route, // Devuelve la ruta construida. Si se elimina, el resultado no incluirá la ruta.
            "totalDistance", totalDistance // Devuelve la distancia total. Si se elimina, el resultado no incluirá el costo.
        );
    }
}
