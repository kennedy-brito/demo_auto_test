package com.kennedy.demo_auto_test.domain;

import static com.kennedy.demo_auto_test.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
}
