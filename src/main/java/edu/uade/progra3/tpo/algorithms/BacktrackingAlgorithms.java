package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BacktrackingAlgorithms {
    /**
     * findAllCycles(graph, startCity, maxLength)
     * - Busca por backtracking todos los ciclos simples que comienzan y terminan en startCity
     *   con longitud máxima maxLength.
     * Complejidad temporal: exponencial respecto a la longitud máxima y ramificación (O(b^L)).
     */
    public List<Map<String, Object>> findAllCycles(Graph graph, String startCity, int maxLength) {
        // Iniciar búsqueda desde la ciudad inicial, manteniendo el camino actual
        List<Map<String, Object>> allCycles = new ArrayList<>();
        List<String> currentPath = new ArrayList<>();
        currentPath.add(startCity);
        
        // Llamada recursiva 1 - para explorar ciclos
        findCyclesBacktrack(graph, startCity, startCity, maxLength, currentPath, 0, allCycles);
        return allCycles;
    }

    /**
     * findCyclesBacktrack(graph, current, start, maxLength, path, totalDistance, allCycles)
     * - Función recursiva que extiende el camino y registra ciclos válidos.
     */
    private void findCyclesBacktrack(Graph graph, String current, String start, int maxLength,
                                   List<String> path, int totalDistance,
                                   List<Map<String, Object>> allCycles) {
        // Si volvemos al inicio con al menos 3 nodos, registramos el ciclo
        if (path.size() > 2 && current.equals(start)) {
            allCycles.add(Map.of(
                "cycle", new ArrayList<>(path),
                "totalDistance", totalDistance
            ));
            return;
        }

        // No extender si alcanzamos la longitud permitida
        if (path.size() >= maxLength) {
            return;
        }

        // Explorar cada vecino; evitar ciclos triviales y ciudades ya en el camino (salvo el inicio)
        for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
            String nextCity = neighbor.getKey();
            if (nextCity.equals(start) && path.size() < 3) {
                // Evitar cerrar ciclo con menos de 3 nodos
                continue;
            }
            
            if (!path.contains(nextCity) || (nextCity.equals(start) && path.size() > 2)) {
                // Extender camino y recursar
                path.add(nextCity);
                findCyclesBacktrack(graph, nextCity, start, maxLength, path,
                                  totalDistance + neighbor.getValue(), allCycles);
                // Backtrack: quitar el último nodo añadido
                path.remove(path.size() - 1);
            }
        }
    }
}
