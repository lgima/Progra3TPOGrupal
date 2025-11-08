package edu.uade.progra3.tpo.service;

import edu.uade.progra3.tpo.model.City;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphAlgorithms {
    private final Neo4jClient neo4jClient;

    public GraphAlgorithms(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    // BFS - Breadth First Search
    public List<String> bfs(String startCity) {
        List<String> visited = new ArrayList<>();
        String query = """
            MATCH (start:City {name: $startCity})
            CALL apoc.path.expandConfig(start, {
                relationshipFilter: 'ROAD',
                uniqueness: 'NODE_GLOBAL',
                bfs: true
            })
            YIELD path
            RETURN [node IN nodes(path) | node.name] as route
        """;

        neo4jClient.query(query)
            .bind(startCity).to("startCity")
            .fetch()
            .all().forEach(result -> {
                List<String> route = (List<String>) result.get("route");
                visited.addAll(route.stream()
                    .filter(city -> !visited.contains(city))
                    .toList());
            });

        return visited;
    }

    // DFS - Depth First Search
    public List<String> dfs(String startCity) {
        List<String> visited = new ArrayList<>();
        String query = """
            MATCH (start:City {name: $startCity})
            CALL apoc.path.expandConfig(start, {
                relationshipFilter: 'ROAD',
                uniqueness: 'NODE_GLOBAL',
                bfs: false
            })
            YIELD path
            RETURN [node IN nodes(path) | node.name] as route
        """;

        neo4jClient.query(query)
            .bind(startCity).to("startCity")
            .fetch()
            .all().forEach(result -> {
                List<String> route = (List<String>) result.get("route");
                visited.addAll(route.stream()
                    .filter(city -> !visited.contains(city))
                    .toList());
            });

        return visited;
    }

    // Método para obtener el camino más corto entre dos ciudades
    public Map<String, Object> shortestPath(String startCity, String endCity) {
        String query = """
            MATCH (start:City {name: $startCity}), (end:City {name: $endCity})
            CALL apoc.algo.dijkstra(start, end, 'ROAD', 'km')
            YIELD path, weight
            RETURN [node IN nodes(path) | node.name] as route,
                   weight as totalDistance
        """;

        Collection<Map<String, Object>> results = neo4jClient.query(query)
            .bind(startCity).to("startCity")
            .bind(endCity).to("endCity")
            .fetch()
            .all();
            
        Optional<Map<String, Object>> firstResult = results.stream().findFirst();
        if (firstResult.isPresent()) {
            Map<String, Object> result = firstResult.get();
            Map<String, Object> response = new HashMap<>();
            response.put("route", result.get("route"));
            response.put("totalDistance", result.get("totalDistance"));
            return response;
        }
        
        return Map.of("route", List.of(), "totalDistance", 0);
    }

    // Algoritmo de Prim para Árbol de Expansión Mínima
    public Map<String, Object> prim() {
        String query = """
            MATCH (n:City)
            WITH collect(n) as nodes
            MATCH (source:City)-[r:ROAD]-(target:City)
            WHERE id(source) < id(target)
            WITH collect({source: source, target: target, weight: r.km}) as edges
            UNWIND edges as edge
            WITH edge
            ORDER BY edge.weight
            WITH collect(edge) as sortedEdges,
                 head(collect(edge)) as firstEdge
            WITH sortedEdges,
                 collect(firstEdge.source.name) + collect(firstEdge.target.name) as visited,
                 [firstEdge] as mst
            UNWIND range(1, size(sortedEdges)-1) as i
            WITH visited, mst, sortedEdges[i] as edge
            WHERE (edge.source.name IN visited AND NOT edge.target.name IN visited)
               OR (edge.target.name IN visited AND NOT edge.source.name IN visited)
            WITH DISTINCT visited + 
                 CASE WHEN edge.source.name IN visited 
                      THEN [edge.target.name]
                      ELSE [edge.source.name]
                 END as visited,
                 mst + edge as mst
            WITH mst
            LIMIT 1
            RETURN collect([edge IN mst | [edge.source.name, edge.target.name]]) as routes,
                   reduce(total = 0, edge IN mst | total + edge.weight) as totalCost
        """;

        return neo4jClient.query(query)
            .fetch()
            .one()
            .map(result -> {
                Map<String, Object> response = new HashMap<>();
                response.put("routes", result.get("routes"));
                response.put("totalCost", result.get("totalCost"));
                return response;
            })
            .orElse(Map.of("routes", List.of(), "totalCost", 0));
    }

    // Algoritmo de Kruskal
    public Map<String, Object> kruskal() {
        String query = """
            MATCH (a:City)-[r:ROAD]-(b:City)
            WHERE id(a) < id(b)
            WITH collect({source: a, target: b, weight: r.km}) as edges
            UNWIND edges as edge
            WITH edge
            ORDER BY edge.weight
            WITH collect(edge) as sortedEdges,
                 head(collect(edge)) as firstEdge
            WITH [[firstEdge.source.name, firstEdge.target.name]] as components,
                 [firstEdge] as mst,
                 sortedEdges as allEdges
            UNWIND range(1, size(allEdges)-1) as i
            WITH components, mst, allEdges[i] as edge
            WHERE NOT any(comp IN components WHERE edge.source.name IN comp AND edge.target.name IN comp)
            WITH DISTINCT components + [[edge.source.name, edge.target.name]] as components,
                 mst + edge as mst
            WITH mst
            LIMIT 1
            RETURN collect([edge IN mst | [edge.source.name, edge.target.name]]) as routes,
                   reduce(total = 0, edge IN mst | total + edge.weight) as totalCost
        """;

        return neo4jClient.query(query)
            .fetch()
            .one()
            .map(result -> {
                Map<String, Object> response = new HashMap<>();
                response.put("routes", result.get("routes"));
                response.put("totalCost", result.get("totalCost"));
                return response;
            })
            .orElse(Map.of("routes", List.of(), "totalCost", 0));
    }

    // Algoritmo Greedy para encontrar el camino que visita todas las ciudades
    public Map<String, Object> greedyTSP(String startCity) {
        String query = """
            MATCH (start:City {name: $startCity})
            WITH start
            MATCH path = (start)-[:ROAD*]-(city:City)
            WHERE ALL(n IN nodes(path) WHERE size([x IN nodes(path) WHERE x = n]) = 1)
            WITH path,
                 reduce(s = 0, r IN relationships(path) | s + r.km) as totalDistance
            ORDER BY totalDistance
            LIMIT 1
            RETURN [n IN nodes(path) | n.name] as route,
                   totalDistance
        """;

        return neo4jClient.query(query)
            .bind(startCity).to("startCity")
            .fetch()
            .one()
            .map(result -> {
                Map<String, Object> response = new HashMap<>();
                response.put("route", result.get("route"));
                response.put("totalDistance", result.get("totalDistance"));
                return response;
            })
            .orElse(Map.of("route", List.of(), "totalDistance", 0));
    }

    // QuickSort implementado en la consulta Cypher
    public List<Map<String, Object>> quickSortCitiesByDistance(String fromCity) {
        String query = """
            MATCH (start:City {name: $fromCity}), (other:City)
            WHERE start <> other
            OPTIONAL MATCH path = shortestPath((start)-[:ROAD*]-(other))
            WITH other.name as city,
                 CASE WHEN path IS NULL 
                      THEN -1 
                      ELSE reduce(s = 0, r IN relationships(path) | s + r.km)
                 END as distance
            ORDER BY distance
            RETURN city, distance
        """;

        return neo4jClient.query(query)
            .bind(fromCity).to("fromCity")
            .fetch()
            .all()
            .stream()
            .map(result -> Map.of(
                "city", result.get("city"),
                "distance", result.get("distance")))
            .toList();
    }

    // Programación Dinámica: Encontrar todos los caminos posibles entre dos ciudades
    public List<Map<String, Object>> findAllPaths(String startCity, String endCity, int maxLength) {
        String query = """
            MATCH path = (start:City {name: $startCity})-[:ROAD*..%d]-(end:City {name: $endCity})
            WHERE ALL(n IN nodes(path) WHERE size([x IN nodes(path) WHERE x = n]) = 1)
            WITH path, 
                 reduce(s = 0, r IN relationships(path) | s + r.km) as totalDistance
            RETURN [n IN nodes(path) | n.name] as route,
                   totalDistance
            ORDER BY totalDistance
        """.formatted(maxLength);

        return neo4jClient.query(query)
            .bind(startCity).to("startCity")
            .bind(endCity).to("endCity")
            .fetch()
            .all()
            .stream()
            .map(result -> Map.of(
                "route", result.get("route"),
                "totalDistance", result.get("totalDistance")))
            .toList();
    }

    // Backtracking: Encontrar todos los ciclos que pasan por una ciudad
    public List<Map<String, Object>> findAllCycles(String startCity, int maxLength) {
        String query = """
            MATCH path = (start:City {name: $startCity})-[:ROAD*..%d]->(start)
            WHERE size(nodes(path)) > 2
            WITH path,
                 reduce(s = 0, r IN relationships(path) | s + r.km) as totalDistance
            RETURN [n IN nodes(path) | n.name] as cycle,
                   totalDistance
            ORDER BY totalDistance
        """.formatted(maxLength);

        return neo4jClient.query(query)
            .bind(startCity).to("startCity")
            .fetch()
            .all()
            .stream()
            .map(result -> Map.of(
                "cycle", result.get("cycle"),
                "totalDistance", result.get("totalDistance")))
            .toList();
    }

    // Branch & Bound: Encontrar el camino óptimo visitando un conjunto específico de ciudades
    public Map<String, Object> tspBranchAndBound(List<String> citiesToVisit) {
        String citiesClause = String.join("','", citiesToVisit);
        String query = """
            MATCH (n:City)
            WHERE n.name IN ['%s']
            WITH collect(n) as nodes
            MATCH path = (n:City)-[:ROAD*]->(m:City)
            WHERE n IN nodes AND ALL(x IN nodes(path) WHERE x IN nodes)
            WITH path,
                 reduce(s = 0, r IN relationships(path) | s + r.km) as totalDistance
            ORDER BY totalDistance
            LIMIT 1
            RETURN [n IN nodes(path) | n.name] as route,
                   totalDistance
        """.formatted(citiesClause);

        return neo4jClient.query(query)
            .fetch()
            .one()
            .map(result -> {
                Map<String, Object> response = new HashMap<>();
                response.put("route", result.get("route"));
                response.put("totalDistance", result.get("totalDistance"));
                return response;
            })
            .orElse(Map.of("route", List.of(), "totalDistance", 0));
    }
}