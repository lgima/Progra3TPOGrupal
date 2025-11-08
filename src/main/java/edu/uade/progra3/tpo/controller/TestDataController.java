package edu.uade.progra3.tpo.controller;

import edu.uade.progra3.tpo.model.City;
import edu.uade.progra3.tpo.model.Road;
import edu.uade.progra3.tpo.repository.CityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/test")
public class TestDataController {
    private final CityRepository cityRepository;

    public TestDataController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @PostMapping("/init")
    public String initializeTestData() {
        // Crear ciudades
        City buenosAires = new City("Buenos Aires");
        City cordoba = new City("Córdoba");
        City rosario = new City("Rosario");
        City mendoza = new City("Mendoza");
        City marDelPlata = new City("Mar del Plata");

        // Crear conexiones
        buenosAires.getRoads().add(new Road(400, cordoba));
        buenosAires.getRoads().add(new Road(300, rosario));
        buenosAires.getRoads().add(new Road(400, marDelPlata));
        cordoba.getRoads().add(new Road(400, mendoza));
        rosario.getRoads().add(new Road(400, cordoba));
        
        // Guardar ciudades
        cityRepository.save(buenosAires);
        cityRepository.save(cordoba);
        cityRepository.save(rosario);
        cityRepository.save(mendoza);
        cityRepository.save(marDelPlata);

        return "Datos de prueba inicializados";
    }

    @GetMapping("/cities")
    public List<String> getAllCities() {
        return cityRepository.findAllCityNames();
    }

    @GetMapping("/roads")
    public List<Map<String, Object>> getAllRoads() {
        return cityRepository.findAllRoads();
    }
}
