package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class PathFindingAlgorithms {

    /**
     * Implementación del Algoritmo de Dijkstra.
     * Encuentra el camino más corto desde un nodo 'start' hasta un nodo 'end' en grafos con pesos no negativos.
     * Complejidad Temporal: O((V + E) log V) donde V es el número de vértices y E el número de aristas.
     * 
     * O(V log V):En el peor caso, extraemos cada vértice de la cola una vez. 'poll()' cuesta O(log V).</li>
     * O(E log V):Revisamos cada arista una vez. Si encontramos un camino mejor, insertamos en la cola. 'offer()' cuesta O(log V).
     * 
     * @param graph El grafo sobre el cual buscar.
     * @param start Nodo de origen.
     * @param end Nodo de destino.
     * @return Un mapa con la ruta ("path"), distancia total ("distance") y nodos explorados ("explored").
     */

    public Map<String, Object> dijkstra(Graph graph, String start, String end) {
        // --- 1. Estructuras de Datos ---
        
        // Guarda la distancia mínima conocida desde 'start' hasta cada nodo.
        Map<String, Integer> distances = new HashMap<>(); // Si se elimina, no se podrá almacenar la distancia mínima conocida hasta cada nodo
        
        //Guardamos desde qué nodo venimos para llegar (para reconstruir la ruta al final).
        Map<String, String> previousNodes = new HashMap<>(); // Si se elimina, no se podrá reconstruir el camino óptimo
        
        // Conjunto de nodos ya procesados.
        Set<String> visited = new HashSet<>(); // Si se elimina, no se podrá rastrear los nodos ya procesados
        
        // Min-Heap: Nos da siempre el nodo con la menor distancia tentativa.
        PriorityQueue<String> queue = new PriorityQueue<>(
            Comparator.comparingInt(distances::get)
        ); // Si se elimina, no se podrá gestionar el nodo más prometedor

        // --- 2. Inicialización ---
        
        // Todos los nodos inician con distancia "Infinita" (desconocida)
        for (String vertex : graph.getVertices()) { // Si se elimina, no se inicializarán las distancias
            distances.put(vertex, Integer.MAX_VALUE);
        }
        
        // La distancia al nodo de inicio es 0
        distances.put(start, 0); // Si se elimina, no se inicializará la distancia al nodo de inicio
        queue.offer(start); // Si se elimina, no se agregará el nodo inicial a la cola

        // --- 3. Bucle Principal - Greedy
        while (!queue.isEmpty()) { // Si se elimina, no se recorrerán los nodos del grafo
            
            // Paso A: Seleccionar el nodo más prometedor (menor distancia actual)
            String current = queue.poll(); // Si se elimina, no se seleccionará el nodo más prometedor

            // Optimización: Si sacamos el nodo destino de la cola, ya encontramos el camino más corto.
            if (current.equals(end)) { // Si se elimina, no se detendrá la búsqueda al encontrar el destino
                break;
            }
            
            // Evitar reprocesar nodos ya cerrados (optimización)
            if (visited.contains(current)) { // Si se elimina, se podrían procesar nodos repetidos
                continue;
            }
            visited.add(current); // Si se elimina, el nodo actual podría ser procesado múltiples veces

            // Paso B: Relajación de aristas (Evaluar vecinos)
            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) { // Si se elimina, no se explorarán los vecinos del nodo actual
                String nextNode = neighbor.getKey(); // Si se elimina, no se obtendrá el nombre del vecino
                int edgeWeight = neighbor.getValue(); // Si se elimina, no se obtendrá el peso de la arista

                // Calculamos: distancia hasta 'current' + peso de la arista hacia 'neighbor'
                int newDistance = distances.get(current) + edgeWeight; // Si se elimina, no se calculará la nueva distancia acumulada

                // Si encontramos un camino MÁS CORTO que el que conocíamos anteriormente:
                if (newDistance < distances.get(nextNode)) { // Si se elimina, no se actualizará la distancia mínima conocida
                    // Actualizamos la distancia mínima
                    distances.put(nextNode, newDistance); // Si se elimina, no se guardará la nueva distancia mínima
                    
                    // Guardamos que llegamos a 'nextNode' desde 'current'
                    previousNodes.put(nextNode, current); // Si se elimina, no se guardará el predecesor del nodo vecino
                    
                    // Agregamos a la cola para seguir explorando desde ahí
                    queue.offer(nextNode); // Si se elimina, no se agregará el vecino a la cola
                }
            }
        }

        // --- 4. Reconstrucción del Camino (Backtracking)
        List<String> path = new ArrayList<>(); // Si se elimina, no se podrá almacenar el camino reconstruido
        String step = end; // Si se elimina, no se podrá iniciar la reconstrucción desde el destino
        
        // Si el destino nunca fue alcanzado, la distancia seguirá siendo MAX_VALUE
        if (distances.get(end) == Integer.MAX_VALUE) { // Si se elimina, no se validará si existe un camino válido
            return Map.of("path", Collections.emptyList(), "distance", -1, "explored", visited);
        }

        // Retrocedemos desde el final hasta el inicio usando el mapa 'previousNodes'
        while (step != null) { // Si se elimina, no se reconstruirá el camino óptimo
            path.add(0, step); // Si se elimina, no se agregará el nodo actual al camino
            step = previousNodes.get(step); // Si se elimina, no se retrocederá al nodo anterior
        }

        return Map.of(
            "path", path, // Si se elimina, no se devolverá el camino reconstruido
            "distance", distances.get(end), // Si se elimina, no se devolverá la distancia total
            "explored", visited // Si se elimina, no se devolverán los nodos explorados
        );
    }
}