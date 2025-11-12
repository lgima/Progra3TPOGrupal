package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class MinimumSpanningTreeAlgorithms {
	/**
	 * prim(graph)
	 * - Implementa Prim para construir el árbol de expansión mínima.
	 * - Usa un conjunto de vértices visitados y una PQ de aristas.
	 * Complejidad temporal: O(E log V) (dependiendo de la PQ).
	 */
	public Map<String, Object> prim(Graph graph) {
        Set<String> visited = new HashSet<>();
        List<List<String>> routes = new ArrayList<>();
        int totalCost = 0;
        
        // Elegir un vértice inicial arbitrario
        String start = graph.getVertices().iterator().next();
        visited.add(start);

        // PriorityQueue de aristas candidatas ordenadas por peso
        PriorityQueue<Edge> pq = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.weight)
        );

        // Mientras queden vértices sin visitar, añadir aristas que conectan el componente
        while (visited.size() < graph.getVertices().size()) {
            // Añadir todas las aristas que salen de los vértices ya visitados hacia no visitados
            for (String vertex : visited) {
                for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(vertex).entrySet()) {
                    if (!visited.contains(neighbor.getKey())) {
                        // Push candidato a la PQ
                        pq.offer(new Edge(vertex, neighbor.getKey(), neighbor.getValue()));
                    }
                }
            }

            // Sacar la arista de menor peso que conecta a un vértice nuevo
            Edge edge = pq.poll();
            // Si su vértice objetivo todavía no está visitado, lo añadimos al árbol
            if (!visited.contains(edge.target)) {
                visited.add(edge.target);
                routes.add(Arrays.asList(edge.source, edge.target));
                totalCost += edge.weight;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("routes", routes);
        result.put("totalCost", totalCost);
        return result;
    }

    private static class Edge {
        String source;
        String target;
        int weight;

        Edge(String source, String target, int weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }
    }

	/**
	 * kruskal(graph)
	 * - Implementa Kruskal: ordenar todas las aristas y unir componentes si no crean ciclos.
	 * - Nota: la implementación de findSet/union es ingenua (usa mapas/conjuntos) en lugar de
	 *   Disjoint Set Union optimizado; por tanto las operaciones de unión/búsqueda pueden ser costosas.
	 * Complejidad temporal: O(E log E) por la ordenación; con la implementación ingenua puede aumentar
	 *   el coste en las operaciones de forest (peor caso cercano a O(V^2) en casos extremos).
	 */
    public Map<String, Object> kruskal(Graph graph) {
        // Implementation of Kruskal's algorithm
        List<KruskalEdge> resultEdges = new ArrayList<>();
        Map<String, Set<String>> forest = new HashMap<>();
        int totalWeight = 0;
        
        // Initialize forest with singleton sets (cada vértice en su componente)
        for (String vertex : graph.getVertices()) {
            Set<String> set = new HashSet<>();
            set.add(vertex);
            forest.put(vertex, set);
        }
        
        // Obtener todas las aristas del grafo (puede contener duplicados si el grafo es dirigido/almacenado así)
        List<KruskalEdge> allEdges = new ArrayList<>();
        for (String from : graph.getVertices()) {
            for (Map.Entry<String, Integer> entry : graph.getNeighbors(from).entrySet()) {
                // Añadir cada arista con su peso
                allEdges.add(new KruskalEdge(from, entry.getKey(), entry.getValue()));
            }
        }
        // Ordenar aristas por peso ascendente
        Collections.sort(allEdges);
        
        // Procesar aristas de menor a mayor, uniendo componentes si no generan ciclo
        for (KruskalEdge edge : allEdges) {
            String setA = findSet(forest, edge.from);
            String setB = findSet(forest, edge.to);
            
            // Si pertenecen a componentes distintas, unirlas y aceptar la arista
            if (!setA.equals(setB)) {
                resultEdges.add(edge);
                totalWeight += edge.weight;
                union(forest, setA, setB);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("edges", resultEdges);
        result.put("totalWeight", totalWeight);
        return result;
    }
    
    private String findSet(Map<String, Set<String>> forest, String vertex) {
        for (Map.Entry<String, Set<String>> entry : forest.entrySet()) {
            if (entry.getValue().contains(vertex)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    private void union(Map<String, Set<String>> forest, String setA, String setB) {
        Set<String> unionSet = new HashSet<>(forest.get(setA));
        unionSet.addAll(forest.get(setB));
        for (String vertex : unionSet) {
            forest.put(vertex, unionSet);
        }
    }
    
    private static class KruskalEdge implements Comparable<KruskalEdge> {
        String from;
        String to;
        int weight;
        
        KruskalEdge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        
        @Override
        public int compareTo(KruskalEdge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }
}
