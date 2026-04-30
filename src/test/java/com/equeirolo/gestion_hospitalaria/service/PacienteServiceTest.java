package com.equeirolo.gestion_hospitalaria.service;

import com.equeirolo.gestion_hospitalaria.dto.ActualizarPacienteRequest;
import com.equeirolo.gestion_hospitalaria.dto.CrearPacienteRequest;
import com.equeirolo.gestion_hospitalaria.dto.PacienteResponse;
import com.equeirolo.gestion_hospitalaria.entity.Paciente;
import com.equeirolo.gestion_hospitalaria.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests para PacienteService.
 */
@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private CrearPacienteRequest crearPacienteRequest;
    private Paciente paciente;

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

        paciente = Paciente.builder()
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
    void testRegistrarPaciente_Exitoso() {
        // Arrange
        when(pacienteRepository.findByRut(crearPacienteRequest.getRut())).thenReturn(Optional.empty());
        when(pacienteRepository.findByEmail(crearPacienteRequest.getEmail())).thenReturn(Optional.empty());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        PacienteResponse response = pacienteService.registrarPaciente(crearPacienteRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@example.com", response.getEmail());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void testRegistrarPaciente_RutYaExiste() {
        // Arrange
        when(pacienteRepository.findByRut(crearPacienteRequest.getRut())).thenReturn(Optional.of(paciente));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.registrarPaciente(crearPacienteRequest);
        });
    }

    @Test
    void testRegistrarPaciente_EmailYaExiste() {
        // Arrange
        when(pacienteRepository.findByRut(crearPacienteRequest.getRut())).thenReturn(Optional.empty());
        when(pacienteRepository.findByEmail(crearPacienteRequest.getEmail())).thenReturn(Optional.of(paciente));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.registrarPaciente(crearPacienteRequest);
        });
    }

    @Test
    void testObtenerPaciente_Exitoso() {
        // Arrange
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        // Act
        PacienteResponse response = pacienteService.obtenerPaciente(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
        verify(pacienteRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPaciente_NoEncontrado() {
        // Arrange
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pacienteService.obtenerPaciente(999L);
        });
    }

    @Test
    void testActualizarPaciente_Exitoso() {
        // Arrange
        ActualizarPacienteRequest actualizarRequest = ActualizarPacienteRequest.builder()
                .email("newemail@example.com")
                .telefono("987654321")
                .direccion("Nueva Calle 456")
                .build();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.findByEmail("newemail@example.com")).thenReturn(Optional.empty());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        PacienteResponse response = pacienteService.actualizarPaciente(1L, actualizarRequest);

        // Assert
        assertNotNull(response);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }
}

