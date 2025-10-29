/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.documento.repository;

import dev.kosten.digesto_system.documento.entity.Documento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author micae
 */

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Integer>{
    
    
    List<Documento> findByTituloContainingIgnoreCase(String titulo);
    Optional<Documento> findByNumDocumento(String numDocumento);
}
