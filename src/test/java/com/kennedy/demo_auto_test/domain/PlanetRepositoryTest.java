package com.kennedy.demo_auto_test.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static com.kennedy.demo_auto_test.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet(){

        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());

    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException(){
        assertThatThrownBy(() -> planetRepository.save(EMPTY_PLANET)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_withExistingName_ThrowsException(){

        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);
        assertThatThrownBy( ()-> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }
}
