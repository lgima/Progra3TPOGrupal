package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BacktrackingAlgorithms {
    /**
     * findAllCycles(graph, startCity, maxLength)
     * - Busca por backtracking todos los ciclos simples que comienzan y terminan en startCity
     *   con longitud máxima maxLength.
     * Complejidad temporal: exponencial respecto a la longitud máxima y ramificación (O(b^L)). 
     * b es el número promedio de vecinos de cada ciudad y l es maxlenght, la longitud del camino.
     */
    public List<Map<String, Object>> findAllCycles(Graph graph, String startCity, int maxLength) {
        // Iniciar búsqueda desde la ciudad inicial, manteniendo el camino actual
        List<Map<String, Object>> allCycles = new ArrayList<>(); // Si se elimina, no se podrán almacenar los ciclos encontrados
        List<String> currentPath = new ArrayList<>(); // Si se elimina, no se podrá construir el camino actual
        currentPath.add(startCity); // Si se elimina, no se agregará la ciudad inicial al camino
        
        // Llamada recursiva 1 - para explorar ciclos
        findCyclesBacktrack(graph, startCity, startCity, maxLength, currentPath, 0, allCycles); // Si se elimina, no se iniciará la búsqueda de ciclos
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
        if (path.size() > 2 && current.equals(start)) { // Si se elimina, no se registrarán los ciclos válidos
            allCycles.add(Map.of(
                "cycle", new ArrayList<>(path), // Si se elimina, no se guardará el ciclo actual
                "totalDistance", totalDistance // Si se elimina, no se guardará la distancia total del ciclo
            ));
            return; // Si se elimina, la recursión continuará innecesariamente
        }
        // No extender si alcanzamos la longitud permitida
        if (path.size() >= maxLength) { // Si se elimina, se podrían generar caminos más largos de lo permitido
            return;
        }
        // Explorar cada vecino; evitar ciclos triviales y ciudades ya en el camino (salvo el inicio)
        for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) { // Si se elimina, no se explorarán los vecinos del nodo actual
            String nextCity = neighbor.getKey(); // Si se elimina, no se obtendrá el nombre de la ciudad vecina
            if (nextCity.equals(start) && path.size() < 3) { // Si se elimina, se podrían cerrar ciclos con menos de 3 nodos
                continue;
            }
            
            // Si me paso por una ciudad ya visitada, no la vuelvo a visitar
            if (!path.contains(nextCity) || (nextCity.equals(start) && path.size() > 2)) { // Si se elimina, se podrían visitar ciudades repetidas
                // Extender camino y recursar
                path.add(nextCity); // Si se elimina, no se agregará la ciudad al camino actual
                // Llamada recursiva 2 - para seguir explorando a partir de esta ciudad añadida
                findCyclesBacktrack(graph, nextCity, start, maxLength, path,
                                  totalDistance + neighbor.getValue(), allCycles); // Si se elimina, no se explorarán los caminos desde la ciudad añadida
                // Backtrack: quitar el último nodo añadido, el bucle for de arriba todavía tiene que probar otros vecinos.
                path.remove(path.size() - 1); // Si se elimina, no se deshará el último cambio en el camino
            }
        }
    }
}
