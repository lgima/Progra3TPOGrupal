package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class MinimumSpanningTreeAlgorithms {

    /**
     * Algoritmo de Prim.
     * Construye el Árbol de Expansión Mínima (MST) creciendo desde un nodo inicial arbitrario.
     * Complejidad Temporal: O(E log V) donde V es el número de vértices y E el número de aristas.
     * 
     * Costo de insertar/extraer de la PriorityQueue.
     * En el peor caso, procesamos todas las aristas conectadas.
     * 
     * @param graph El grafo conexo.
     * @return Mapa con las rutas seleccionadas y el costo total.
     */
    public Map<String, Object> prim(Graph graph) {
        // --- 1. Estructuras de Datos ---
        
        // Control de nodos ya incluidos en el MST.
        Set<String> visited = new HashSet<>();
        
        // Lista final de aristas que componen el árbol.
        List<List<String>> routes = new ArrayList<>();
        
        // Acumulador del peso total del árbol.
        int totalCost = 0;
        
        // Min-Heap: Mantiene las aristas ordenadas por menor peso.
        PriorityQueue<Edge> pq = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.weight)
        );

        // --- 2. Inicialización ---
        
        // Elegimos un vértice arbitrario para comenzar (primero que encontremos).
        String start = graph.getVertices().iterator().next();
        visited.add(start);

        // --- 3. Bucle Principal (Crecimiento del Árbol) ---
        
        // Bucle hasta que hayamos conectado todos los vértices del grafo.
        while (visited.size() < graph.getVertices().size()) {
            
            // Paso A: Identificar aristas frontera
            // (Añadir todas las conexiones desde los nodos visitados hacia el exterior)
            for (String vertex : visited) {
                for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(vertex).entrySet()) {
                    // Solo nos interesan aristas que lleven a territorio no visitado
                    if (!visited.contains(neighbor.getKey())) {
                        pq.offer(new Edge(vertex, neighbor.getKey(), neighbor.getValue()));
                    }
                }
            }

            // Paso B: Selección Greedy
            // Extraemos la arista más barata disponible en la frontera.
            Edge edge = pq.poll();
            
            // Si la cola se vacía y no cubrimos todos los nodos, el grafo no es conexo.
            if (edge == null) break; 

            // Paso C: Expansión 
            // Si el nodo destino ya fue visitado por otro camino más barato antes, descartamos esta arista.
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

    // Clase auxiliar para manejar aristas en la PriorityQueue
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
     * Algoritmo de Kruskal.
     * Construye el MST uniendo "bosques" (conjuntos disjuntos) mediante las aristas más baratas.
     * Análisis de Complejidad Temporal:
     * Ordenamiento: O(E log E) debido a la ordenación de aristas.
     * Gestión de Conjuntos: En esta implementación manual es O(E * V) en peor caso. 
     * @param graph El grafo.
     * @return Mapa con las aristas seleccionadas y el peso total.
     */
    public Map<String, Object> kruskal(Graph graph) {
        
        List<KruskalEdge> resultEdges = new ArrayList<>();
        int totalWeight = 0;
        
        // 1. Inicialización (Disjoint Sets / Bosque) 
        // Mapa 'forest': Cada nodo inicia en su propio conjunto aislado.
        // Key: ID del conjunto, Value: Set de vértices en ese conjunto.
        Map<String, Set<String>> forest = new HashMap<>();
        
        for (String vertex : graph.getVertices()) {
            Set<String> set = new HashSet<>();
            set.add(vertex);
            forest.put(vertex, set);
        }
        
        // 2. Preparación de Aristas 
        // Aplanamos el grafo para tener una lista simple de todas las aristas.
        List<KruskalEdge> allEdges = new ArrayList<>();
        for (String from : graph.getVertices()) {
            for (Map.Entry<String, Integer> entry : graph.getNeighbors(from).entrySet()) {
                allEdges.add(new KruskalEdge(from, entry.getKey(), entry.getValue()));
            }
        }
        
        //3. Ordenamiento 
        // Ordenamos de menor a mayor peso. O(E log E).
        Collections.sort(allEdges);
        
        // --- 4. Procesamiento y Unión ---
        for (KruskalEdge edge : allEdges) {
            // Buscamos a qué conjunto pertenece cada extremo de la arista
            String setA = findSet(forest, edge.from);
            String setB = findSet(forest, edge.to);
            
            // Solo unimos si están en conjuntos distintos.
            // Si setA == setB, significa que ya están conectados por otro camino
            if (setA != null && setB != null && !setA.equals(setB)) {
                
                // Aceptamos la arista en la solución
                resultEdges.add(edge);
                totalWeight += edge.weight;
                
                // Fusionamos los dos conjuntos en uno solo
                union(forest, setA, setB);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("edges", resultEdges);
        result.put("totalWeight", totalWeight);
        return result;
    }
    
    /**
     * Operación Find (Buscar Conjunto).
     * Determina el identificador del conjunto al que pertenece un vértice.
     * Complejidad: O(V)(recorre linealmente).
     */
    private String findSet(Map<String, Set<String>> forest, String vertex) {
        for (Map.Entry<String, Set<String>> entry : forest.entrySet()) {
            if (entry.getValue().contains(vertex)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Operación Union (Unir Conjuntos).
     * Fusiona dos componentes conexos.
     * Complejidad: O(V)(copia sets).
     */
    private void union(Map<String, Set<String>> forest, String setA, String setB) {
        // Creamos un 'Set' con elementos de A y B
        Set<String> unionSet = new HashSet<>(forest.get(setA));
        unionSet.addAll(forest.get(setB));
        
        // Actualizamos la referencia en el mapa para los nodos involucrados
        // para que apunten al nuevo conjunto unido.
        for (String vertex : unionSet) {
            forest.put(vertex, unionSet);
        }
    }
    
    // Clase auxiliar que implementa Comparable para poder usar Collections.sort()
    private static class KruskalEdge implements Comparable<KruskalEdge> {
        String from; // Origen
        String to;   // Destino
        int weight;  // Peso
        
        KruskalEdge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        
        @Override
        public int compareTo(KruskalEdge other) {
            // Define el orden natural por peso ascendente
            return Integer.compare(this.weight, other.weight);
        }
    }
}