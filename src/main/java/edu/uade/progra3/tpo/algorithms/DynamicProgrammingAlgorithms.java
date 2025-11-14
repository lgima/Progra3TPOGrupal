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
        List<Map<String, Object>> allPaths = new ArrayList<>(); // Si se elimina, no se podrán almacenar los caminos encontrados
        
        // Validación básica
        if (maxLength <= 0) return allPaths; // Si se elimina, no se validará el número máximo de nodos permitido
        
        // 1. Preprocesamiento (Mapeo String a Integer)
        // Convertir los nombres de ciudades a índices numéricos 0..N
        List<String> vertices = new ArrayList<>(graph.getVertices()); // Si se elimina, no se obtendrán los vértices del grafo
        int V = vertices.size(); // Si se elimina, no se calculará el número de vértices
        Map<String, Integer> index = new HashMap<>(); // Si se elimina, no se mapearán los nombres de las ciudades a índices
        
        for (int i = 0; i < V; i++) { // Si se elimina, no se llenará el mapa de índices
            index.put(vertices.get(i), i);
        }

        if (!index.containsKey(start) || !index.containsKey(end)) { // Si se elimina, no se validará que las ciudades existan en el grafo
            return allPaths;
        }

        int startIdx = index.get(start); // Si se elimina, no se obtendrá el índice de la ciudad de inicio
        int endIdx = index.get(end); // Si se elimina, no se obtendrá el índice de la ciudad de destino
        
        // Convertimos "máximo de nodos" a "máximo de aristas" (saltos)
        int maxEdges = Math.max(0, maxLength - 1); // Si se elimina, no se calculará el número máximo de aristas permitido

        // 2. Estructuras de la Tabla
        // dp[k][v] = Costo mínimo para llegar al vértice 'v' usando  'k' aristas.
        // Usamos /4 para evitar desbordamiento al sumar distancias.
        long INF = Long.MAX_VALUE / 4; // Si se elimina, no se definirá un valor infinito para inicializar la tabla
        long[][] dp = new long[maxEdges + 1][V]; // Si se elimina, no se creará la tabla de costos mínimos
        
        // pred[k][v] = Guardamos el vértice anterior para poder reconstruir el camino.
        int[][] pred = new int[maxEdges + 1][V]; // Si se elimina, no se podrá reconstruir el camino óptimo

        // Inicialización de la tabla
        for (int k = 0; k <= maxEdges; k++) { // Si se elimina, no se inicializarán las tablas
            Arrays.fill(dp[k], INF);
            Arrays.fill(pred[k], -1);
        }
        
        // Caso base: Para llegar al inicio con 0 aristas, el costo es 0.
        dp[0][startIdx] = 0; // Si se elimina, no se inicializará el caso base

        // Aplanamos el grafo a una lista de aristas simple para iterar rápido: origen, destino, peso
        List<int[]> edges = new ArrayList<>(); // Si se elimina, no se podrá iterar sobre las aristas del grafo
        for (String u : vertices) { // Si se elimina, no se recorrerán los vértices del grafo
            int uIdx = index.get(u); // Si se elimina, no se obtendrá el índice del vértice actual
            for (Map.Entry<String, Integer> neigh : graph.getNeighbors(u).entrySet()) { // Si se elimina, no se recorrerán los vecinos del vértice actual
                String v = neigh.getKey(); // Si se elimina, no se obtendrá el nombre del vecino
                if (index.containsKey(v)) { // Si se elimina, no se validará que el vecino exista en el grafo
                    edges.add(new int[]{uIdx, index.get(v), neigh.getValue()}); // Si se elimina, no se agregarán las aristas a la lista
                }
            }
        }

        // 3. Bucle Principal (La Lógica DP)
        // Iteramos aumentando el número de aristas permitidas (k) de 1 hasta maxEdges.
        for (int k = 1; k <= maxEdges; k++) { // Si se elimina, no se iterará sobre el número de aristas permitidas
            
            // Relajación: Intentamos mejorar las rutas de longitud k basándonos en las rutas de longitud k-1.
            // "¿Puedo llegar a 'v' más rápido si vengo desde 'u' (que tomó k-1 pasos) y sumo esta arista?"
            for (int[] e : edges) { // Si se elimina, no se recorrerán las aristas del grafo
                int u = e[0]; // Si se elimina, no se obtendrá el vértice de origen de la arista
                int v = e[1]; // Si se elimina, no se obtendrá el vértice de destino de la arista
                int w = e[2]; // Si se elimina, no se obtendrá el peso de la arista
                
                // Si es posible llegar a 'u' en k-1 pasos...
                if (dp[k-1][u] != INF) { // Si se elimina, no se validará si es posible llegar al vértice de origen
                    long newCost = dp[k-1][u] + (long) w; // Si se elimina, no se calculará el nuevo costo acumulado
                    
                    // Si este nuevo camino es mejor que lo que teníamos para llegar a 'v' con k pasos...
                    if (newCost < dp[k][v]) { // Si se elimina, no se actualizará el costo mínimo para el vértice de destino
                        dp[k][v] = newCost; // Si se elimina, no se guardará el nuevo costo mínimo
                        pred[k][v] = u; // Si se elimina, no se guardará el predecesor del vértice de destino
                    }
                }
            }
        }

        // 4. Selección del Mejor Resultado 
        // El camino óptimo podría usar - aristas que el máximo permitido.
        // Revisamos la columna del destino (endIdx) para todos los k posibles y nos quedamos con el mínimo global.
        long bestDist = INF; // Si se elimina, no se inicializará la distancia mínima global
        int bestK = -1; // Si se elimina, no se inicializará el número de aristas del camino óptimo
        
        for (int k = 0; k <= maxEdges; k++) { // Si se elimina, no se buscará el mejor resultado entre todas las longitudes de camino
            if (dp[k][endIdx] < bestDist) { // Si se elimina, no se actualizará la distancia mínima global
                bestDist = dp[k][endIdx];
                bestK = k;
            }
        }

        // Si bestDist sigue siendo inferior, no hay camino que cumpla las restricciones
        if (bestK == -1 || bestDist == INF) { // Si se elimina, no se validará si existe un camino válido
            return allPaths;
        }

        // 5. Reconstrucción del Camino (Backtracking)
        LinkedList<String> route = new LinkedList<>(); // Si se elimina, no se podrá almacenar el camino reconstruido
        int currentVertexIdx = endIdx; // Si se elimina, no se podrá iniciar la reconstrucción desde el destino
        int k = bestK; // Si se elimina, no se podrá retroceder desde el número óptimo de aristas
        
        // Retrocedemos desde el final hasta el inicio usando la matriz 'pred'
        while (k > 0) { // Si se elimina, no se reconstruirá el camino óptimo
            route.addFirst(vertices.get(currentVertexIdx)); // Si se elimina, no se agregará el vértice actual al camino
            int previousVertexIdx = pred[k][currentVertexIdx]; // Si se elimina, no se obtendrá el predecesor del vértice actual
            
            if (previousVertexIdx == -1) return allPaths; // Seguridad
            
            currentVertexIdx = previousVertexIdx; // Si se elimina, no se retrocederá al vértice anterior
            k--; // Si se elimina, no se decrementará el número de aristas restantes
        }

        // Agregamos el nodo inicial
        route.addFirst(vertices.get(currentVertexIdx)); // Si se elimina, no se agregará el vértice inicial al camino

        // Empaquetamos resultado
        Map<String, Object> result = new HashMap<>(); // Si se elimina, no se podrá almacenar el resultado final
        result.put("route", new ArrayList<>(route)); // Si se elimina, no se guardará el camino óptimo
        result.put("totalDistance", bestDist); // Si se elimina, no se guardará la distancia total del camino óptimo
        allPaths.add(result); // Si se elimina, no se agregará el resultado a la lista de caminos
        
        return allPaths; // Si se elimina, no se devolverán los caminos encontrados
    }
}