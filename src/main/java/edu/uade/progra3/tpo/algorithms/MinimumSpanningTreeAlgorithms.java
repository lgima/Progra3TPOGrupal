package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class MinimumSpanningTreeAlgorithms {
    /**
     * prim(graph)
     * - Implementa Prim para construir el árbol de expansión mínima.
     * - Usa un conjunto de vértices visitados y una PriorityQueue de aristas.
     * Complejidad temporal: O(E log V).
     */
    public Map<String, Object> prim(Graph graph) {
        // Crear un conjunto para los vértices visitados
        Set<String> visited = new HashSet<>(); // Si se elimina, no se podrá rastrear los vértices visitados
        // Crear una lista para almacenar las rutas del árbol
        List<List<String>> routes = new ArrayList<>(); // Si se elimina, no se podrán registrar las rutas del árbol
        // Inicializar el costo total a 0
        int totalCost = 0; // Si se elimina, no se podrá calcular el costo total

        // Elegir un vértice inicial arbitrario
        String start = graph.getVertices().iterator().next(); // Si se elimina, no se podrá iniciar el algoritmo
        visited.add(start); // Si se elimina, el vértice inicial no será marcado como visitado

        // Crear una PriorityQueue para las aristas candidatas ordenadas por peso
        PriorityQueue<Edge> pq = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.weight) // Si se elimina, las aristas no estarán ordenadas por peso
        );

        // Mientras queden vértices sin visitar
        while (visited.size() < graph.getVertices().size()) { // Si se elimina, el algoritmo no iterará correctamente
            // Añadir todas las aristas que conectan vértices visitados con no visitados
            for (String vertex : visited) { // Si se elimina, no se explorarán las aristas de los vértices visitados
                for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(vertex).entrySet()) { // Si se elimina, no se explorarán los vecinos
                    if (!visited.contains(neighbor.getKey())) { // Si se elimina, se podrían añadir aristas redundantes
                        pq.offer(new Edge(vertex, neighbor.getKey(), neighbor.getValue())); // Si se elimina, no se agregarán aristas a la cola
                    }
                }
            }

            // Sacar la arista de menor peso que conecta a un vértice nuevo
            Edge edge = pq.poll(); // Si se elimina, no se seleccionará la arista de menor peso
            if (!visited.contains(edge.target)) { // Si se elimina, se podrían añadir ciclos al árbol
                visited.add(edge.target); // Si se elimina, el vértice objetivo no será marcado como visitado
                routes.add(Arrays.asList(edge.source, edge.target)); // Si se elimina, no se registrará la ruta
                totalCost += edge.weight; // Si se elimina, no se actualizará el costo total
            }
        }

        // Devolver las rutas y el costo total
        Map<String, Object> result = new HashMap<>(); // Si se elimina, no se podrá devolver el resultado
        result.put("routes", routes); // Si se elimina, no se incluirán las rutas en el resultado
        result.put("totalCost", totalCost); // Si se elimina, no se incluirá el costo total en el resultado
        return result; // Si se elimina, no se devolverá el resultado
    }

    /**
     * kruskal(graph)
     * - Implementa Kruskal: ordenar todas las aristas y unir componentes si no crean ciclos.
     * Complejidad temporal: O(E log E).
     */
    public Map<String, Object> kruskal(Graph graph) {
        // Crear una lista para almacenar las aristas del árbol
        List<KruskalEdge> resultEdges = new ArrayList<>(); // Si se elimina, no se podrán registrar las aristas del árbol
        // Crear un mapa para los conjuntos disjuntos de vértices
        Map<String, Set<String>> forest = new HashMap<>(); // Si se elimina, no se podrá gestionar la unión de componentes
        // Inicializar el peso total a 0
        int totalWeight = 0; // Si se elimina, no se podrá calcular el peso total

        // Inicializar cada vértice en su propio conjunto
        for (String vertex : graph.getVertices()) { // Si se elimina, no se inicializarán los conjuntos
            Set<String> set = new HashSet<>();
            set.add(vertex);
            forest.put(vertex, set);
        }

        // Obtener todas las aristas del grafo
        List<KruskalEdge> allEdges = new ArrayList<>(); // Si se elimina, no se podrán almacenar las aristas
        for (String from : graph.getVertices()) { // Si se elimina, no se recorrerán los vértices
            for (Map.Entry<String, Integer> entry : graph.getNeighbors(from).entrySet()) { // Si se elimina, no se explorarán los vecinos
                allEdges.add(new KruskalEdge(from, entry.getKey(), entry.getValue())); // Si se elimina, no se agregarán aristas a la lista
            }
        }

        // Ordenar aristas por peso ascendente
        Collections.sort(allEdges); // Si se elimina, las aristas no estarán ordenadas por peso

        // Procesar aristas de menor a mayor
        for (KruskalEdge edge : allEdges) { // Si se elimina, no se recorrerán las aristas
            String setA = findSet(forest, edge.from); // Si se elimina, no se identificará el conjunto del vértice origen
            String setB = findSet(forest, edge.to); // Si se elimina, no se identificará el conjunto del vértice destino

            if (!setA.equals(setB)) { // Si se elimina, se podrían añadir ciclos al árbol
                resultEdges.add(edge); // Si se elimina, no se registrará la arista en el árbol
                totalWeight += edge.weight; // Si se elimina, no se actualizará el peso total
                union(forest, setA, setB); // Si se elimina, no se unirán los conjuntos
            }
        }

        // Devolver las aristas y el peso total
        Map<String, Object> result = new HashMap<>(); // Si se elimina, no se podrá devolver el resultado
        result.put("edges", resultEdges); // Si se elimina, no se incluirán las aristas en el resultado
        result.put("totalWeight", totalWeight); // Si se elimina, no se incluirá el peso total en el resultado
        return result; // Si se elimina, no se devolverá el resultado
    }

    private String findSet(Map<String, Set<String>> forest, String vertex) {
        for (Map.Entry<String, Set<String>> entry : forest.entrySet()) { // Si se elimina, no se buscará el conjunto del vértice
            if (entry.getValue().contains(vertex)) { // Si se elimina, no se identificará el conjunto correcto
                return entry.getKey();
            }
        }
        return null; // Si se elimina, no se manejarán vértices sin conjunto
    }

    private void union(Map<String, Set<String>> forest, String setA, String setB) {
        Set<String> unionSet = new HashSet<>(forest.get(setA)); // Si se elimina, no se creará un nuevo conjunto
        unionSet.addAll(forest.get(setB)); // Si se elimina, no se combinarán los conjuntos
        for (String vertex : unionSet) { // Si se elimina, no se actualizarán los conjuntos en el mapa
            forest.put(vertex, unionSet);
        }
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
            return Integer.compare(this.weight, other.weight); // Si se elimina, no se podrán comparar aristas por peso
        }
    }
}
