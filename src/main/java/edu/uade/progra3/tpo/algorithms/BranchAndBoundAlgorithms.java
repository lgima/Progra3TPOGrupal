package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BranchAndBoundAlgorithms {

    private int[][] distanceMatrix;
    private List<String> cities;
    private int n;
    private int finalCost;
    private List<String> finalPath;

    public Map<String, Object> tspBranchAndBound(Graph graph, List<String> citiesToVisit) {
        // 1. Inicialización
        initialize(graph, citiesToVisit);

        boolean[] visited = new boolean[n];
        visited[0] = true;

        List<String> currentPath = new ArrayList<>();
        currentPath.add(cities.get(0));

        // 2. Búsqueda recursiva
        branchAndBound(visited, currentPath, 0, 1);

        return Map.of(
                "route", finalPath,
                "totalDistance", finalCost
        );
    }

    private void initialize(Graph graph, List<String> citiesToVisit) {
        this.cities = new ArrayList<>(citiesToVisit);
        this.n = cities.size();
        this.distanceMatrix = new int[n][n];

        this.finalCost = Integer.MAX_VALUE;
        this.finalPath = new ArrayList<>();

        // Llenar matriz de distancias
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    int w = graph.getWeight(cities.get(i), cities.get(j));
                    // Si no hay arista (0 o valor negativo), lo tratamos como infinito
                    distanceMatrix[i][j] = (w > 0) ? w : Integer.MAX_VALUE;
                }
            }
        }
    }

    private void branchAndBound(boolean[] visited, List<String> currentPath,
                               int currentCost, int level) {

        // Caso base: ya visitamos todas las ciudades
        if (level == n) {
            int lastCityIdx = cities.indexOf(currentPath.get(currentPath.size() - 1));
            int returnCost = distanceMatrix[lastCityIdx][0];

            // Si no hay camino de vuelta, no es una solución válida
            if (returnCost == Integer.MAX_VALUE) {
                return;
            }

            long totalRouteCostLong = (long) currentCost + returnCost;
            if (totalRouteCostLong >= Integer.MAX_VALUE || totalRouteCostLong < 0) {
                // Overflow o demasiado grande: descartamos
                return;
            }
            int totalRouteCost = (int) totalRouteCostLong;

            if (totalRouteCost < finalCost) {
                finalCost = totalRouteCost;
                finalPath = new ArrayList<>(currentPath);
                finalPath.add(cities.get(0)); // cerrar ciclo
            }
            return;
        }

        // Ramificación
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                int lastCityIdx = cities.indexOf(currentPath.get(currentPath.size() - 1));
                int travelCost = distanceMatrix[lastCityIdx][i];

                // Si no hay arista desde la última ciudad a esta, saltamos la rama
                if (travelCost == Integer.MAX_VALUE) {
                    continue;
                }

                long newCostLong = (long) currentCost + travelCost;
                if (newCostLong >= Integer.MAX_VALUE || newCostLong < 0) {
                    // Overflow o muy grande, peor que la mejor solución
                    continue;
                }
                int newCost = (int) newCostLong;

                // Poda: si ya es peor que la mejor ruta encontrada, no sigo
                if (newCost < finalCost) {
                    visited[i] = true;
                    currentPath.add(cities.get(i));

                    branchAndBound(visited, currentPath, newCost, level + 1);

                    visited[i] = false;
                    currentPath.remove(currentPath.size() - 1);
                }
            }
        }
    }
}
