package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BranchAndBoundAlgorithms {
    private int[][] distanceMatrix;
    private List<String> cities;
    private int n;
    private int finalCost;
    private List<String> finalPath;

    /**
     * tspBranchAndBound(graph, citiesToVisit)
     * - Estrategia Branch and Bound para TSP sobre el subconjunto dado.
     * - Explora permutaciones y poda ramas cuyo coste acumulado >= mejor coste conocido.
     * Complejidad temporal: en el peor caso exponencial/factorial (O(n!)), aunque se poda búsqueda.
     */
    public Map<String, Object> tspBranchAndBound(Graph graph, List<String> citiesToVisit) {
        // Preparar estructuras: ciudades, matriz de distancias y valores iniciales
        initialize(graph, citiesToVisit);
        
        boolean[] visited = new boolean[n];
        visited[0] = true;
        List<String> currentPath = new ArrayList<>();
        currentPath.add(cities.get(0));
        
        // Empezar búsqueda con poda (branch & bound)
        branchAndBound(visited, currentPath, 0, 1);

        return Map.of(
            "route", finalPath,
            "totalDistance", finalCost
        );
    }

    /**
     * initialize(graph, citiesToVisit)
     * - Prepara matriz de distancias y estructuras iniciales.
     */
    private void initialize(Graph graph, List<String> citiesToVisit) {
        // Guardar lista de ciudades y construir matriz de costes entre ellas
        this.cities = new ArrayList<>(citiesToVisit);
        this.n = cities.size();
        this.distanceMatrix = new int[n][n];
        this.finalCost = Integer.MAX_VALUE;
        this.finalPath = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    // Cargar coste entre city i y city j
                    distanceMatrix[i][j] = graph.getWeight(cities.get(i), cities.get(j));
                }
            }
        }
    }

    /**
     * branchAndBound(visited, currentPath, currentCost, level)
     * - Función recursiva que genera permutaciones con poda por coste.
     */
    private void branchAndBound(boolean[] visited, List<String> currentPath, 
                              int currentCost, int level) {
        // Si ya hemos visitado todas las ciudades, calcular coste de retorno y actualizar solución
        if (level == n) {
            int lastCity = cities.indexOf(currentPath.get(currentPath.size() - 1));
            int returnCost = distanceMatrix[lastCity][0];
            
            if (currentCost + returnCost < finalCost) {
                // Hemos encontrado una solución mejor; guardarla
                finalCost = currentCost + returnCost;
                finalPath = new ArrayList<>(currentPath);
                finalPath.add(cities.get(0));
            }
            return;
        }

        // Intentar cada ciudad no visitada, podando ramas cuyo coste ya excede finalCost
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                int lastCity = cities.indexOf(currentPath.get(currentPath.size() - 1));
                int newCost = currentCost + distanceMatrix[lastCity][i];
                
                if (newCost < finalCost) {
                    // Explorar esta rama (marcar, añadir a camino, recursar)
                    visited[i] = true;
                    currentPath.add(cities.get(i));
                    
                    branchAndBound(visited, currentPath, newCost, level + 1);
                    
                    // Deshacer cambios (backtrack)
                    visited[i] = false;
                    currentPath.remove(currentPath.size() - 1);
                } // si newCost >= finalCost se poda esta rama
            }
        }
    }
}
