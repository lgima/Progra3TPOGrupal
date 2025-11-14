package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class BranchAndBoundAlgorithms {
    
    // Variables globales de la instancia para mantener el estado durante la recursión
    private int[][] distanceMatrix; // Si se elimina, no se podrá acceder rápidamente a las distancias entre ciudades
    private List<String> cities;    // Si se elimina, no se podrá mapear índices a nombres de ciudades
    private int n;                  // Si se elimina, no se podrá conocer el número de ciudades
    private int finalCost;          // Si se elimina, no se podrá almacenar el costo de la mejor ruta encontrada
    private List<String> finalPath; // Si se elimina, no se podrá almacenar la mejor ruta encontrada

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
        initialize(graph, citiesToVisit); // Si se elimina, no se inicializarán las estructuras necesarias
        
        // Arreglo para marcar ciudades visitadas en la recursión actual
        boolean[] visited = new boolean[n]; // Si se elimina, no se podrá rastrear las ciudades visitadas
        
        // Empezamos siempre por la primera ciudad (índice 0)
        visited[0] = true; // Si se elimina, la ciudad inicial no será marcada como visitada
        List<String> currentPath = new ArrayList<>(); // Si se elimina, no se podrá construir la ruta actual
        currentPath.add(cities.get(0)); // Si se elimina, no se agregará la ciudad inicial a la ruta
        
        // 2. Ejecución Recursiva (Branch & Bound) 
        // Nivel 1 porque ya tenemos la ciudad 0 en el path
        branchAndBound(visited, currentPath, 0, 1); // Si se elimina, no se realizará la búsqueda recursiva

        return Map.of(
            "route", finalPath, // Si se elimina, no se devolverá la mejor ruta encontrada
            "totalDistance", finalCost // Si se elimina, no se devolverá el costo total de la mejor ruta
        );
    }

    /**
     * Preparar las estructuras de datos para optimizar el acceso durante la recursión.
     * Convertir el grafo en una Matriz de Adyacencia para obtener distancias en O(1).
     */
    private void initialize(Graph graph, List<String> citiesToVisit) {
        this.cities = new ArrayList<>(citiesToVisit); // Si se elimina, no se almacenarán las ciudades a visitar
        this.n = cities.size(); // Si se elimina, no se calculará el número de ciudades
        this.distanceMatrix = new int[n][n]; // Si se elimina, no se creará la matriz de distancias
        
        // Inicializamos el mejor costo con "Infinito" para que la primera ruta válida siempre sea mejor.
        this.finalCost = Integer.MAX_VALUE; // Si se elimina, no se inicializará el costo óptimo
        this.finalPath = new ArrayList<>(); // Si se elimina, no se inicializará la ruta óptima

        // Llenado de la matriz O(n^2)
        for (int i = 0; i < n; i++) { // Si se elimina, no se llenará la matriz de distancias
            for (int j = 0; j < n; j++) {
                if (i != j) { // Si se elimina, se incluirán distancias inválidas
                    distanceMatrix[i][j] = graph.getWeight(cities.get(i), cities.get(j)); // Si se elimina, no se calcularán las distancias
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
        if (level == n) { // Si se elimina, no se detectará el caso base
            // Calcular costo de retorno al origen
            int lastCityIdx = cities.indexOf(currentPath.get(currentPath.size() - 1)); // Si se elimina, no se calculará la última ciudad
            int returnCost = distanceMatrix[lastCityIdx][0]; // Si se elimina, no se calculará el costo de retorno
            
            int totalRouteCost = currentCost + returnCost; // Si se elimina, no se calculará el costo total de la ruta

            // Si esta ruta completa es mejor que la mejor conocida hasta ahora:
            if (totalRouteCost < finalCost) { // Si se elimina, no se actualizará la solución óptima
                finalCost = totalRouteCost; // Si se elimina, no se guardará el nuevo costo óptimo
                finalPath = new ArrayList<>(currentPath); // Si se elimina, no se guardará la nueva ruta óptima
                finalPath.add(cities.get(0)); // Si se elimina, no se cerrará el ciclo en la ruta final
            }
            return; // Si se elimina, la recursión continuará innecesariamente
        }

        // RAMIFICACIÓN (Branch) 
        // Probamos movernos a cada ciudad disponible
        for (int i = 0; i < n; i++) { // Si se elimina, no se explorarán las ciudades
            
            // Solo si no la hemos visitado aún en este camino
            if (!visited[i]) { // Si se elimina, se podrían visitar ciudades repetidas
                int lastCityIdx = cities.indexOf(currentPath.get(currentPath.size() - 1)); // Si se elimina, no se calculará la última ciudad
                int travelCost = distanceMatrix[lastCityIdx][i]; // Si se elimina, no se calculará el costo de viajar a la siguiente ciudad
                int newCost = currentCost + travelCost; // Si se elimina, no se calculará el nuevo costo acumulado
                
                // PODA (Bound)
                // Si el costo acumulado ya es mayor o igual que una solución completa que encontramos antes,
                // cortamos la recursión.
                if (newCost < finalCost) { // Si se elimina, no se podarán ramas subóptimas
                    
                    // 1. Marcar (Do), entramos en la rama
                    visited[i] = true; // Si se elimina, no se marcará la ciudad como visitada
                    currentPath.add(cities.get(i)); // Si se elimina, no se agregará la ciudad a la ruta actual
                    
                    // 2. Recursión (Deep Dive), profundizamos
                    branchAndBound(visited, currentPath, newCost, level + 1); // Si se elimina, no se explorarán las ramas
                    
                    // 3. Desmarcar (Undo / Backtrack), salimos y cambiamos de rama
                    // Necesario para que el nodo padre pueda explorar otras opciones 
                    visited[i] = false; // Si se elimina, no se deshará el marcado de la ciudad
                    currentPath.remove(currentPath.size() - 1); // Si se elimina, no se deshará la ruta actual
                }
                // PODA IMPLÍCITA (El 'if' falla, por lo que ignoramos esta rama)
            }
        }
    }
}