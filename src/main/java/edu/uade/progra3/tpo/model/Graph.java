package edu.uade.progra3.tpo.model;

import java.util.*;

public class Graph {
    private Map<String, Map<String, Integer>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(String source, String target, int weight) {
        adjacencyList.computeIfAbsent(source, k -> new HashMap<>()).put(target, weight);
        adjacencyList.computeIfAbsent(target, k -> new HashMap<>()).put(source, weight);
    }

    public Map<String, Integer> getNeighbors(String city) {
        return adjacencyList.getOrDefault(city, new HashMap<>());
    }

    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    public Integer getWeight(String source, String target) {
        return adjacencyList.getOrDefault(source, new HashMap<>()).getOrDefault(target, Integer.MAX_VALUE);
    }
}
