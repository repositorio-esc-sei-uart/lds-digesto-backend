/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dev.kosten.digesto_system.cargo;

import dev.kosten.digesto_system.cargo.Cargo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Cargo}.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de cargos.
 * Extiende de {@link JpaRepository} para heredar funcionalidades básicas de persistencia.
 * 
 */
public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    /**
     * Busca un cargo por su nombre.
     * 
     * @param nombre nombre del cargo a buscar
     * @return un {@link Optional} que puede contener el cargo encontrado
     */
    Optional<Cargo> findByNombre(String nombre);
}

