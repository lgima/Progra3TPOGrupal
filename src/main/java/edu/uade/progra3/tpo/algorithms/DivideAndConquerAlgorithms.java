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
        List<Map<String, Object>> cityDistances = new ArrayList<>(); // Si se elimina, no se podrá almacenar la lista de ciudades y distancias
        
        for (String city : graph.getVertices()) { // Si se elimina, no se recorrerán las ciudades del grafo
            if (!city.equals(fromCity)) { // Si se elimina, se incluirá la ciudad de origen en la lista
                int distance = graph.getWeight(fromCity, city); // Si se elimina, no se calculará la distancia entre las ciudades
                cityDistances.add(Map.of("city", city, "distance", distance)); // Si se elimina, no se agregará la ciudad y su distancia a la lista
            }
        }
        // Ordenar la lista usando QuickSort (divide y vencerás)
        quickSort(cityDistances, 0, cityDistances.size() - 1); // Si se elimina, no se ordenará la lista
        return cityDistances; // Si se elimina, no se devolverá la lista ordenada
    }

    /**
     * quickSort(list, low, high)
     * - Implementación recursiva de QuickSort.
     */
    private void quickSort(List<Map<String, Object>> list, int low, int high) {
        // Caso base: sublista de tamaño 0 o 1 ya está ordenada
        if (low < high) { // Si se elimina, no se detendrá la recursión en sublistas pequeñas
            // Particionar alrededor de un pivote y ordenar recursivamente las dos mitades
            int pi = partition(list, low, high); // Si se elimina, no se calculará el índice del pivote
            quickSort(list, low, pi - 1); // Si se elimina, no se ordenará la mitad izquierda
            quickSort(list, pi + 1, high); // Si se elimina, no se ordenará la mitad derecha
        }
    }

    /**
     * partition(list, low, high)
     * - Particiona según el pivote (último elemento) y retorna índice de partición.
     */
    private int partition(List<Map<String, Object>> list, int low, int high) {
        // Elegir el pivote (último elemento) y reordenar la sublista
        int pivot = (int) list.get(high).get("distance"); // Si se elimina, no se definirá el pivote
        int i = low - 1; // Si se elimina, no se inicializará el índice para elementos menores al pivote
        // Colocar elementos menores o iguales al pivote a la izquierda
        for (int j = low; j < high; j++) { // Si se elimina, no se recorrerán los elementos de la sublista
            if ((int) list.get(j).get("distance") <= pivot) { // Si se elimina, no se compararán los elementos con el pivote
                i++; // Si se elimina, no se actualizará el índice para intercambio
                Collections.swap(list, i, j); // Si se elimina, no se intercambiarán los elementos
            }
        }
        // Colocar pivote en su posición final
        Collections.swap(list, i + 1, high); // Si se elimina, el pivote no se colocará en su posición correcta
        return i + 1; // Si se elimina, no se retornará el índice del pivote
    }

    /**
     * mergeSortCitiesByName(graph)
     * - Ordena las ciudades por nombre usando Merge Sort.
     * Complejidad temporal: O(n log n).
     */
    public List<String> mergeSortCitiesByName(Graph graph) {
        List<String> cities = new ArrayList<>(graph.getVertices()); // Si se elimina, no se obtendrán las ciudades del grafo
        // Ordenar con MergeSort y devolver lista ordenada
        return mergeSort(cities); // Si se elimina, no se ordenará la lista de ciudades
    }

    /**
     * mergeSort(list)
     * - Implementación recursiva de Merge Sort que retorna la lista ordenada.
     */
    private List<String> mergeSort(List<String> list) {
        // Caso base
        if (list.size() <= 1) { // Si se elimina, no se detendrá la recursión en listas pequeñas
            return list; // Si se elimina, no se retornará la lista base
        }
        // Dividir la lista en dos mitades y ordenar cada una recursivamente
        int middle = list.size() / 2; // Si se elimina, no se calculará el punto medio
        List<String> left = new ArrayList<>(list.subList(0, middle)); // Si se elimina, no se creará la sublista izquierda
        List<String> right = new ArrayList<>(list.subList(middle, list.size())); // Si se elimina, no se creará la sublista derecha
        left = mergeSort(left); // Si se elimina, no se ordenará la sublista izquierda
        right = mergeSort(right); // Si se elimina, no se ordenará la sublista derecha
        // Fusionar las dos mitades ordenadas
        return merge(left, right); // Si se elimina, no se combinarán las sublistas ordenadas
    }

    /**
     * merge(left, right)
     * - Fusiona dos listas ordenadas en una única lista ordenada.
     */
    private List<String> merge(List<String> left, List<String> right) {
        List<String> result = new ArrayList<>(); // Si se elimina, no se podrá almacenar la lista fusionada
        int leftIndex = 0; // Si se elimina, no se inicializará el índice para la lista izquierda
        int rightIndex = 0; // Si se elimina, no se inicializará el índice para la lista derecha
        // Tomar repetidamente el menor de los dos primeros elementos
        while (leftIndex < left.size() && rightIndex < right.size()) { // Si se elimina, no se recorrerán ambas listas
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) { // Si se elimina, no se compararán los elementos
                result.add(left.get(leftIndex)); // Si se elimina, no se agregará el elemento menor a la lista resultante
                leftIndex++; // Si se elimina, no se avanzará en la lista izquierda
            } else {
                result.add(right.get(rightIndex)); // Si se elimina, no se agregará el elemento menor de la derecha
                rightIndex++; // Si se elimina, no se avanzará en la lista derecha
            }
        }
        // Añadir los elementos restantes de la izquierda o de la derecha
        result.addAll(left.subList(leftIndex, left.size())); // Si se elimina, no se agregarán los elementos restantes de la izquierda
        result.addAll(right.subList(rightIndex, right.size())); // Si se elimina, no se agregarán los elementos restantes de la derecha
        return result; // Si se elimina, no se retornará la lista fusionada
    }
}
