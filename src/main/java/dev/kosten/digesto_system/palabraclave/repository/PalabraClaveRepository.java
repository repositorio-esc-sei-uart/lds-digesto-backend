/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dev.kosten.digesto_system.palabraclave.repository;

import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author micae
 */
public interface PalabraClaveRepository extends JpaRepository<PalabraClave, Integer>{
    // Método para validar si ya existe una etiqueta con el mismo nombre
    Optional<PalabraClave> findByNombre(String nombre);
}
