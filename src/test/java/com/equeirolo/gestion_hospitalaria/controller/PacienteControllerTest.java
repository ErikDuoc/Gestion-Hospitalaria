package com.equeirolo.gestion_hospitalaria.controller;

import com.equeirolo.gestion_hospitalaria.dto.CrearPacienteRequest;
import com.equeirolo.gestion_hospitalaria.dto.PacienteResponse;
import com.equeirolo.gestion_hospitalaria.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para PacienteController.
 */
@WebMvcTest(PacienteController.class)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private CrearPacienteRequest crearPacienteRequest;
    private PacienteResponse pacienteResponse;

    @BeforeEach
    void setUp() {
        crearPacienteRequest = CrearPacienteRequest.builder()
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Pérez")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .genero("M")
                .email("juan@example.com")
                .previsionId("FONASA")
                .telefono("123456789")
                .direccion("Calle 123")
                .build();

        pacienteResponse = PacienteResponse.builder()
                .id(1L)
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Pérez")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .genero("M")
                .email("juan@example.com")
                .previsionId("FONASA")
                .telefono("123456789")
                .direccion("Calle 123")
                .activo(true)
                .build();
    }

    @Test
    void testRegistrarPaciente() throws Exception {
        // Arrange
        when(pacienteService.registrarPaciente(any(CrearPacienteRequest.class)))
                .thenReturn(pacienteResponse);

        // Act & Assert
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearPacienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void testObtenerPaciente() throws Exception {
        // Arrange
        when(pacienteService.obtenerPaciente(1L))
                .thenReturn(pacienteResponse);

        // Act & Assert
        mockMvc.perform(get("/api/patients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void testActualizarPaciente() throws Exception {
        // Arrange
        CrearPacienteRequest actualizarRequest = CrearPacienteRequest.builder()
                .email("newemail@example.com")
                .build();

        when(pacienteService.actualizarPaciente(eq(1L), any()))
                .thenReturn(pacienteResponse);

        // Act & Assert
        mockMvc.perform(put("/api/patients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizarRequest)))
                .andExpect(status().isOk());
    }
}

