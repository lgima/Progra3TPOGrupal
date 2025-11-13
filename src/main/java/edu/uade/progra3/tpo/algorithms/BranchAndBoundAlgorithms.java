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
        initialize(graph, citiesToVisit); // Si se elimina, no se inicializarán las estructuras necesarias

        boolean[] visited = new boolean[n]; // Si se elimina, no se podrá rastrear las ciudades visitadas
        visited[0] = true; // Si se elimina, la ciudad inicial no será marcada como visitada
        List<String> currentPath = new ArrayList<>(); // Si se elimina, no se podrá construir la ruta actual
        currentPath.add(cities.get(0)); // Si se elimina, no se agregará la ciudad inicial a la ruta

        // Empezar búsqueda con poda (branch & bound)
        branchAndBound(visited, currentPath, 0, 1); // Si se elimina, no se realizará la búsqueda

        return Map.of(
            "route", finalPath, // Si se elimina, no se devolverá la ruta óptima
            "totalDistance", finalCost // Si se elimina, no se devolverá el costo total
        );
    }

    /**
     * initialize(graph, citiesToVisit)
     * - Prepara matriz de distancias y estructuras iniciales.
     */
    private void initialize(Graph graph, List<String> citiesToVisit) {
        this.cities = new ArrayList<>(citiesToVisit); // Si se elimina, no se almacenarán las ciudades a visitar
        this.n = cities.size(); // Si se elimina, no se calculará el número de ciudades
        this.distanceMatrix = new int[n][n]; // Si se elimina, no se creará la matriz de distancias
        this.finalCost = Integer.MAX_VALUE; // Si se elimina, no se inicializará el costo óptimo
        this.finalPath = new ArrayList<>(); // Si se elimina, no se inicializará la ruta óptima

        for (int i = 0; i < n; i++) { // Si se elimina, no se llenará la matriz de distancias
            for (int j = 0; j < n; j++) {
                if (i != j) { // Si se elimina, se incluirán distancias inválidas
                    distanceMatrix[i][j] = graph.getWeight(cities.get(i), cities.get(j)); // Si se elimina, no se calcularán las distancias
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
        if (level == n) { // Si se elimina, no se detectará el caso base
            int lastCity = cities.indexOf(currentPath.get(currentPath.size() - 1)); // Si se elimina, no se calculará la última ciudad
            int returnCost = distanceMatrix[lastCity][0]; // Si se elimina, no se calculará el costo de retorno

            if (currentCost + returnCost < finalCost) { // Si se elimina, no se actualizará la solución óptima
                finalCost = currentCost + returnCost; // Si se elimina, no se guardará el nuevo costo óptimo
                finalPath = new ArrayList<>(currentPath); // Si se elimina, no se guardará la nueva ruta óptima
                finalPath.add(cities.get(0)); // Si se elimina, no se cerrará el ciclo en la ruta
            }
            return; // Si se elimina, la recursión continuará innecesariamente
        }

        for (int i = 0; i < n; i++) { // Si se elimina, no se explorarán las ciudades
            if (!visited[i]) { // Si se elimina, se podrían visitar ciudades repetidas
                int lastCity = cities.indexOf(currentPath.get(currentPath.size() - 1)); // Si se elimina, no se calculará la última ciudad
                int newCost = currentCost + distanceMatrix[lastCity][i]; // Si se elimina, no se calculará el nuevo costo

                if (newCost < finalCost) { // Si se elimina, no se podarán ramas subóptimas
                    visited[i] = true; // Si se elimina, no se marcará la ciudad como visitada
                    currentPath.add(cities.get(i)); // Si se elimina, no se agregará la ciudad a la ruta

                    branchAndBound(visited, currentPath, newCost, level + 1); // Si se elimina, no se explorarán las ramas

                    visited[i] = false; // Si se elimina, no se deshará el marcado de la ciudad
                    currentPath.remove(currentPath.size() - 1); // Si se elimina, no se deshará la ruta actual
                }
            }
        }
    }
}
