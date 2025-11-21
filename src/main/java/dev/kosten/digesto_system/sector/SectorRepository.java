/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dev.kosten.digesto_system.sector;

import dev.kosten.digesto_system.sector.Sector;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Sector}.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de sectores.
 * Extiende de {@link JpaRepository} para heredar funcionalidades básicas de persistencia.
 * 
 */
public interface SectorRepository extends JpaRepository<Sector, Integer> {
    
    /**
     * Busca un sector por su nombre.
     * 
     * @param nombre nombre del sector a buscar
     * @return un {@link Optional} que puede contener el sector encontrado
     */
    Optional<Sector> findByNombre(String nombre);
}

