package com.equeirolo.gestion_hospitalaria.controller;

import com.equeirolo.gestion_hospitalaria.dto.ActualizarPacienteRequest;
import com.equeirolo.gestion_hospitalaria.dto.CrearPacienteRequest;
import com.equeirolo.gestion_hospitalaria.dto.PacienteResponse;
import com.equeirolo.gestion_hospitalaria.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar operaciones de Pacientes.
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    /**
     * POST /api/patients: Registra un nuevo paciente.
     * @param request datos del nuevo paciente
     * @return respuesta con código 201 (Created) y datos del paciente creado
     */
    @PostMapping
    public ResponseEntity<PacienteResponse> registrarPaciente(@Valid @RequestBody CrearPacienteRequest request) {
        PacienteResponse response = pacienteService.registrarPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/patients/{id}: Obtiene el perfil completo de un paciente.
     * @param id el ID del paciente
     * @return respuesta con código 200 (OK) y datos del paciente
     */
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> obtenerPaciente(@PathVariable Long id) {
        PacienteResponse response = pacienteService.obtenerPaciente(id);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/patients/{id}: Actualiza datos de contacto de un paciente.
     * @param id el ID del paciente
     * @param request datos a actualizar
     * @return respuesta con código 200 (OK) y datos del paciente actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> actualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPacienteRequest request) {
        PacienteResponse response = pacienteService.actualizarPaciente(id, request);
        return ResponseEntity.ok(response);
    }
}

