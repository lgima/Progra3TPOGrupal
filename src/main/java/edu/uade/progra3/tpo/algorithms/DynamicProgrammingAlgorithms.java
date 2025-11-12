package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class DynamicProgrammingAlgorithms {
	/**
	 * findAllPaths(graph, start, end, maxLength)
	 * - Programación dinámica por número de aristas.
	 * - Calcula la mínima distancia desde 'start' hasta 'end' con a lo sumo maxLength nodos
	 *   (es decir, hasta maxEdges = maxLength - 1 aristas).
	 * - Devuelve una lista que contiene un único mapa con "route" (lista de nodos) y
	 *   "totalDistance" (Long). Si no hay camino válido devuelve lista vacía.
	 *
	 * Complejidad temporal: O(K * E) donde K = maxEdges y E = número de aristas del grafo.
	 */
	public List<Map<String, Object>> findAllPaths(Graph graph, String start, String end, int maxLength) {
		List<Map<String, Object>> allPaths = new ArrayList<>();
		if (maxLength <= 0) {
			return allPaths;
		}

		// Preparar lista de vértices e índices
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
		int maxEdges = Math.max(0, maxLength - 1); // número máximo de aristas permitidas

		// DP: dp[k][v] = menor coste para llegar a v usando exactamente k aristas
		long INF = Long.MAX_VALUE / 4;
		long[][] dp = new long[maxEdges + 1][V]; //maxEdges = número de aristas
		int[][] pred = new int[maxEdges + 1][V]; // pred[k][v] = índice del vértice previo en camino con k aristas

		// Inicializar
		for (int k = 0; k <= maxEdges; k++) {
			Arrays.fill(dp[k], INF);
			Arrays.fill(pred[k], -1);
		}
		dp[0][startIdx] = 0; // 0 aristas para estar en el inicio

		// Construir una lista de aristas para iterar (uIndex, vIndex, weight)
		List<int[]> edges = new ArrayList<>();
		for (String u : vertices) {
			int uIdx = index.get(u);
			for (Map.Entry<String, Integer> neigh : graph.getNeighbors(u).entrySet()) {
				String v = neigh.getKey();
				Integer w = neigh.getValue();
				if (index.containsKey(v)) {
					int vIdx = index.get(v);
					edges.add(new int[]{uIdx, vIdx, w});
				}
			}
		}

		// Transición DP: para cada k>0, relajar todas las aristas a partir de dp[k-1]
		for (int k = 1; k <= maxEdges; k++) {
			// Inicialmente copiamos INF (ya hecho) y luego intentamos mejorar dp[k][*]
			for (int[] e : edges) {
				int u = e[0], v = e[1], w = e[2];
				if (dp[k-1][u] != INF) {
					long candidate = dp[k-1][u] + (long) w;
					if (candidate < dp[k][v]) {
						dp[k][v] = candidate;
						pred[k][v] = u;
					}
				}
			}
		}

		// Seleccionar el mejor k (0..maxEdges) que minimize dp[k][endIdx]
		long bestDist = INF;
		int bestK = -1;
		for (int k = 0; k <= maxEdges; k++) {
			if (dp[k][endIdx] < bestDist) {
				bestDist = dp[k][endIdx];
				bestK = k;
			}
		}

		// Si no se encontró camino válido, devolver lista vacía
		if (bestK == -1 || bestDist == INF) {
			return allPaths;
		}

		// Reconstruir ruta desde endIdx usando pred y bestK (k = número de aristas)
		LinkedList<String> route = new LinkedList<>();
		int cur = endIdx;
		int k = bestK;
		// Añadir nodos retrocediendo por las aristas utilizadas
		while (k > 0) {
			route.addFirst(vertices.get(cur));
			int p = pred[k][cur];
			if (p == -1) {
				// defensa: si no hay pred, abortar (ruta no recuperable)
				return allPaths;
			}
			cur = p;
			k--;
		}
		// Añadir el nodo inicial
		route.addFirst(vertices.get(cur));

		// Formar resultado (lista con un único mapa)
		Map<String, Object> result = new HashMap<>();
		result.put("route", new ArrayList<>(route));
		result.put("totalDistance", bestDist);
		allPaths.add(result);
		return allPaths;
	}
}
