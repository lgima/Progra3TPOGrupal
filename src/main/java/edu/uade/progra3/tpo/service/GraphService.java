package edu.uade.progra3.tpo.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.uade.progra3.tpo.algorithms.BacktrackingAlgorithms;
import edu.uade.progra3.tpo.algorithms.BranchAndBoundAlgorithms;
import edu.uade.progra3.tpo.algorithms.DivideAndConquerAlgorithms;
import edu.uade.progra3.tpo.algorithms.DynamicProgrammingAlgorithms;
import edu.uade.progra3.tpo.algorithms.GraphBasicAlgorithms;
import edu.uade.progra3.tpo.algorithms.GreedyAlgorithms;
import edu.uade.progra3.tpo.algorithms.MinimumSpanningTreeAlgorithms;
import edu.uade.progra3.tpo.algorithms.PathFindingAlgorithms;
import edu.uade.progra3.tpo.model.City;
import edu.uade.progra3.tpo.model.Graph;
import edu.uade.progra3.tpo.model.Road;
import edu.uade.progra3.tpo.repository.CityRepository;

@Service
public class GraphService {
    private static final Logger logger = LoggerFactory.getLogger(GraphService.class);
    private final CityRepository cityRepository;
    private final GraphBasicAlgorithms basicAlgorithms;
    private final MinimumSpanningTreeAlgorithms mstAlgorithms;
    private final GreedyAlgorithms greedyAlgorithms;
    private final DivideAndConquerAlgorithms divideAndConquerAlgorithms;
    private final DynamicProgrammingAlgorithms dpAlgorithms;
    private final BacktrackingAlgorithms backtrackingAlgorithms;
    private final BranchAndBoundAlgorithms branchAndBoundAlgorithms;
    private final PathFindingAlgorithms pathFindingAlgorithms;

    private Graph cachedGraph;
    private long lastGraphBuildTime;
    private static final long CACHE_DURATION = 60000; // 1 minute cache

    public GraphService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        this.basicAlgorithms = new GraphBasicAlgorithms();
        this.mstAlgorithms = new MinimumSpanningTreeAlgorithms();
        this.greedyAlgorithms = new GreedyAlgorithms();
        this.divideAndConquerAlgorithms = new DivideAndConquerAlgorithms();
        this.dpAlgorithms = new DynamicProgrammingAlgorithms();
        this.backtrackingAlgorithms = new BacktrackingAlgorithms();
        this.branchAndBoundAlgorithms = new BranchAndBoundAlgorithms();
        this.pathFindingAlgorithms = new PathFindingAlgorithms();
    }

    private Graph buildGraphFromDatabase() {
        // Check if cached graph is still valid
        if (cachedGraph != null && System.currentTimeMillis() - lastGraphBuildTime < CACHE_DURATION) {
            return cachedGraph;
        }

        try {
            Graph graph = new Graph();
            List<City> cities = cityRepository.findAll();
            
            for (City city : cities) {
                for (Road road : city.getRoads()) {
                    graph.addEdge(
                        city.getName(),
                        road.getDestination().getName(),
                        road.getKm()
                    );
                }
            }
            
            cachedGraph = graph;
            lastGraphBuildTime = System.currentTimeMillis();
            return graph;
        } catch (Exception e) {
            logger.error("Error building graph from database", e);
            throw new RuntimeException("Failed to build graph from database", e);
        }
    }

    private Graph getGraph() {
        Graph graph = buildGraphFromDatabase();
        if (graph.getVertices().isEmpty()) {
            throw new IllegalStateException("Graph is empty. Please check database connectivity.");
        }
        return graph;
    }

    public List<String> bfs(String startCity) {
        validateCity(startCity);
        return basicAlgorithms.bfs(getGraph(), startCity);
    }

    public List<String> dfs(String startCity) {
        validateCity(startCity);
        return basicAlgorithms.dfs(getGraph(), startCity);
    }

    public Map<String, Object> findShortestPath(String startCity, String endCity) {
        validateCity(startCity);
        validateCity(endCity);
        
        // Usar un maxLength razonable: número de ciudades (peor caso: visitar todas)
        int maxLength = getGraph().getVertices().size();
        
        List<Map<String, Object>> paths = dpAlgorithms.findAllPaths(getGraph(), startCity, endCity, maxLength);
        
        return paths.stream()
            .min(Comparator.comparingLong(m -> (Long) m.get("totalDistance")))
            .orElse(Map.of("route", List.of(), "totalDistance", 0));
    }

    public Map<String, Object> findShortestPathDijkstra(String startCity, String endCity) {
        validateCity(startCity);
        validateCity(endCity);
        return pathFindingAlgorithms.dijkstra(getGraph(), startCity, endCity);
    }

    public Map<String, Object> findMinimumSpanningTreePrim() {
        return mstAlgorithms.prim(getGraph());
    }

    public Map<String, Object> findMinimumSpanningTreeKruskal() {
        return mstAlgorithms.kruskal(getGraph());
    }

    public Map<String, Object> solveTSPGreedy(String startCity) {
        return greedyAlgorithms.greedyTSP(getGraph(), startCity);
    }

    public List<Map<String, Object>> sortCitiesByDistance(String fromCity) {
        return divideAndConquerAlgorithms.quickSortCitiesByDistance(getGraph(), fromCity);
    }

    public List<Map<String, Object>> findAllPaths(String startCity, String endCity, int maxLength) {
        return dpAlgorithms.findAllPaths(getGraph(), startCity, endCity, maxLength);
    }

    public List<Map<String, Object>> findAllCycles(String startCity, int maxLength) {
        return backtrackingAlgorithms.findAllCycles(getGraph(), startCity, maxLength);
    }

    public Map<String, Object> solveTSPBranchAndBound(List<String> cities) {
        return branchAndBoundAlgorithms.tspBranchAndBound(getGraph(), cities);
    }

    public List<String> mergeSortCitiesByName() {
        return divideAndConquerAlgorithms.mergeSortCitiesByName(getGraph());
    }

    private void validateCity(String cityName) {
        if (!cityRepository.existsById(cityName)) {
            throw new IllegalArgumentException("City not found: " + cityName);
        }
    }
}