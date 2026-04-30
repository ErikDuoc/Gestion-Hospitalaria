package com.equeirolo.gestion_hospitalaria.service;

import com.equeirolo.gestion_hospitalaria.dto.ActualizarPacienteRequest;
import com.equeirolo.gestion_hospitalaria.dto.CrearPacienteRequest;
import com.equeirolo.gestion_hospitalaria.dto.PacienteResponse;
import com.equeirolo.gestion_hospitalaria.entity.Paciente;
import com.equeirolo.gestion_hospitalaria.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestionar la lógica de negocio de Pacientes.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    /**
     * Registra un nuevo paciente en el sistema.
     * @param request datos del nuevo paciente
     * @return respuesta con datos del paciente creado
     * @throws IllegalArgumentException si el RUT o email ya existen
     */
    public PacienteResponse registrarPaciente(CrearPacienteRequest request) {
        // Validar que no exista paciente con el mismo RUT
        if (pacienteRepository.findByRut(request.getRut()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con el RUT: " + request.getRut());
        }

        // Validar que no exista paciente con el mismo email
        if (pacienteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con el email: " + request.getEmail());
        }

        // Crear y guardar el paciente
        Paciente paciente = Paciente.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .fechaNacimiento(request.getFechaNacimiento())
                .genero(request.getGenero())
                .email(request.getEmail())
                .previsionId(request.getPrevisionId())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .activo(true)
                .build();

        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        return convertirAResponse(pacienteGuardado);
    }

    /**
     * Obtiene el perfil completo de un paciente por su ID.
     * @param id el ID del paciente
     * @return respuesta con datos del paciente
     * @throws RuntimeException si el paciente no existe
     */
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return convertirAResponse(paciente);
    }

    /**
     * Actualiza los datos de contacto de un paciente.
     * @param id el ID del paciente
     * @param request datos a actualizar
     * @return respuesta con datos del paciente actualizado
     * @throws RuntimeException si el paciente no existe
     * @throws IllegalArgumentException si el email ya está en uso por otro paciente
     */
    public PacienteResponse actualizarPaciente(Long id, ActualizarPacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        // Validar que el email no esté en uso por otro paciente
        if (request.getEmail() != null && !request.getEmail().equals(paciente.getEmail())) {
            if (pacienteRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("El email ya está en uso por otro paciente: " + request.getEmail());
            }
            paciente.setEmail(request.getEmail());
        }

        // Actualizar teléfono si se proporciona
        if (request.getTelefono() != null) {
            paciente.setTelefono(request.getTelefono());
        }

        // Actualizar dirección si se proporciona
        if (request.getDireccion() != null) {
            paciente.setDireccion(request.getDireccion());
        }

        Paciente pacienteActualizado = pacienteRepository.save(paciente);
        return convertirAResponse(pacienteActualizado);
    }

    /**
     * Convierte una entidad Paciente a PacienteResponse.
     */
    private PacienteResponse convertirAResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .rut(paciente.getRut())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .genero(paciente.getGenero())
                .email(paciente.getEmail())
                .previsionId(paciente.getPrevisionId())
                .telefono(paciente.getTelefono())
                .direccion(paciente.getDireccion())
                .activo(paciente.getActivo())
                .build();
    }
}

