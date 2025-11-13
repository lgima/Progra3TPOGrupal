package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GraphBasicAlgorithms {
    /**
     * bfs(graph, start)
     * - Recorrido en anchura usando una cola; devuelve el orden de visita.
     * Complejidad temporal: O(V + E).(num vertices + num aristas), lineal, ya que cada nodo y arista se procesa una vez.
     */
    public List<String> bfs(Graph graph, String start) {
        List<String> visited = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        
        // Empezar desde el nodo inicial
        queue.add(start);
        visited.add(start);

        // Procesar por niveles: extraer nodo, encolar vecinos no visitados
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : graph.getNeighbors(current).keySet()) {
                if (!visited.contains(neighbor)) {
                    // Marcar y encolar vecino para visitar más tarde
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

    /**
     * dfs(graph, start)
     * - Recorrido en profundidad (usa función recursiva auxiliar).
     * Complejidad temporal: O(V + E), (num vertices + num aristas), lineal, ya que cada nodo y arista se procesa una vez.
     */
    public List<String> dfs(Graph graph, String start) {
        List<String> visited = new ArrayList<>();
        // Llamada inicial a la recursión
        dfsRecursive(graph, start, visited);
        return visited;
    }

    /**
     * dfsRecursive(graph, current, visited)
     * - Auxiliar recursiva que visita vecinos no visitados en profundidad.
     */
    private void dfsRecursive(Graph graph, String current, List<String> visited) {
        // Visitar el nodo actual y luego explorar recursivamente cada vecino no visitado
        visited.add(current);
        for (String neighbor : graph.getNeighbors(current).keySet()) {
            if (!visited.contains(neighbor)) {
                dfsRecursive(graph, neighbor, visited);
            }
        }
    }
}
