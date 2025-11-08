package edu.uade.progra3.tpo.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
public class City {
    @Id
    private String name;
    
    @Relationship(type = "ROAD", direction = Relationship.Direction.OUTGOING)
    private List<Road> roads = new ArrayList<>();
    
    public City() {}
    
    public City(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Road> getRoads() {
        return roads;
    }
}