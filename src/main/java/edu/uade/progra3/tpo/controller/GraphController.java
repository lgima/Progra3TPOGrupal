package edu.uade.progra3.tpo.controller;

import edu.uade.progra3.tpo.service.GraphService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/graph")
public class GraphController {
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    // Recorridos Básicos Gráfos
    @GetMapping("/bfs/{startCity}")
    public List<String> bfs(@PathVariable String startCity) {
        return graphService.bfs(startCity);
    }

    @GetMapping("/dfs/{startCity}")
    public List<String> dfs(@PathVariable String startCity) {
        return graphService.dfs(startCity);
    }

    // Arboles de Expansión Mínima - MST
    @GetMapping("/mst/prim")
    public Map<String, Object> prim() {
        return graphService.findMinimumSpanningTreePrim();
    }

    //Kruskal
    @GetMapping("/mst/kruskal")
    public Map<String, Object> kruskal() {
        return graphService.findMinimumSpanningTreeKruskal();
    }

    // Camino màs corto - Djikstra
    //calcula el camino más corto entre dos ciudades dadas (start y end) y devuelve la ruta junto con la distancia total.
    @GetMapping("/shortestPath")
    public Map<String, Object> shortestPath(
            @RequestParam String from,
            @RequestParam String to) {
        return graphService.findShortestPath(from, to);
    }

    // Greedy 
    @GetMapping("/greedy/tsp/{startCity}")
    public Map<String, Object> greedyTSP(@PathVariable String startCity) {
        return graphService.solveTSPGreedy(startCity);
    }

    // Divide y Conquista
    @GetMapping("/sort/cities/{fromCity}")
    public List<Map<String, Object>> sortCitiesByDistance(@PathVariable String fromCity) {
        return graphService.sortCitiesByDistance(fromCity);
    }

    // Programación Dinámica
    @GetMapping("/paths")
    public List<Map<String, Object>> findAllPaths(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "5") int maxLength) {
        return graphService.findAllPaths(from, to, maxLength);
    }

    // Backtracking
    @GetMapping("/cycles/{startCity}")
    public List<Map<String, Object>> findAllCycles(
            @PathVariable String startCity,
            @RequestParam(defaultValue = "5") int maxLength) {
        return graphService.findAllCycles(startCity, maxLength);
    }

    // Branch and Bound
    @PostMapping("/tsp/optimal")
    public Map<String, Object> tspBranchAndBound(@RequestBody List<String> cities) {
        return graphService.solveTSPBranchAndBound(cities);
    }
}