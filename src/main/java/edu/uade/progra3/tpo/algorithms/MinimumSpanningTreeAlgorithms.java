package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class MinimumSpanningTreeAlgorithms {
    public Map<String, Object> prim(Graph graph) {
        Set<String> visited = new HashSet<>();
        List<List<String>> routes = new ArrayList<>();
        int totalCost = 0;
        
        String start = graph.getVertices().iterator().next();
        visited.add(start);

        PriorityQueue<Edge> pq = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.weight)
        );

        while (visited.size() < graph.getVertices().size()) {
            for (String vertex : visited) {
                for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(vertex).entrySet()) {
                    if (!visited.contains(neighbor.getKey())) {
                        pq.offer(new Edge(vertex, neighbor.getKey(), neighbor.getValue()));
                    }
                }
            }

            Edge edge = pq.poll();
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

    public Map<String, Object> kruskal(Graph graph) {
        // Implementation of Kruskal's algorithm
        List<KruskalEdge> resultEdges = new ArrayList<>();
        Map<String, Set<String>> forest = new HashMap<>();
        int totalWeight = 0;
        
        // Initialize forest with singleton sets
        for (String vertex : graph.getVertices()) {
            Set<String> set = new HashSet<>();
            set.add(vertex);
            forest.put(vertex, set);
        }
        
        // Get all edges and sort them
        List<KruskalEdge> allEdges = new ArrayList<>();
        for (String from : graph.getVertices()) {
            for (Map.Entry<String, Integer> entry : graph.getNeighbors(from).entrySet()) {
                allEdges.add(new KruskalEdge(from, entry.getKey(), entry.getValue()));
            }
        }
        Collections.sort(allEdges);
        
        // Process each edge
        for (KruskalEdge edge : allEdges) {
            String setA = findSet(forest, edge.from);
            String setB = findSet(forest, edge.to);
            
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
