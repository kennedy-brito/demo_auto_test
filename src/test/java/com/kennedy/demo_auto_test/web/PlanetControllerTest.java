package com.kennedy.demo_auto_test.web;

import static com.kennedy.demo_auto_test.common.PlanetConstants.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennedy.demo_auto_test.domain.Planet;
import com.kennedy.demo_auto_test.domain.PlanetService;
import com.kennedy.demo_auto_test.web.controller.PlanetController;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
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

    @Test
    public void listPlanets_WithFilter_ReturnPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

        mockMvc.perform(
                get("/planets")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));

        mockMvc.perform(
                        get("/planets?" + String.format("climate=%s&terrain=%s", TATOOINE.getClimate(), TATOOINE.getTerrain()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATOOINE));

    }

    @Test
    public void listPlanets_WithFilter_ReturnNoPlanets() throws Exception {
        when(planetService.list(any(), any())).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(
                        get("/planets")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$",empty()));


    }

    @Test
    public void deletePlanet_ByExistingId_ReturnNoContent() throws Exception{
        mockMvc.perform(
                delete("/planets/1")
        ).andExpect(status().isNoContent());
    }

    @Test
    public void deletePlanet_ByNonExistingId_ReturnNotFound() throws Exception{
        doThrow(EmptyResultDataAccessException.class).when(planetService).remove(anyLong());

        mockMvc.perform(
                delete("/planets/1")
        ).andExpect(status().isNotFound());
    }
}
