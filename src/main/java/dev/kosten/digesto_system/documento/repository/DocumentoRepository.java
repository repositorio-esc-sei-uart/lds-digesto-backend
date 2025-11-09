package dev.kosten.digesto_system.documento.repository;

import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

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

    /**
     * Busca un documento cuyo título coincida exactamente con el texto proporcionado.
     * @param titulo El título exacto del documento a buscar.
     * @return Un {@link Optional} que contiene el documento si se encuentra,
     *         o vacío si no existe ningún documento con ese título.
     */

    Optional<Documento> findByTitulo(String titulo);

    /**
     * Verifica si existe al menos un documento que utiliza el TipoDocumento especificado.
     * @param tipoExistente
     * @return true si al menos un documento lo usa, false de lo contrario.
     */
    public boolean existsByTipoDocumento(TipoDocumento tipoExistente);

    /**
     * Busca documentos por ID de tipo de documento con paginación.
     * Spring Data JPA crea automáticamente la query basándose en el nombre del método.
     * @param idTipoDocumento ID del tipo de documento
     * @param pageable Configuración de paginación
     * @return Page de documentos filtrados
     */
    Page<Documento> findByTipoDocumento_IdTipoDocumento(Integer idTipoDocumento, Pageable pageable);
}
