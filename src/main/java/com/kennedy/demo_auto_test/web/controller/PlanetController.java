package com.kennedy.demo_auto_test.web.controller;

import com.kennedy.demo_auto_test.domain.Planet;
import com.kennedy.demo_auto_test.domain.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet){
        Planet planetCreated = planetService.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getById(@PathVariable Long id){
        return planetService.findById(id).map(
                    (p) -> ResponseEntity.ok(p))
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable String name){
        return planetService.findByName(name).map(
                (p) -> ResponseEntity.ok(p)
        ).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }
}
