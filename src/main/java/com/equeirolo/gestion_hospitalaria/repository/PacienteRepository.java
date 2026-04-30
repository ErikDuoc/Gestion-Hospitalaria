package com.equeirolo.gestion_hospitalaria.repository;

import com.equeirolo.gestion_hospitalaria.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceder a datos de Paciente en la base de datos.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca un paciente por su RUT.
     * @param rut el RUT del paciente
     * @return Optional con el paciente si existe
     */
    Optional<Paciente> findByRut(String rut);

    /**
     * Busca un paciente por su email.
     * @param email el email del paciente
     * @return Optional con el paciente si existe
     */
    Optional<Paciente> findByEmail(String email);
}

