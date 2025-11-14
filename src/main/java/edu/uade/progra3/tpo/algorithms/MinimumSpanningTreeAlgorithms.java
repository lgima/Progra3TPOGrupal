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
        Set<String> visited = new HashSet<>(); // Si se elimina, no se podrá rastrear los nodos ya incluidos en el MST
        
        // Lista final de aristas que componen el árbol.
        List<List<String>> routes = new ArrayList<>(); // Si se elimina, no se podrán almacenar las aristas del MST
        
        // Acumulador del peso total del árbol.
        int totalCost = 0; // Si se elimina, no se podrá calcular el costo total del MST
        
        // Min-Heap: Mantiene las aristas ordenadas por menor peso.
        PriorityQueue<Edge> pq = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.weight)
        ); // Si se elimina, no se podrán gestionar las aristas por peso mínimo

        // --- 2. Inicialización ---
        
        // Elegimos un vértice arbitrario para comenzar (primero que encontremos).
        String start = graph.getVertices().iterator().next(); // Si se elimina, no se podrá seleccionar un nodo inicial
        visited.add(start); // Si se elimina, el nodo inicial podría ser procesado múltiples veces

        // --- 3. Bucle Principal (Crecimiento del Árbol) ---
        
        // Bucle hasta que hayamos conectado todos los vértices del grafo.
        while (visited.size() < graph.getVertices().size()) { // Si se elimina, no se recorrerán todos los nodos del grafo
            
            // Paso A: Identificar aristas frontera
            // (Añadir todas las conexiones desde los nodos visitados hacia el exterior)
            for (String vertex : visited) { // Si se elimina, no se identificarán las aristas frontera
                for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(vertex).entrySet()) { // Si se elimina, no se explorarán los vecinos de los nodos visitados
                    // Solo nos interesan aristas que lleven a territorio no visitado
                    if (!visited.contains(neighbor.getKey())) { // Si se elimina, se podrían agregar aristas redundantes
                        pq.offer(new Edge(vertex, neighbor.getKey(), neighbor.getValue())); // Si se elimina, no se agregarán aristas a la cola de prioridad
                    }
                }
            }

            // Paso B: Selección Greedy
            // Extraemos la arista más barata disponible en la frontera.
            Edge edge = pq.poll(); // Si se elimina, no se seleccionará la arista de menor peso
            
            // Si la cola se vacía y no cubrimos todos los nodos, el grafo no es conexo.
            if (edge == null) break; // Si se elimina, no se manejarán grafos no conexos

            // Paso C: Expansión 
            // Si el nodo destino ya fue visitado por otro camino más barato antes, descartamos esta arista.
            if (!visited.contains(edge.target)) { // Si se elimina, se podrían incluir ciclos en el MST
                visited.add(edge.target); // Si se elimina, el nodo destino podría ser procesado múltiples veces
                routes.add(Arrays.asList(edge.source, edge.target)); // Si se elimina, no se registrará la arista en el MST
                totalCost += edge.weight; // Si se elimina, no se acumulará el costo total del MST

            }
        }

        Map<String, Object> result = new HashMap<>(); // Si se elimina, no se podrá almacenar el resultado final
        result.put("routes", routes); // Si se elimina, no se incluirán las aristas del MST en el resultado
        result.put("totalCost", totalCost); // Si se elimina, no se incluirá el costo total en el resultado
        return result; // Si se elimina, no se devolverá el resultado del algoritmo
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
        List<KruskalEdge> resultEdges = new ArrayList<>(); // Almacena las aristas seleccionadas para el MST. Si se elimina, no se podrá construir el resultado.
        int totalWeight = 0; // Acumula el peso total del MST. Si se elimina, no se podrá calcular el costo total.
        Map<String, Set<String>> forest = new HashMap<>(); // Mapa para gestionar los conjuntos disjuntos. Si se elimina, no se podrá rastrear qué nodos están conectados.
        for (String vertex : graph.getVertices()) {
            Set<String> set = new HashSet<>(); // Crea un conjunto para cada vértice. Si se elimina, no se podrá inicializar el bosque.
            set.add(vertex); // Agrega el vértice al conjunto. Si se elimina, el conjunto estará vacío.
            forest.put(vertex, set); // Asocia el vértice con su conjunto. Si se elimina, no se podrá rastrear el conjunto de cada vértice.
        }
        List<KruskalEdge> allEdges = new ArrayList<>(); // Lista de todas las aristas del grafo. Si se elimina, no se podrá procesar el grafo.
        for (String from : graph.getVertices()) {
            for (Map.Entry<String, Integer> entry : graph.getNeighbors(from).entrySet()) {
                allEdges.add(new KruskalEdge(from, entry.getKey(), entry.getValue())); // Agrega cada arista a la lista. Si se elimina, el algoritmo no tendrá datos para procesar.
            }
        }
        Collections.sort(allEdges); // Ordena las aristas por peso. Si se elimina, el algoritmo no procesará las aristas en el orden correcto.
        for (KruskalEdge edge : allEdges) {
            String setA = findSet(forest, edge.from); // Encuentra el conjunto del vértice origen. Si se elimina, no se podrá verificar la conexión.
            String setB = findSet(forest, edge.to); // Encuentra el conjunto del vértice destino. Si se elimina, no se podrá verificar la conexión.
            if (setA != null && setB != null && !setA.equals(setB)) { // Verifica si los vértices están en conjuntos distintos. Si se elimina, el algoritmo podría incluir ciclos.
                resultEdges.add(edge); // Agrega la arista al MST. Si se elimina, el MST estará incompleto.
                totalWeight += edge.weight; // Suma el peso de la arista al costo total. Si se elimina, el costo será incorrecto.
                union(forest, setA, setB); // Une los conjuntos de los vértices. Si se elimina, el algoritmo no podrá conectar los nodos.
            }
        }
        Map<String, Object> result = new HashMap<>(); // Crea el mapa para almacenar el resultado. Si se elimina, no se podrá devolver el resultado.
        result.put("edges", resultEdges); // Agrega las aristas al resultado. Si se elimina, el resultado no incluirá las aristas.
        result.put("totalWeight", totalWeight); // Agrega el peso total al resultado. Si se elimina, el resultado no incluirá el costo.
        return result; // Devuelve el resultado del algoritmo. Si se elimina, el algoritmo no tendrá salida.
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

        @Override
        public String toString() {
            return "{" +
                   "de: '" + from + '\'' +
                   ", a: '" + to + '\'' +
                   ", peso: " + weight +
                   '}';
        }
    }
}