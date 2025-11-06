package edu.uade.progra3.tpo;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  private final Neo4jClient client;
  public TestController(Neo4jClient client) { this.client = client; }

  @GetMapping("/test")
  public String probar() {
    return client.query("RETURN 'Conexión OK desde Spring Boot' AS mensaje")
                 .fetchAs(String.class).one().orElse("Sin conexión");
  }
}
