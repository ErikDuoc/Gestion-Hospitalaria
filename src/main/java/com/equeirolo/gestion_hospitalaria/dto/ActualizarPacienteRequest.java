package com.equeirolo.gestion_hospitalaria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar datos de contacto de un paciente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarPacienteRequest {
    private String email;
    private String telefono;
    private String direccion;
}

