package edu.uade.progra3.tpo.algorithms;

import edu.uade.progra3.tpo.model.Graph;
import java.util.*;

public class DivideAndConquerAlgorithms {
    public List<Map<String, Object>> quickSortCitiesByDistance(Graph graph, String fromCity) {
        List<Map<String, Object>> cityDistances = new ArrayList<>();
        
        for (String city : graph.getVertices()) {
            if (!city.equals(fromCity)) {
                int distance = graph.getWeight(fromCity, city);
                cityDistances.add(Map.of("city", city, "distance", distance));
            }
        }

        quickSort(cityDistances, 0, cityDistances.size() - 1);
        return cityDistances;
    }

    private void quickSort(List<Map<String, Object>> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private int partition(List<Map<String, Object>> list, int low, int high) {
        int pivot = (int) list.get(high).get("distance");
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if ((int) list.get(j).get("distance") <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public List<String> mergeSortCitiesByName(Graph graph) {
        List<String> cities = new ArrayList<>(graph.getVertices());
        return mergeSort(cities);
    }

    private List<String> mergeSort(List<String> list) {
        if (list.size() <= 1) {
            return list;
        }

        int middle = list.size() / 2;
        List<String> left = new ArrayList<>(list.subList(0, middle));
        List<String> right = new ArrayList<>(list.subList(middle, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private List<String> merge(List<String> left, List<String> right) {
        List<String> result = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));

        return result;
    }
}
