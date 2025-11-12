package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class PathFindingAlgorithms {
    /**
     * dijkstra(graph, start, end)
     * - Implementa Dijkstra usando una PriorityQueue (min-heap) para obtener el camino
     *   de menor coste desde start hasta end.
     * - Mantiene distancias y nodos previos para reconstruir la ruta.
     * Complejidad temporal: O((V + E) log V) típicamente O(E log V) con heap.
     */
    public Map<String, Object> dijkstra(Graph graph, String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
            Comparator.comparingInt(distances::get));
        Set<String> visited = new HashSet<>();

        // Inicializar distancias
        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.offer(start);

        // Main algoritmo
        while (!queue.isEmpty()) {
            // Extraer el vértice con distancia mínima conocida
            String current = queue.poll();
            if (current.equals(end)) {
                // Si alcanzamos el destino podemos salir antes (optimización)
                break;
            }
            
            if (visited.contains(current)) {
                // Si ya procesamos este vértice lo saltamos
                continue;
            }
            visited.add(current);

            // Relajar todas las aristas salientes del vértice actual
            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
                String next = neighbor.getKey();
                // calcular distancia provisional pasando por 'current'
                int newDistance = distances.get(current) + neighbor.getValue();

                if (newDistance < distances.get(next)) {
                    // Si encontramos mejor distancia, actualizamos y recordamos el predecesor
                    distances.put(next, newDistance);
                    previousNodes.put(next, current);
                    // Encolamos para evaluar sus vecinos más tarde
                    queue.offer(next);
                }
            }
        }

        // Reconstruct path
        // Retrotraer desde 'end' usando previousNodes hasta llegar a null/start
        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(0, current);
            current = previousNodes.get(current);
        }

        return Map.of(
            "path", path,
            "distance", distances.get(end),
            "explored", visited
        );
    }
}
