package dev.kosten.digesto_system.archivo.repository;

import dev.kosten.digesto_system.archivo.entity.Archivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Archivo.
 * @author micael
 * @author Quique
 */

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Integer>{
    /**
     * Busca todos los archivos asociados a un ID de Documento específico.
     * Spring Data JPA crea la consulta automáticamente basado en el nombre del método.
     * @param idDocumento El ID del documento padre.
     * @return Una lista de entidades Archivo.
     */
    List<Archivo> findByDocumentoIdDocumento(Integer idDocumento);
}
