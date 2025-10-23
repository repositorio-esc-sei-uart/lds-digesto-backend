/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.archivo.repository;

import dev.labintec.digesto_system.archivo.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author micae
 */

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Integer>{
    // Podríamos necesitar buscar archivos por documento
    // List<Archivo> findByDocumentoIdDocumento(Integer idDocumento);
}
