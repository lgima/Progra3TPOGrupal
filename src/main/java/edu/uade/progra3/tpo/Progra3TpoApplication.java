package edu.uade.progra3.tpo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Progra3TpoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Progra3TpoApplication.class, args);
    }

	@Bean
	CommandLineRunner testConnection(org.springframework.data.neo4j.core.Neo4jClient client) {
		return args -> {
			try {
				var result = client.query("RETURN 'Conexión OK con Neo4j AuraDB' AS mensaje")
						.fetchAs(String.class)
						.one()
						.orElse("Sin conexión");
				System.out.println("🟢 Resultado: " + result);
			} catch (Exception e) {
				System.out.println("🔴 Error probando conexión: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}
}


