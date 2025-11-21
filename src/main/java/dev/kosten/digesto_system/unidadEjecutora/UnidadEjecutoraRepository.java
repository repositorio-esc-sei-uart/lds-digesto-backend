/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dev.kosten.digesto_system.unidadEjecutora;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link UnidadEjecutore}.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de unidadEjecutorees.
 * Extiende de {@link JpaRepository} para heredar funcionalidades básicas de persistencia.
 * @author Matias
 */
public interface UnidadEjecutoraRepository extends JpaRepository<UnidadEjecutora, Integer> {
    
    /**
     * Busca un unidadEjecutore por su nombre.
     * 
     * @param nombre nombre de la unidadEjecutore a buscar
     * @return un {@link Optional} que puede contener el unidadEjecutore encontrado
     */
    Optional<UnidadEjecutora> findByNombre(String nombre);
}

