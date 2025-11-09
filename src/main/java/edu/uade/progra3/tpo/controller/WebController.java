package edu.uade.progra3.tpo.controller;

import edu.uade.progra3.tpo.service.GraphService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Map;

@Controller
public class WebController {
    private final GraphService graphService;

    public WebController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/execute")
    public String executeAlgorithm(@RequestParam int option, Model model) {
        Object result = switch (option) {
            case 1 -> graphService.bfs("Buenos Aires");
            case 2 -> graphService.dfs("Buenos Aires");
            case 3 -> graphService.findMinimumSpanningTreePrim();
            case 4 -> graphService.findMinimumSpanningTreeKruskal();
            case 5 -> graphService.findShortestPath("Buenos Aires", "Mendoza");
            case 6 -> graphService.solveTSPGreedy("Buenos Aires");
            case 7 -> graphService.sortCitiesByDistance("Buenos Aires");
            case 8 -> graphService.findAllPaths("Buenos Aires", "Mendoza", 5);
            case 9 -> graphService.findAllCycles("Buenos Aires", 5);
            case 10 -> graphService.solveTSPBranchAndBound(
                Arrays.asList("Buenos Aires", "Córdoba", "Rosario", "Mendoza", "Mar del Plata")
            );
            case 11 -> Map.of(
                "cities", graphService.mergeSortCitiesByName(),
                "description", "Ciudades ordenadas alfabéticamente"
            );
            case 12 -> graphService.findShortestPathDijkstra("Buenos Aires", "Mendoza");
            default -> "Opción no válida";
        };

        model.addAttribute("result", result);
        model.addAttribute("algorithm", getAlgorithmName(option));
        return "result";
    }

    private String getAlgorithmName(int option) {
        return switch (option) {
            case 1 -> "Breadth-First Search (BFS)";
            case 2 -> "Depth-First Search (DFS)";
            case 3 -> "Minimum Spanning Tree - Prim";
            case 4 -> "Minimum Spanning Tree - Kruskal";
            case 5 -> "Shortest Path";
            case 6 -> "Greedy TSP";
            case 7 -> "Sort Cities by Distance";
            case 8 -> "All Paths (Dynamic Programming)";
            case 9 -> "Find Cycles (Backtracking)";
            case 10 -> "TSP (Branch and Bound)";
            case 11 -> "Merge Sort - Ordenamiento alfabético de ciudades";
            case 12 -> "Dijkstra - Camino más corto";
            default -> "Unknown Algorithm";
        };
    }
}
