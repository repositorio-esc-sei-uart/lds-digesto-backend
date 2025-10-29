/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.rol;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link Rol}.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de roles.
 * Extiende de {@link JpaRepository} para heredar funcionalidades básicas de persistencia.
 * 
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre.
     * 
     * @param nombre nombre del rol a buscar
     * @return un {@link Optional} que puede contener el rol encontrado
     */
    Optional<Rol> findByNombre(String nombre);
}

