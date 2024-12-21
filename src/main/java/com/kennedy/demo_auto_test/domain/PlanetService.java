package com.kennedy.demo_auto_test.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    public Planet create(Planet planet){
        System.out.println(planet);
        return planetRepository.save(planet);
    }
}
