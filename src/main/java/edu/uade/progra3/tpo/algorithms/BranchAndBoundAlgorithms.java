package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BranchAndBoundAlgorithms {
    
    // Variables globales de la instancia para mantener el estado durante la recursión
    private int[][] distanceMatrix; // Matriz de adyacencia para acceso rápido O(1) a distancias
    private List<String> cities;    // Mapeo de índice (int) a nombre de ciudad (String)
    private int n;                  // Número de ciudades
    private int finalCost;          // El costo de la mejor ruta encontrada hasta ahora
    private List<String> finalPath; // La mejor ruta encontrada hasta ahora

    /**
     * Algoritmo Branch and Bound (Ramificación y Poda) para TSP.
     * Encuentra ruta óptima visitando todas las ciudades indicadas exactamente una vez y volviendo al inicio.
     * Complejidad Temporal: O(n!) - Factorial
     * En el peor caso (donde no se puede podar nada), explora todas las permutaciones posibles.
     * Si n=10, son 3.6 millones de rutas. Si n=20, es computacionalmente imposible.
    
     * @param graph El grafo con las conexiones.
     * @param citiesToVisit Lista de ciudades que se deben incluir en el tour.
     * @return Mapa con la ruta óptima y su costo mínimo.
     */
    public Map<String, Object> tspBranchAndBound(Graph graph, List<String> citiesToVisit) {
        // 1. Inicialización
        initialize(graph, citiesToVisit);
        
        // Arreglo para marcar ciudades visitadas en la recursión actual
        boolean[] visited = new boolean[n];
        
        // Empezamos siempre por la primera ciudad (índice 0)
        visited[0] = true;
        List<String> currentPath = new ArrayList<>();
        currentPath.add(cities.get(0));
        
        // 2. Ejecución Recursiva (Branch & Bound) 
        // Nivel 1 porque ya tenemos la ciudad 0 en el path
        branchAndBound(visited, currentPath, 0, 1);

        return Map.of(
            "route", finalPath,
            "totalDistance", finalCost
        );
    }

    /**
     * Preparar las estructuras de datos para optimizar el acceso durante la recursión.
     * Convertir el grafo en una Matriz de Adyacencia para obtener distancias en O(1).
     */
    private void initialize(Graph graph, List<String> citiesToVisit) {
        this.cities = new ArrayList<>(citiesToVisit);
        this.n = cities.size();
        this.distanceMatrix = new int[n][n];
        
        // Inicializamos el mejor costo con "Infinito" para que la primera ruta válida siempre sea mejor.
        this.finalCost = Integer.MAX_VALUE;
        this.finalPath = new ArrayList<>();

        // Llenado de la matriz O(n^2)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    distanceMatrix[i][j] = graph.getWeight(cities.get(i), cities.get(j));
                }
            }
        }
    }

    /**
     * Función Recursiva Central.
     * @param visited Array booleano de ciudades visitadas en la rama actual.
     * @param currentPath Lista de ciudades en orden de visita actual.
     * @param currentCost Costo acumulado hasta el momento.
     * @param level Cuántas ciudades llevamos visitadas.
     */
    private void branchAndBound(boolean[] visited, List<String> currentPath, 
                              int currentCost, int level) {
        
        // CASO BASE: Ruta Completa
        // Si visitamos todas las ciudades (nivel == n)
        if (level == n) {
            // Calcular costo de retorno al origen
            int lastCityIdx = cities.indexOf(currentPath.get(currentPath.size() - 1));
            int returnCost = distanceMatrix[lastCityIdx][0]; // Volver a ciudad 0
            
            int totalRouteCost = currentCost + returnCost;

            // Si esta ruta completa es mejor que la mejor conocida hasta ahora:
            if (totalRouteCost < finalCost) {
                finalCost = totalRouteCost;
                finalPath = new ArrayList<>(currentPath);
                finalPath.add(cities.get(0)); // Cerrar el ciclo en la ruta final
            }
            return;
        }

        // RAMIFICACIÓN (Branch) 
        // Probamos movernos a cada ciudad disponible
        for (int i = 0; i < n; i++) {
            
            // Solo si no la hemos visitado aún en este camino
            if (!visited[i]) {
                int lastCityIdx = cities.indexOf(currentPath.get(currentPath.size() - 1));
                int travelCost = distanceMatrix[lastCityIdx][i];
                int newCost = currentCost + travelCost;
                
                // PODA (Bound)
                // Si el costo acumulado ya es mayor o igual que una solución completa que encontramos antes,
                // cortamos la recursión.
                if (newCost < finalCost) {
                    
                    // 1. Marcar (Do), entramos en la rama
                    visited[i] = true;
                    currentPath.add(cities.get(i));
                    
                    // 2. Recursión (Deep Dive), profundizamos
                    branchAndBound(visited, currentPath, newCost, level + 1);
                    
                    // 3. Desmarcar (Undo / Backtrack), salimos y cambiamos de rama
                    // Necesario para que el nodo padre pueda explorar otras opciones 
                    visited[i] = false;
                    currentPath.remove(currentPath.size() - 1);
                }
                // PODA IMPLÍCITA (El 'if' falla, por lo que ignoramos esta rama)
            }
        }
    }
}