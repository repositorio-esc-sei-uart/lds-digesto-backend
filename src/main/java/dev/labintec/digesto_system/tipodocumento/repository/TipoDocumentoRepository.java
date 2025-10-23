/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.tipodocumento.repository;

import dev.labintec.digesto_system.tipodocumento.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author micae
 */
@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer>{
    // JpaRepository ya nos da:
    // - save() (para crear y actualizar)
    // - findById()
    // - findAll()
    // - deleteById()
    
}
