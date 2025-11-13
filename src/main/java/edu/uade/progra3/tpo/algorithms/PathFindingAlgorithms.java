package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class PathFindingAlgorithms {
    /**
     * dijkstra(graph, start, end)
     * - Implementa Dijkstra usando una PriorityQueue (min-heap) para obtener el camino
     *   de menor coste desde start hasta end.
     * - Mantiene distancias y nodos previos para reconstruir la ruta.
     * Complejidad temporal: O((V + E) log V).
     */
    public Map<String, Object> dijkstra(Graph graph, String start, String end) {
        // Crear un mapa para almacenar las distancias mínimas desde el nodo inicial
        Map<String, Integer> distances = new HashMap<>(); // Si se elimina, no se podrán rastrear las distancias
        // Crear un mapa para rastrear los nodos previos en el camino más corto
        Map<String, String> previousNodes = new HashMap<>(); // Si se elimina, no se podrá reconstruir la ruta
        // Crear una PriorityQueue para gestionar los nodos a visitar
        PriorityQueue<String> queue = new PriorityQueue<>(
            Comparator.comparingInt(distances::get) // Si se elimina, no se priorizarán los nodos con menor distancia
        );
        // Crear un conjunto para los nodos ya visitados
        Set<String> visited = new HashSet<>(); // Si se elimina, no se podrá evitar visitar nodos repetidos

        // Inicializar distancias
        for (String vertex : graph.getVertices()) { // Si se elimina, no se inicializarán las distancias
            distances.put(vertex, Integer.MAX_VALUE); // Si se elimina, no se asignará un valor inicial infinito
        }
        distances.put(start, 0); // Si se elimina, no se establecerá la distancia inicial en 0
        queue.offer(start); // Si se elimina, no se agregará el nodo inicial a la cola

        // Algoritmo principal
        while (!queue.isEmpty()) { // Si se elimina, no se recorrerán los nodos
            // Extraer el vértice con distancia mínima conocida
            String current = queue.poll(); // Si se elimina, no se seleccionará el nodo más cercano
            if (current.equals(end)) { // Si se elimina, no se detectará cuando se alcance el destino
                break;
            }

            if (visited.contains(current)) { // Si se elimina, se podrían procesar nodos repetidos
                continue;
            }
            visited.add(current); // Si se elimina, no se marcará el nodo como visitado

            // Explorar todas las aristas salientes del vértice actual
            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) { // Si se elimina, no se explorarán los vecinos
                String next = neighbor.getKey();
                int newDistance = distances.get(current) + neighbor.getValue(); // Si se elimina, no se calculará la nueva distancia
                if (newDistance < distances.get(next)) { // Si se elimina, no se actualizarán las distancias mínimas
                    distances.put(next, newDistance); // Si se elimina, no se guardará la nueva distancia mínima
                    previousNodes.put(next, current); // Si se elimina, no se rastreará el nodo previo
                    queue.offer(next); // Si se elimina, no se agregará el vecino a la cola
                }
            }
        }

        // Reconstruir el camino desde 'end' usando previousNodes
        List<String> path = new ArrayList<>(); // Si se elimina, no se podrá construir la ruta
        String current = end; // Si se elimina, no se podrá rastrear el nodo actual
        while (current != null) { // Si se elimina, no se recorrerá la ruta hacia atrás
            path.add(0, current); // Si se elimina, no se agregará el nodo a la ruta
            current = previousNodes.get(current); // Si se elimina, no se obtendrá el nodo previo
        }

        // Devolver el resultado
        return Map.of( // Si se elimina, no se podrá devolver el resultado
            "path", path, // Si se elimina, no se incluirá la ruta en el resultado
            "distance", distances.get(end), // Si se elimina, no se incluirá la distancia total en el resultado
            "explored", visited // Si se elimina, no se incluirán los nodos explorados en el resultado
        );
    }
}
