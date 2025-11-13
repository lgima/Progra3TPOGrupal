package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class DynamicProgrammingAlgorithms {
    /**
     * findAllPaths(graph, start, end, maxLength)
     * - Programación dinámica por número de aristas.
     * - Calcula la mínima distancia desde 'start' hasta 'end' con a lo sumo maxLength nodos.
     * Complejidad temporal: O(K * E) donde K = maxEdges y E = número de aristas del grafo.
     */
    public List<Map<String, Object>> findAllPaths(Graph graph, String start, String end, int maxLength) {
        List<Map<String, Object>> allPaths = new ArrayList<>(); // Si se elimina, no se podrán almacenar los caminos encontrados
        if (maxLength <= 0) { // Si se elimina, no se manejarán casos de longitud inválida
            return allPaths;
        }

        // Preparar lista de vértices e índices
        List<String> vertices = new ArrayList<>(graph.getVertices()); // Si se elimina, no se obtendrán los vértices del grafo
        int V = vertices.size(); // Si se elimina, no se calculará el número de vértices
        Map<String, Integer> index = new HashMap<>(); // Si se elimina, no se podrá mapear cada vértice a un índice
        for (int i = 0; i < V; i++) { // Si se elimina, no se llenará el mapa de índices
            index.put(vertices.get(i), i);
        }

        if (!index.containsKey(start) || !index.containsKey(end)) { // Si se elimina, no se validará la existencia de los nodos
            return allPaths;
        }

        int startIdx = index.get(start); // Si se elimina, no se obtendrá el índice del nodo inicial
        int endIdx = index.get(end); // Si se elimina, no se obtendrá el índice del nodo final
        int maxEdges = Math.max(0, maxLength - 1); // Si se elimina, no se calculará el número máximo de aristas

        // Inicializar estructuras de programación dinámica
        long INF = Long.MAX_VALUE / 4; // Si se elimina, no se manejarán valores infinitos
        long[][] dp = new long[maxEdges + 1][V]; // Si se elimina, no se almacenarán los costos mínimos
        int[][] pred = new int[maxEdges + 1][V]; // Si se elimina, no se rastrearán los predecesores
        for (int k = 0; k <= maxEdges; k++) { // Si se elimina, no se inicializarán las estructuras
            Arrays.fill(dp[k], INF);
            Arrays.fill(pred[k], -1);
        }
        dp[0][startIdx] = 0; // Si se elimina, no se inicializará el costo del nodo inicial

        // Construir una lista de aristas para iterar
        List<int[]> edges = new ArrayList<>(); // Si se elimina, no se podrán relajar las aristas
        for (String u : vertices) { // Si se elimina, no se recorrerán los vértices
            int uIdx = index.get(u);
            for (Map.Entry<String, Integer> neigh : graph.getNeighbors(u).entrySet()) { // Si se elimina, no se explorarán los vecinos
                String v = neigh.getKey();
                Integer w = neigh.getValue();
                if (index.containsKey(v)) { // Si se elimina, no se validará la existencia del vecino
                    int vIdx = index.get(v);
                    edges.add(new int[]{uIdx, vIdx, w}); // Si se elimina, no se agregarán aristas a la lista
                }
            }
        }

        // Transición DP: para cada k > 0, relajar todas las aristas
        for (int k = 1; k <= maxEdges; k++) { // Si se elimina, no se calcularán los costos para cada número de aristas
            for (int[] e : edges) { // Si se elimina, no se recorrerán las aristas
                int u = e[0], v = e[1], w = e[2];
                if (dp[k - 1][u] != INF) { // Si se elimina, no se validará la alcanzabilidad del nodo
                    long candidate = dp[k - 1][u] + (long) w; // Si se elimina, no se calculará el nuevo costo
                    if (candidate < dp[k][v]) { // Si se elimina, no se actualizarán los costos mínimos
                        dp[k][v] = candidate;
                        pred[k][v] = u; // Si se elimina, no se rastreará el predecesor
                    }
                }
            }
        }

        // Seleccionar el mejor k que minimice dp[k][endIdx]
        long bestDist = INF; // Si se elimina, no se inicializará la mejor distancia
        int bestK = -1; // Si se elimina, no se inicializará el mejor número de aristas
        for (int k = 0; k <= maxEdges; k++) { // Si se elimina, no se buscará el mejor k
            if (dp[k][endIdx] < bestDist) {
                bestDist = dp[k][endIdx];
                bestK = k;
            }
        }

        if (bestK == -1 || bestDist == INF) { // Si se elimina, no se manejarán casos sin solución
            return allPaths;
        }

        // Reconstruir ruta desde endIdx usando pred y bestK
        LinkedList<String> route = new LinkedList<>(); // Si se elimina, no se podrá construir la ruta
        int cur = endIdx; // Si se elimina, no se podrá rastrear el nodo actual
        int k = bestK; // Si se elimina, no se podrá rastrear el número de aristas
        while (k > 0) { // Si se elimina, no se reconstruirá la ruta
            route.addFirst(vertices.get(cur)); // Si se elimina, no se agregará el nodo a la ruta
            int p = pred[k][cur]; // Si se elimina, no se obtendrá el predecesor
            if (p == -1) { // Si se elimina, no se manejarán rutas inválidas
                return allPaths;
            }
            cur = p;
            k--;
        }
        route.addFirst(vertices.get(cur)); // Si se elimina, no se agregará el nodo inicial

        // Formar resultado
        Map<String, Object> result = new HashMap<>(); // Si se elimina, no se podrá devolver el resultado
        result.put("route", new ArrayList<>(route)); // Si se elimina, no se incluirá la ruta en el resultado
        result.put("totalDistance", bestDist); // Si se elimina, no se incluirá la distancia total en el resultado
        allPaths.add(result); // Si se elimina, no se agregará el resultado a la lista
        return allPaths; // Si se elimina, no se devolverán los caminos encontrados
    }
}
