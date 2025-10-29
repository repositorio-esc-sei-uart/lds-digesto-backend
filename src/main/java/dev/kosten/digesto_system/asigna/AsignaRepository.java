/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.asigna;

import dev.kosten.digesto_system.asigna.Asigna;
import dev.kosten.digesto_system.asigna.AsignaId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Asigna}.
 * 
 * Proporciona operaciones CRUD sobre la tabla de asignaciones
 * y consultas personalizadas para buscar asignaciones por rol o permiso.
 * 
 * Utiliza {@link AsignaId} como clave primaria compuesta.
 * 
 */
public interface AsignaRepository extends JpaRepository<Asigna, AsignaId> {
    
    /**
     * Obtiene todas las asignaciones de un rol específico.
     * 
     * @param idRol identificador del rol
     * @return lista de {@link Asigna} correspondientes al rol
     */
    List<Asigna> findByRolIdRol(Integer idRol);
    
    /**
     * Obtiene todas las asignaciones de un permiso específico.
     * 
     * @param idPermiso identificador del permiso
     * @return lista de {@link Asigna} correspondientes al permiso
     */
    List<Asigna> findByPermisoIdPermiso(Integer idPermiso);
}


