package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class DynamicProgrammingAlgorithms {

    /**
     * Algoritmo de Programación Dinámica para Camino Más Corto con Restricción de Aristas.
     * Encuentra la distancia mínima entre 'start' y 'end' utilizando como máximo K aristas.
     * Análisis de Complejidad Temporal: O(K * E) 
	 * Donde: k es el número máximo de aristas permitidas y E es el número total de aristas en el grafo.
     * K: Es el número máximo de aristas permitidas (maxLength - 1).
     * E:</strong> Es el número total de aristas en el grafo.
     *  El algoritmo realiza K iteraciones. 
	 * En cada iteración, recorre todas las aristas (E) para intentar mejorar las rutas encontradas en el paso anterior.
     * O(n^2), ya que el grafo tiene pocas conexiones (es "disperso"). 
	 * Si el grafo tuviese muchas conexiones, el rendimiento decae hacia O(n^3).
	 *
     * @param graph El grafo.
     * @param start Nodo de origen.
     * @param end Nodo de destino.
     * @param maxLength Número máximo de nodos en la ruta (incluyendo inicio y fin).
     * @return Lista con un mapa que contiene la ruta y la distancia (o lista vacía si no hay solución).
     */
    public List<Map<String, Object>> findAllPaths(Graph graph, String start, String end, int maxLength) {
        List<Map<String, Object>> allPaths = new ArrayList<>();
        
        // Validación básica
        if (maxLength <= 0) return allPaths;

        // 1. Preprocesamiento (Mapeo String a Integer)
        // Convertir los nombres de ciudades a índices numéricos 0..N
        List<String> vertices = new ArrayList<>(graph.getVertices());
        int V = vertices.size();
        Map<String, Integer> index = new HashMap<>();
        
        for (int i = 0; i < V; i++) {
            index.put(vertices.get(i), i);
        }

        if (!index.containsKey(start) || !index.containsKey(end)) {
            return allPaths;
        }

        int startIdx = index.get(start);
        int endIdx = index.get(end);
        
        // Convertimos "máximo de nodos" a "máximo de aristas" (saltos)
        int maxEdges = Math.max(0, maxLength - 1); 

        // 2. Estructuras de la Tabla
        // dp[k][v] = Costo mínimo para llegar al vértice 'v' usando  'k' aristas.
        // Usamos /4 para evitar desbordamiento al sumar distancias.
        long INF = Long.MAX_VALUE / 4;
        long[][] dp = new long[maxEdges + 1][V]; 
        
        // pred[k][v] = Guardamos el vértice anterior para poder reconstruir el camino.
        int[][] pred = new int[maxEdges + 1][V]; 

        // Inicialización de la tabla
        for (int k = 0; k <= maxEdges; k++) {
            Arrays.fill(dp[k], INF);
            Arrays.fill(pred[k], -1);
        }
        
        // Caso base: Para llegar al inicio con 0 aristas, el costo es 0.
        dp[0][startIdx] = 0; 

        // Aplanamos el grafo a una lista de aristas simple para iterar rápido: origen, destino, peso
        List<int[]> edges = new ArrayList<>();
        for (String u : vertices) {
            int uIdx = index.get(u);
            for (Map.Entry<String, Integer> neigh : graph.getNeighbors(u).entrySet()) {
                String v = neigh.getKey();
                if (index.containsKey(v)) {
                    edges.add(new int[]{uIdx, index.get(v), neigh.getValue()});
                }
            }
        }

        // 3. Bucle Principal (La Lógica DP)
        // Iteramos aumentando el número de aristas permitidas (k) de 1 hasta maxEdges.
        for (int k = 1; k <= maxEdges; k++) {
            
            // Relajación: Intentamos mejorar las rutas de longitud k basándonos en las rutas de longitud k-1.
            // "¿Puedo llegar a 'v' más rápido si vengo desde 'u' (que tomó k-1 pasos) y sumo esta arista?"
            for (int[] e : edges) {
                int u = e[0]; // Origen
                int v = e[1]; // Destino
                int w = e[2]; // Peso
                
                // Si es posible llegar a 'u' en k-1 pasos...
                if (dp[k-1][u] != INF) {
                    long newCost = dp[k-1][u] + (long) w;
                    
                    // Si este nuevo camino es mejor que lo que teníamos para llegar a 'v' con k pasos...
                    if (newCost < dp[k][v]) {
                        dp[k][v] = newCost; // Actualizamos costo
                        pred[k][v] = u;     // Guardamos predecesor
                    }
                }
            }
        }

        // 4. Selección del Mejor Resultado 
        // El camino óptimo podría usar - aristas que el máximo permitido.
        // Revisamos la columna del destino (endIdx) para todos los k posibles y nos quedamos con el mínimo global.
        long bestDist = INF;
        int bestK = -1;
        
        for (int k = 0; k <= maxEdges; k++) {
            if (dp[k][endIdx] < bestDist) {
                bestDist = dp[k][endIdx];
                bestK = k;
            }
        }

        // Si bestDist sigue siendo inferior, no hay camino que cumpla las restricciones
        if (bestK == -1 || bestDist == INF) {
            return allPaths;
        }

        // 5. Reconstrucción del Camino (Backtracking)
        LinkedList<String> route = new LinkedList<>();
        int currentVertexIdx = endIdx;
        int k = bestK;
        
        // Retrocedemos desde el final hasta el inicio usando la matriz 'pred'
        while (k > 0) {
            route.addFirst(vertices.get(currentVertexIdx));
            int previousVertexIdx = pred[k][currentVertexIdx];
            
            if (previousVertexIdx == -1) return allPaths; // Seguridad
            
            currentVertexIdx = previousVertexIdx;
            k--;
        }

        // Agregamos el nodo inicial
        route.addFirst(vertices.get(currentVertexIdx));

        // Empaquetamos resultado
        Map<String, Object> result = new HashMap<>();
        result.put("route", new ArrayList<>(route));
        result.put("totalDistance", bestDist);
        allPaths.add(result);
        
        return allPaths;
    }
}