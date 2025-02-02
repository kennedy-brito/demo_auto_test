package com.kennedy.demo_auto_test.domain;

import static com.kennedy.demo_auto_test.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {

//    @Autowired
    @InjectMocks
    private PlanetService planetService;

//    @MockitoBean
    @Mock
    private PlanetRepository planetRepository;

    //Test Name Pattern: operation_stateOrParams_Return
    @Test
    public void createPlanet_withValidData_ReturnsPlanetStatus201(){

        // * AAA PRINCIPLE
        // * ARRANGE

        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        //System under test
        // * ACT
        Planet sut = planetService.create(PLANET);

        // * ASSERT
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException(){

        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy( () -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnPlanet(){
        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetRepository.findById(1L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByNonExistingId_ReturnEmpty(){
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnPlanet(){
        when(planetRepository.findByName("name")).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.findByName("name");

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByNonExistingName_ReturnEmpty(){
        when(planetRepository.findByName(anyString())).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.findByName("name");

        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets(){
        List<Planet> planets = new ArrayList<>() {
            {

                add(PLANET);
            }
        };

        Example<Planet> query = QueryBuilder.makeQuery(new Planet(null, null, PLANET.getClimate(), PLANET.getTerrain()));

        when(planetRepository.findAll(query)).thenReturn(planets);

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets(){
        when(planetRepository.findAll(any(Example.class))).thenReturn(Collections.EMPTY_LIST);

        List<Planet> sut = planetService.list(PLANET.getTerrain(),PLANET.getClimate());

        assertThat(sut).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_doesNotThrowAnyException(){
        assertThatCode( () -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithExistingId_ThrowsException(){
        doThrow(RuntimeException.class).when(planetRepository).deleteById(anyLong());

        assertThatThrownBy(() -> planetService.remove(1L)).isInstanceOf(RuntimeException.class);
    }
}
