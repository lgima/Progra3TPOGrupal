package edu.uade.progra3.tpo;

import edu.uade.progra3.tpo.service.GraphAlgorithms;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Progra3TpoApplicationTests {

    @Autowired
    private GraphAlgorithms graphAlgorithms;

    @Test
    void testBFS() {
        List<String> result = graphAlgorithms.bfs("Buenos Aires");
        assertNotNull(result);
        assertTrue(result.contains("Buenos Aires"));
        assertTrue(result.contains("La Plata"));
        assertTrue(result.contains("Córdoba"));
        assertTrue(result.contains("Santa Fe"));
        assertTrue(result.contains("Mendoza"));
    }

    @Test
    void testDFS() {
        List<String> result = graphAlgorithms.dfs("Buenos Aires");
        assertNotNull(result);
        assertTrue(result.contains("Buenos Aires"));
        assertTrue(result.contains("La Plata"));
        assertTrue(result.contains("Córdoba"));
        assertTrue(result.contains("Santa Fe"));
        assertTrue(result.contains("Mendoza"));
    }

    @Test
    void testShortestPath() {
        Map<String, Object> result = graphAlgorithms.shortestPath("Buenos Aires", "Mendoza");
        assertNotNull(result);
        assertNotNull(result.get("route"));
        assertTrue(result.get("totalDistance") instanceof Number);
        List<String> route = (List<String>) result.get("route");
        assertTrue(route.contains("Buenos Aires"));
        assertTrue(route.contains("Mendoza"));
    }

    @Test
    void testPrim() {
        Map<String, Object> result = graphAlgorithms.prim();
        assertNotNull(result);
        assertNotNull(result.get("routes"));
        assertTrue(result.get("totalCost") instanceof Number);
    }

    @Test
    void testKruskal() {
        Map<String, Object> result = graphAlgorithms.kruskal();
        assertNotNull(result);
        assertNotNull(result.get("routes"));
        assertTrue(result.get("totalCost") instanceof Number);
    }

    @Test
    void testGreedyTSP() {
        Map<String, Object> result = graphAlgorithms.greedyTSP("Buenos Aires");
        assertNotNull(result);
        assertNotNull(result.get("route"));
        assertTrue(result.get("totalDistance") instanceof Number);
        List<String> route = (List<String>) result.get("route");
        assertTrue(route.contains("Buenos Aires"));
    }

    @Test
    void testQuickSortCitiesByDistance() {
        List<Map<String, Object>> result = graphAlgorithms.quickSortCitiesByDistance("Buenos Aires");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).containsKey("city"));
        assertTrue(result.get(0).containsKey("distance"));
    }

    @Test
    void testFindAllPaths() {
        List<Map<String, Object>> result = graphAlgorithms.findAllPaths("Buenos Aires", "Mendoza", 5);
        assertNotNull(result);
        if (!result.isEmpty()) {
            assertTrue(result.get(0).containsKey("route"));
            assertTrue(result.get(0).containsKey("totalDistance"));
        }
    }

    @Test
    void testFindAllCycles() {
        List<Map<String, Object>> result = graphAlgorithms.findAllCycles("Buenos Aires", 5);
        assertNotNull(result);
        if (!result.isEmpty()) {
            assertTrue(result.get(0).containsKey("cycle"));
            assertTrue(result.get(0).containsKey("totalDistance"));
        }
    }

    @Test
    void testTspBranchAndBound() {
        List<String> cities = Arrays.asList("Buenos Aires", "La Plata", "Córdoba", "Santa Fe", "Mendoza");
        Map<String, Object> result = graphAlgorithms.tspBranchAndBound(cities);
        assertNotNull(result);
        assertNotNull(result.get("route"));
        assertTrue(result.get("totalDistance") instanceof Number);
    }
}
