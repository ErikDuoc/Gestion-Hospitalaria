package com.equeirolo.gestion_hospitalaria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para solicitar el registro de un nuevo paciente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearPacienteRequest {
    private String rut;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String genero;
    private String email;
    private String previsionId;
    private String telefono;
    private String direccion;
}

