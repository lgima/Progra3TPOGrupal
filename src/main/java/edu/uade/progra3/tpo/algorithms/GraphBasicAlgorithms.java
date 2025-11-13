package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GraphBasicAlgorithms {
    /**
     * bfs(graph, start)
     * - Recorrido en anchura usando una cola; devuelve el orden de visita.
     * Complejidad temporal: O(V + E).
     */
    public List<String> bfs(Graph graph, String start) {
        // Crear una lista para almacenar los nodos visitados
        List<String> visited = new ArrayList<>(); // Si se elimina, no se podrá rastrear los nodos visitados
        // Crear una cola para gestionar el recorrido en anchura
        Queue<String> queue = new LinkedList<>(); // Si se elimina, no se podrá gestionar el orden de visita

        // Empezar desde el nodo inicial
        queue.add(start); // Si se elimina, no se iniciará el recorrido desde el nodo inicial
        visited.add(start); // Si se elimina, el nodo inicial no será marcado como visitado

        // Procesar por niveles: extraer nodo, encolar vecinos no visitados
        while (!queue.isEmpty()) { // Si se elimina, no se recorrerán los nodos
            String current = queue.poll(); // Si se elimina, no se extraerá el nodo actual de la cola

            for (String neighbor : graph.getNeighbors(current).keySet()) { // Si se elimina, no se explorarán los vecinos del nodo actual
                if (!visited.contains(neighbor)) { // Si se elimina, se podrían visitar nodos repetidos
                    visited.add(neighbor); // Si se elimina, el vecino no será marcado como visitado
                    queue.add(neighbor); // Si se elimina, el vecino no será encolado para su visita posterior
                }
            }
        }

        return visited; // Si se elimina, no se devolverá el orden de visita
    }

    /**
     * dfs(graph, start)
     * - Recorrido en profundidad (usa función recursiva auxiliar).
     * Complejidad temporal: O(V + E).
     */
    public List<String> dfs(Graph graph, String start) {
        // Crear una lista para almacenar los nodos visitados
        List<String> visited = new ArrayList<>(); // Si se elimina, no se podrá rastrear los nodos visitados
        // Llamada inicial a la recursión
        dfsRecursive(graph, start, visited); // Si se elimina, no se iniciará el recorrido en profundidad
        return visited; // Si se elimina, no se devolverá el orden de visita
    }

    /**
     * dfsRecursive(graph, current, visited)
     * - Auxiliar recursiva que visita vecinos no visitados en profundidad.
     */
    private void dfsRecursive(Graph graph, String current, List<String> visited) {
        // Visitar el nodo actual
        visited.add(current); // Si se elimina, el nodo actual no será marcado como visitado

        for (String neighbor : graph.getNeighbors(current).keySet()) { // Si se elimina, no se explorarán los vecinos del nodo actual
            if (!visited.contains(neighbor)) { // Si se elimina, se podrían visitar nodos repetidos
                dfsRecursive(graph, neighbor, visited); // Si se elimina, no se recorrerán los vecinos en profundidad
            }
        }
    }
}
