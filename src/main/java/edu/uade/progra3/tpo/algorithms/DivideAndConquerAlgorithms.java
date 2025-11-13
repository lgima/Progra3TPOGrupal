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
        // Crear una lista para almacenar las ciudades y sus distancias desde fromCity
        List<Map<String, Object>> cityDistances = new ArrayList<>();

        // Iterar sobre todos los vértices del grafo
        for (String city : graph.getVertices()) {
            if (!city.equals(fromCity)) { // Si se elimina, incluiría la ciudad de origen en la lista
                int distance = graph.getWeight(fromCity, city); // Si se elimina, no se calcularía la distancia
                cityDistances.add(Map.of("city", city, "distance", distance)); // Si se elimina, no se agregarían ciudades a la lista
            }
        }

        // Ordenar la lista usando QuickSort (divide y vencerás)
        quickSort(cityDistances, 0, cityDistances.size() - 1); // Si se elimina, la lista no estará ordenada
        return cityDistances; // Si se elimina, no se devolverá la lista ordenada
    }

    /**
     * quickSort(list, low, high)
     * - Implementación recursiva de QuickSort.
     */
    private void quickSort(List<Map<String, Object>> list, int low, int high) {
        if (low < high) { // Caso base: si se elimina, QuickSort no terminaría correctamente
            int pi = partition(list, low, high); // Si se elimina, no se dividiría la lista correctamente
            quickSort(list, low, pi - 1); // Si se elimina, no se ordenaría la mitad izquierda
            quickSort(list, pi + 1, high); // Si se elimina, no se ordenaría la mitad derecha
        }
    }

    /**
     * partition(list, low, high)
     * - Particiona según el pivote (último elemento) y retorna índice de partición.
     */
    private int partition(List<Map<String, Object>> list, int low, int high) {
        int pivot = (int) list.get(high).get("distance"); // Si se elimina, no habría pivote para dividir
        int i = low - 1; // Si se elimina, no se inicializaría el índice de partición

        for (int j = low; j < high; j++) { // Si se elimina, no se recorrerían los elementos para comparar con el pivote
            if ((int) list.get(j).get("distance") <= pivot) { // Si se elimina, no se identificarían elementos menores o iguales al pivote
                i++;
                Collections.swap(list, i, j); // Si se elimina, no se reorganizarían los elementos
            }
        }

        Collections.swap(list, i + 1, high); // Si se elimina, el pivote no se colocaría en su posición final
        return i + 1; // Si se elimina, no se retornaría el índice de partición
    }

    /**
     * mergeSortCitiesByName(graph)
     * - Ordena las ciudades por nombre usando Merge Sort.
     * Complejidad temporal: O(n log n).
     */
    public List<String> mergeSortCitiesByName(Graph graph) {
        List<String> cities = new ArrayList<>(graph.getVertices()); // Si se elimina, no se obtendrían las ciudades del grafo
        return mergeSort(cities); // Si se elimina, no se ordenaría la lista de ciudades
    }

    /**
     * mergeSort(list)
     * - Implementación recursiva de Merge Sort que retorna la lista ordenada.
     */
    private List<String> mergeSort(List<String> list) {
        if (list.size() <= 1) { // Caso base: si se elimina, Merge Sort no terminaría correctamente
            return list;
        }

        int middle = list.size() / 2; // Si se elimina, no se dividiría la lista en dos mitades
        List<String> left = new ArrayList<>(list.subList(0, middle)); // Si se elimina, no se crearía la mitad izquierda
        List<String> right = new ArrayList<>(list.subList(middle, list.size())); // Si se elimina, no se crearía la mitad derecha

        left = mergeSort(left); // Si se elimina, no se ordenaría recursivamente la mitad izquierda
        right = mergeSort(right); // Si se elimina, no se ordenaría recursivamente la mitad derecha

        return merge(left, right); // Si se elimina, no se fusionarían las mitades ordenadas
    }

    /**
     * merge(left, right)
     * - Fusiona dos listas ordenadas en una única lista ordenada.
     */
    private List<String> merge(List<String> left, List<String> right) {
        List<String> result = new ArrayList<>(); // Si se elimina, no se crearía la lista fusionada
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) { // Si se elimina, no se compararían los elementos de ambas listas
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) { // Si se elimina, no se identificaría el menor elemento
                result.add(left.get(leftIndex)); // Si se elimina, no se agregarían elementos de la izquierda
                leftIndex++;
            } else {
                result.add(right.get(rightIndex)); // Si se elimina, no se agregarían elementos de la derecha
                rightIndex++;
            }
        }

        result.addAll(left.subList(leftIndex, left.size())); // Si se elimina, no se agregarían los elementos restantes de la izquierda
        result.addAll(right.subList(rightIndex, right.size())); // Si se elimina, no se agregarían los elementos restantes de la derecha
        return result; // Si se elimina, no se retornaría la lista fusionada
    }
}
