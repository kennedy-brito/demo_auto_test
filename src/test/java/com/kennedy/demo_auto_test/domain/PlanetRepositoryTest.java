package com.kennedy.demo_auto_test.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static com.kennedy.demo_auto_test.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach(){
        PLANET.setId(null);
    }

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

    @Test
    public void getPlanet_ByExistingId_ReturnsEmpty(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = planetRepository.findById(planet.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(planet);

    }

    @Test
    public void getPlanet_ByNonExistingId_ReturnsEmpty(){

        Optional<Planet> sut = planetRepository.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsEmpty(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = planetRepository.findByName(planet.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(planet);

    }

    @Test
    public void getPlanet_ByNonExistingName_ReturnsEmpty(){

        Optional<Planet> sut = planetRepository.findByName("1L");

        assertThat(sut).isEmpty();
    }

    @Test
    @Sql(scripts = "/import_planets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete_planets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void listPlanets_ReturnsFilteredPlanets(){

        Planet planet = new Planet(null, null, "temperate", null);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();;

        List<Planet> sut = planetRepository.findAll(Example.of(planet, exampleMatcher));

        assertThat(sut).isNotNull();
        assertThat(sut).hasSize(1);

        planet = new Planet(null, null, null, "desert");

        exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();;

        sut = planetRepository.findAll(Example.of(planet, exampleMatcher));

        assertThat(sut).isNotNull();
        assertThat(sut).hasSize(1);

        planet = new Planet(null, null, "arid", "desert");

        exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();;

        sut = planetRepository.findAll(Example.of(planet, exampleMatcher));

        assertThat(sut).isNotNull();
        assertThat(sut).hasSize(1);

    }

    @Test
    public void listPlanets_ReturnsNoPlanets(){

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();;

        List<Planet> sut = planetRepository.findAll(Example.of(PLANET, exampleMatcher));

        assertThat(sut).isNotNull();
        assertThat(sut).isEmpty();
    }

    @Sql(scripts = "/import_planets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete_planets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void deletePlanet_WithExistingId_DoesntThrowException(){

        Planet planet = testEntityManager.find(Planet.class, 1L);

        assertThat(planet).isNotNull();

        planetRepository.deleteById(1L);

        planet = testEntityManager.find(Planet.class, 1L);

        assertThat(planet).isNull();
    }

    @Test
    @Sql(scripts = "/delete_planets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deletePlanet_WithNonExistingId_ThrowException(){

        assertThatNoException().isThrownBy(
                () -> planetRepository.deleteById(1L)
        );


    }
}
