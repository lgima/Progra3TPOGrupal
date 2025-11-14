package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class DivideAndConquerAlgorithms {
    /**
     * quickSortCitiesByDistance(graph, fromCity)
     * - Construye lista (ciudad, distancia) y ordena por distancia usando QuickSort.
     * Complejidad temporal: promedio O(n log n), peor O(n^2) si pivote es malo.
     */
    public List<Map<String, Object>> quickSortCitiesByDistance(Graph graph, String fromCity) {
        List<Map<String, Object>> cityDistances = new ArrayList<>();
        
        for (String city : graph.getVertices()) {
            if (!city.equals(fromCity)) {
                int distance = graph.getWeight(fromCity, city);
                cityDistances.add(Map.of("city", city, "distance", distance));
            }
        }

        // Ordenar la lista usando QuickSort (divide y vencerás)
        quickSort(cityDistances, 0, cityDistances.size() - 1);
        return cityDistances;
    }

    /**
     * quickSort(list, low, high)
     * - Implementación recursiva de QuickSort.
     */
    private void quickSort(List<Map<String, Object>> list, int low, int high) {
        // Caso base: sublista de tamaño 0 o 1 ya está ordenada
        if (low < high) {
            // Particionar alrededor de un pivote y ordenar recursivamente las dos mitades
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    /**
     * partition(list, low, high)
     * - Particiona según el pivote (último elemento) y retorna índice de partición.
     */
    private int partition(List<Map<String, Object>> list, int low, int high) {
        // Elegir el pivote (último elemento) y reordenar la sublista
        int pivot = (int) list.get(high).get("distance");
        int i = low - 1;

        // Colocar elementos menores o iguales al pivote a la izquierda
        for (int j = low; j < high; j++) {
            if ((int) list.get(j).get("distance") <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        // Colocar pivote en su posición final
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    /**
     * mergeSortCitiesByName(graph)
     * - Ordena las ciudades por nombre usando Merge Sort.
     * Complejidad temporal: O(n log n).
     */
    public List<String> mergeSortCitiesByName(Graph graph) {
        List<String> cities = new ArrayList<>(graph.getVertices());
        // Ordenar con MergeSort y devolver lista ordenada
        return mergeSort(cities);
    }

    /**
     * mergeSort(list)
     * - Implementación recursiva de Merge Sort que retorna la lista ordenada.
     */
    private List<String> mergeSort(List<String> list) {
        // Caso base
        if (list.size() <= 1) {
            return list;
        }

        // Dividir la lista en dos mitades y ordenar cada una recursivamente
        int middle = list.size() / 2;
        List<String> left = new ArrayList<>(list.subList(0, middle));
        List<String> right = new ArrayList<>(list.subList(middle, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        // Fusionar las dos mitades ordenadas
        return merge(left, right);
    }

    /**
     * merge(left, right)
     * - Fusiona dos listas ordenadas en una única lista ordenada.
     */
    private List<String> merge(List<String> left, List<String> right) {
        List<String> result = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        // Tomar repetidamente el menor de los dos primeros elementos
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        // Añadir los elementos restantes de la izquierda o de la derecha
        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));

        return result;
    }
}
