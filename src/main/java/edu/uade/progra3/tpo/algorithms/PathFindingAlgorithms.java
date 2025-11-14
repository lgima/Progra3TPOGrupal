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
        Map<String, Integer> distances = new HashMap<>();
        
        //Guardamos desde qué nodo venimos para llegar (para reconstruir la ruta al final).
        Map<String, String> previousNodes = new HashMap<>();
        
        // Conjunto de nodos ya procesados.
        Set<String> visited = new HashSet<>();
        
        // Min-Heap: Nos da siempre el nodo con la menor distancia tentativa.
        PriorityQueue<String> queue = new PriorityQueue<>(
            Comparator.comparingInt(distances::get)
        );

        // --- 2. Inicialización ---
        
        // Todos los nodos inician con distancia "Infinita" (desconocida)
        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        
        // La distancia al nodo de inicio es 0
        distances.put(start, 0);
        queue.offer(start);

        // --- 3. Bucle Principal - Greedy
        while (!queue.isEmpty()) {
            
            // Paso A: Seleccionar el nodo más prometedor (menor distancia actual)
            String current = queue.poll(); 

            // Optimización: Si sacamos el nodo destino de la cola, ya encontramos el camino más corto.
            if (current.equals(end)) {
                break;
            }
            
            // Evitar reprocesar nodos ya cerrados (optimización)
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            // Paso B: Relajación de aristas (Evaluar vecinos)
            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
                String nextNode = neighbor.getKey();
                int edgeWeight = neighbor.getValue();

                // Calculamos: distancia hasta 'current' + peso de la arista hacia 'neighbor'
                int newDistance = distances.get(current) + edgeWeight;

                // Si encontramos un camino MÁS CORTO que el que conocíamos anteriormente:
                if (newDistance < distances.get(nextNode)) {
                    // Actualizamos la distancia mínima
                    distances.put(nextNode, newDistance);
                    
                    // Guardamos que llegamos a 'nextNode' desde 'current'
                    previousNodes.put(nextNode, current);
                    
                    // Agregamos a la cola para seguir explorando desde ahí
                    queue.offer(nextNode);
                }
            }
        }

        // --- 4. Reconstrucción del Camino (Backtracking)
        List<String> path = new ArrayList<>();
        String step = end;
        
        // Si el destino nunca fue alcanzado, la distancia seguirá siendo MAX_VALUE
        if (distances.get(end) == Integer.MAX_VALUE) {
            return Map.of("path", Collections.emptyList(), "distance", -1, "explored", visited);
        }

        // Retrocedemos desde el final hasta el inicio usando el mapa 'previousNodes'
        while (step != null) {
            path.add(0, step); // Insertamos al inicio de la lista para orden correcto
            step = previousNodes.get(step);
        }

        return Map.of(
            "path", path,
            "distance", distances.get(end),
            "explored", visited
        );
    }
}