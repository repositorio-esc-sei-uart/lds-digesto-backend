/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dev.labintec.digesto_system.estadoU;

import dev.labintec.digesto_system.estadoU.EstadoU;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link EstadoU}.
 * 
 * Proporciona operaciones CRUD sobre la tabla de estados de usuario
 * y una consulta personalizada para buscar estados por nombre.
 * 
 */
public interface EstadoURepository extends JpaRepository<EstadoU, Integer> {
    
    /**
     * Busca un estado de usuario por su nombre.
     * 
     * @param nombre nombre del estado de usuario
     * @return {@link Optional} conteniendo el {@link EstadoU} si existe
     */
    Optional<EstadoU> findByNombre(String nombre);
}

