package com.kennedy.demo_auto_test;


import static com.kennedy.demo_auto_test.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kennedy.demo_auto_test.domain.Planet;
import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("it")
@Sql(scripts = "/remove_planets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/import_planets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanetIT {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void createPlanet_ReturnCreated(){
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void getPlanet_ReturnsPlanet(){

        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanetByName_ReturnsPlanet(){

        ResponseEntity<Planet> sut = restTemplate.getForEntity(
                "/planets/name/" + TATOOINE.getName() ,
                Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnAllPlanets(){

        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);

        assertThat(sut).isNotNull();
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(3);

    }

    @Test
    public void listPlanets_ByClimate_ReturnPlanets(){

        ResponseEntity<Planet[]> sut = restTemplate.getForEntity(
                "/planets?climate=" + TATOOINE.getClimate(),
                Planet[].class
        );

        assertThat(sut).isNotNull();
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);

    }

    @Test
    public void listPlanets_ByTerrain_ReturnPlanets(){

        ResponseEntity<Planet[]> sut = restTemplate.getForEntity(
                "/planets?terrain=" + TATOOINE.getTerrain(),
                Planet[].class
        );

        assertThat(sut).isNotNull();
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);

    }
    @Test
    public void listPlanets_ByClimateAndTerrain_ReturnPlanets(){

        ResponseEntity<Planet[]> sut = restTemplate.getForEntity(
                String.format(
                        "/planets?climate=%s&terrain=%s",TATOOINE.getClimate(), TATOOINE.getTerrain()
                ),Planet[].class
        );

        assertThat(sut).isNotNull();
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);

    }

    @Test
    public void removePlanet_ReturnsNoContent(){

        ResponseEntity<Void> sut = restTemplate.exchange(
                "/planets/1",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
