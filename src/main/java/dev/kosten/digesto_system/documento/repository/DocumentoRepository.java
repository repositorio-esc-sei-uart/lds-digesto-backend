package dev.kosten.digesto_system.documento.repository;

import dev.kosten.digesto_system.documento.entity.Documento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Documento}.
 * Proporciona métodos CRUD estándar y consultas personalizadas
 * para buscar documentos por título y número de documento
 * @author micael
 */

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Integer>{

    /**
     * Busca documentos cuyo título contenga el fragmento de texto proporcionado,
     * ignorando mayúsculas y minúsculas (búsqueda LIKE %titulo%).
     * @param titulo El fragmento de texto a buscar en el título.
     * @return Una lista de {@link Documento} que coinciden.
     */
    List<Documento> findByTituloContainingIgnoreCase(String titulo);

    /**
     * Busca un documento por su número de documento único (ej. "RES-001/25").
     * @param numDocumento El número de documento exacto a buscar.
     * @return Un Optional conteniendo el documento si se encuentra,
     * o un Optional vacío si no existe.
     */
    Optional<Documento> findByNumDocumento(String numDocumento);
}
