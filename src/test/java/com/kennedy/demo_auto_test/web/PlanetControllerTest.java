package com.kennedy.demo_auto_test.web;

import static com.kennedy.demo_auto_test.common.PlanetConstants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennedy.demo_auto_test.domain.Planet;
import com.kennedy.demo_auto_test.domain.PlanetService;
import com.kennedy.demo_auto_test.web.controller.PlanetController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;


@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanetService planetService;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {

        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(
                post("/planets")
                        .content(
                                objectMapper.writeValueAsString(PLANET)
                        ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsStatusBadRequest() throws Exception {


        mockMvc.perform(
                        post("/planets")
                                .content(
                                        objectMapper.writeValueAsString(EMPTY_PLANET)
                                ).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity());

        mockMvc.perform(
                post("/planets")
                        .content(
                                objectMapper.writeValueAsString(INVALID_PLANET)
                        ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(
                post("/planets")
                        .content(
                                objectMapper.writeValueAsString(PLANET)
                        ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanetStatus200() throws Exception {

        when(planetService.findById(anyLong())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(
                get("/planets/1")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));

    }

    @Test
    public void getPlanet_ByNonExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(
                        get("/planets/1")
                ).andExpect(status().isNotFound());

    }
    @Test
    public void getPlanet_ByExistingName_ReturnsPlanetStatus200() throws Exception {

        when(planetService.findByName(anyString())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(
                        get("/planets/name/{name}", PLANET.getName())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));

    }

    @Test
    public void getPlanet_ByNonExistingName_ReturnsNotFound() throws Exception {
        mockMvc.perform(
                get("/planets/name/{name}", "any")
        ).andExpect(status().isNotFound());

    }
}
