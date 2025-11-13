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
        // Crear una lista para almacenar todos los ciclos encontrados
        List<Map<String, Object>> allCycles = new ArrayList<>();
        // Crear una lista para mantener el camino actual y agregar la ciudad inicial
        List<String> currentPath = new ArrayList<>();
        currentPath.add(startCity); // Si se elimina, no se iniciará el camino desde la ciudad inicial

        // Llamar a la función recursiva para explorar ciclos
        findCyclesBacktrack(graph, startCity, startCity, maxLength, currentPath, 0, allCycles);
        // Si se elimina, no se realizará la búsqueda de ciclos

        return allCycles; // Si se elimina, no se devolverán los ciclos encontrados
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
                "cycle", new ArrayList<>(path), // Si se elimina, no se almacenará el ciclo actual
                "totalDistance", totalDistance // Si se elimina, no se almacenará la distancia total del ciclo
            ));
            return; // Si se elimina, el ciclo se registrará múltiples veces
        }

        // No extender si alcanzamos la longitud permitida
        if (path.size() >= maxLength) {
            return; // Si se elimina, se podrían generar caminos más largos de lo permitido
        }

        // Explorar cada vecino; evitar ciclos triviales y ciudades ya en el camino (salvo el inicio)
        for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
            String nextCity = neighbor.getKey();
            if (nextCity.equals(start) && path.size() < 3) {
                // Evitar cerrar ciclo con menos de 3 nodos
                continue; // Si se elimina, se podrían registrar ciclos triviales
            }

            if (!path.contains(nextCity) || (nextCity.equals(start) && path.size() > 2)) {
                // Extender camino y recursar
                path.add(nextCity); // Si se elimina, no se extenderá el camino con la ciudad vecina
                findCyclesBacktrack(graph, nextCity, start, maxLength, path,
                                  totalDistance + neighbor.getValue(), allCycles); // Si se elimina, no se explorarán caminos desde la ciudad vecina
                // Backtrack: quitar el último nodo añadido
                path.remove(path.size() - 1); // Si se elimina, el camino no se restaurará correctamente al estado anterior
            }
        }
    }
}
