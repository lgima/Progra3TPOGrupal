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
        List<String> traversalOrder = new ArrayList<>();
        
        // Set para control rápido de visitados (Búsqueda O(1)). 
        Set<String> visited = new HashSet<>();
        
        // Cola para gestionar los pendientes por visitar (Nivel por nivel)
        Queue<String> queue = new LinkedList<>();
        
        // 2. Inicialización
        
        // Empezar desde el nodo inicial
        queue.add(start);
        visited.add(start);
        traversalOrder.add(start);

        // 3. Bucle Principal (Exploración por Niveles)
        while (!queue.isEmpty()) {
            
            // Paso A: Desencolar (Sacar el primero de la fila)
            String current = queue.poll();
            
            // Paso B: Explorar Vecinos
            for (String neighbor : graph.getNeighbors(current).keySet()) {
                
                // Si el vecino no ha sido descubierto...
                if (!visited.contains(neighbor)) {
                    // 1. Marcar como visitado (para no volver a meterlo)
                    visited.add(neighbor);
                    
                    // 2. Guardar en el resultado
                    traversalOrder.add(neighbor);
                    
                    // 3. Encolar para explorar sus vecinos más tarde
                    queue.add(neighbor);
                }
            }
        }
        return traversalOrder;
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
        List<String> traversalOrder = new ArrayList<>();
        
        // Set para control de visitados O(1)
        Set<String> visited = new HashSet<>();
        
        // Llamada inicial a la recursión
        dfsRecursive(graph, start, visited, traversalOrder);
        
        return traversalOrder;
    }

    /**
     * Método Auxiliar Recursivo para DFS.
     * Funciona como una pila.
     */
    private void dfsRecursive(Graph graph, String current, Set<String> visited, List<String> traversalOrder) {
        // Paso 1: Procesar nodo actual (Marcar y Guardar)
        visited.add(current);
        traversalOrder.add(current);
        
        // Paso 2: Explorar cada vecino
        for (String neighbor : graph.getNeighbors(current).keySet()) {
            
            // Si encontramos un camino no explorado, (Recursión)
            if (!visited.contains(neighbor)) {
                dfsRecursive(graph, neighbor, visited, traversalOrder);
                // Cuando esta llamada retorna, significa que terminamos esa rama y volvemos (Backtracking)
            }
        }
    }
}