package edu.uade.progra3.tpo.controller;

import edu.uade.progra3.tpo.service.GraphAlgorithms;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graph")
public class GraphController {
    private final GraphAlgorithms graphAlgorithms;

    public GraphController(GraphAlgorithms graphAlgorithms) {
        this.graphAlgorithms = graphAlgorithms;
    }

    @GetMapping("/bfs/{startCity}")
    public List<String> bfs(@PathVariable String startCity) {
        return graphAlgorithms.bfs(startCity);
    }

    @GetMapping("/dfs/{startCity}")
    public List<String> dfs(@PathVariable String startCity) {
        return graphAlgorithms.dfs(startCity);
    }

    @GetMapping("/shortestPath")
    public Map<String, Object> shortestPath(
            @RequestParam String from,
            @RequestParam String to) {
        return graphAlgorithms.shortestPath(from, to);
    }

    @GetMapping("/prim")
    public Map<String, Object> prim() {
        return graphAlgorithms.prim();
    }

    @GetMapping("/kruskal")
    public Map<String, Object> kruskal() {
        return graphAlgorithms.kruskal();
    }

    @GetMapping("/greedy/tsp/{startCity}")
    public Map<String, Object> greedyTSP(@PathVariable String startCity) {
        return graphAlgorithms.greedyTSP(startCity);
    }

    @GetMapping("/quicksort/distances/{fromCity}")
    public List<Map<String, Object>> quickSortCitiesByDistance(@PathVariable String fromCity) {
        return graphAlgorithms.quickSortCitiesByDistance(fromCity);
    }

    @GetMapping("/dynamic/paths")
    public List<Map<String, Object>> findAllPaths(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "5") int maxLength) {
        return graphAlgorithms.findAllPaths(from, to, maxLength);
    }

    @GetMapping("/backtracking/cycles/{startCity}")
    public List<Map<String, Object>> findAllCycles(
            @PathVariable String startCity,
            @RequestParam(defaultValue = "5") int maxLength) {
        return graphAlgorithms.findAllCycles(startCity, maxLength);
    }

    @PostMapping("/branch-and-bound/tsp")
    public Map<String, Object> tspBranchAndBound(@RequestBody List<String> cities) {
        return graphAlgorithms.tspBranchAndBound(cities);
    }
}