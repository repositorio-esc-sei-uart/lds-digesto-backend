/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dev.kosten.digesto_system.permiso;

import dev.kosten.digesto_system.permiso.Permiso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Permiso}.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de permisos.
 * Extiende de {@link JpaRepository} para heredar funcionalidades básicas de persistencia.
 * 
 * Permite buscar permisos por su nombre.
 * 
 */
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    
    /**
     * Busca un permiso por su nombre.
     * 
     * @param nombre nombre del permiso a buscar
     * @return un {@link Optional} que puede contener el permiso encontrado
     */
    Optional<Permiso> findByNombre(String nombre);
}

