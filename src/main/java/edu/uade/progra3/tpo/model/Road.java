package edu.uade.progra3.tpo.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Road {
    @Id @GeneratedValue
    private Long id;
    
    private final int km;
    
    @TargetNode
    private final City destination;
    
    public Road(int km, City destination) {
        this.km = km;
        this.destination = destination;
    }
    
    public int getKm() {
        return km;
    }
    
    public City getDestination() {
        return destination;
    }
    
    public Long getId() {
        return id;
    }
}