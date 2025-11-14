package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class GraphBasicAlgorithms {

    /**
     * BFS (Breadth-First Search) - Búsqueda en Anchura.
     * Explora el grafo "por capas" o niveles: primero vecinos directos, luego vecinos de vecinos.
     * Análisis de Complejidad Temporal: O(V + E)
     * 
     * O(V): Cada vértice entra y sale de la cola exactamente una vez.
     * O(E): Recorremos la lista de adyacencia de cada vértice. En total procesamos todas las aristas.
     */

    public List<String> bfs(Graph graph, String start) {
        // 1. Estructuras de Datos 
        
        // Lista para devolver el orden de visita final
        List<String> traversalOrder = new ArrayList<>(); // Si se elimina, no se podrá almacenar el orden de visita
        
        // Set para control rápido de visitados (Búsqueda O(1)). 
        Set<String> visited = new HashSet<>(); // Si se elimina, no se podrá rastrear los nodos visitados
        
        // Cola para gestionar los pendientes por visitar (Nivel por nivel)
        Queue<String> queue = new LinkedList<>(); // Si se elimina, no se podrá gestionar los nodos pendientes por visitar
        
        // 2. Inicialización
        
        // Empezar desde el nodo inicial
        queue.add(start); // Si se elimina, no se agregará el nodo inicial a la cola
        visited.add(start); // Si se elimina, el nodo inicial podría ser visitado múltiples veces
        traversalOrder.add(start); // Si se elimina, el nodo inicial no se incluirá en el resultado

        // 3. Bucle Principal (Exploración por Niveles)
        while (!queue.isEmpty()) { // Si se elimina, no se recorrerán los nodos del grafo
            
            // Paso A: Desencolar (Sacar el primero de la fila)
            String current = queue.poll(); // Si se elimina, no se procesará el nodo actual
            
            // Paso B: Explorar Vecinos
            for (String neighbor : graph.getNeighbors(current).keySet()) { // Si se elimina, no se explorarán los vecinos del nodo actual
                
                // Si el vecino no ha sido descubierto...
                if (!visited.contains(neighbor)) { // Si se elimina, se podrían visitar nodos repetidos
                    // 1. Marcar como visitado (para no volver a meterlo)
                    visited.add(neighbor); // Si se elimina, el nodo vecino podría ser visitado múltiples veces
                    
                    // 2. Guardar en el resultado
                    traversalOrder.add(neighbor); // Si se elimina, el nodo vecino no se incluirá en el resultado
                    
                    // 3. Encolar para explorar sus vecinos más tarde
                    queue.add(neighbor); // Si se elimina, no se explorarán los vecinos del nodo vecino
                }
            }
        }
        return traversalOrder; // Si se elimina, no se devolverá el orden de visita
    }

    /**
     * DFS (Depth-First Search) - Búsqueda en Profundidad.
     * Explora tanto como sea posible por una rama antes de retroceder (backtracking).
     * 
     * Análisis de Complejidad Temporal: O(V + E)
     * Num vertices + Num aristas, lineal, ya que cada nodo y arista se procesa una vez.
     * Se usa la Pila de Recursión.
     */

    public List<String> dfs(Graph graph, String start) {
        List<String> traversalOrder = new ArrayList<>(); // Si se elimina, no se podrá almacenar el orden de visita
        
        // Set para control de visitados O(1)
        Set<String> visited = new HashSet<>(); // Si se elimina, no se podrá rastrear los nodos visitados
        
        // Llamada inicial a la recursión
        dfsRecursive(graph, start, visited, traversalOrder); // Si se elimina, no se iniciará la búsqueda en profundidad
        
        return traversalOrder; // Si se elimina, no se devolverá el orden de visita
    }

    /**
     * Método Auxiliar Recursivo para DFS.
     * Funciona como una pila.
     */
    private void dfsRecursive(Graph graph, String current, Set<String> visited, List<String> traversalOrder) {
        // Paso 1: Procesar nodo actual (Marcar y Guardar)
        visited.add(current); // Si se elimina, el nodo actual podría ser visitado múltiples veces
        traversalOrder.add(current); // Si se elimina, el nodo actual no se incluirá en el resultado
        
        // Paso 2: Explorar cada vecino
        for (String neighbor : graph.getNeighbors(current).keySet()) { // Si se elimina, no se explorarán los vecinos del nodo actual
            
            // Si encontramos un camino no explorado, (Recursión)
            if (!visited.contains(neighbor)) { // Si se elimina, se podrían visitar nodos repetidos
                dfsRecursive(graph, neighbor, visited, traversalOrder); // Si se elimina, no se explorarán los vecinos del nodo vecino
                // Cuando esta llamada retorna, significa que terminamos esa rama y volvemos (Backtracking)
            }
        }
    }
}