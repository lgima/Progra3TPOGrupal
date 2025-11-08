package edu.uade.progra3.tpo.repository;

import edu.uade.progra3.tpo.model.City;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

public interface CityRepository extends Neo4jRepository<City, String> {
    @Query("MATCH (c:City)-[r:ROAD]-(d:City) RETURN c.name as source, d.name as target, r.km as distance")
    List<Map<String, Object>> findAllRoads();

    @Query("MATCH (c:City) RETURN c.name as name")
    List<String> findAllCityNames();
}
