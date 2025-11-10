package dev.kosten.digesto_system.documento.specification;

import dev.kosten.digesto_system.documento.entity.Documento;
import org.springframework.data.jpa.domain.Specification;

/**
 * Especificaciones reutilizables para consultas de Documento.
 * Permite construcción dinámica de queries con filtros opcionales.
 */
public class DocumentoSpecification {

    /**
     * Busca el término en título, resumen y número de documento (case-insensitive).
     * @param searchTerm
     * @return 
     */
    public static Specification<Documento> conTerminoDeBusqueda(String searchTerm) {
        return (root, query, cb) -> {
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            
            return cb.or(
                cb.like(cb.lower(root.get("titulo")), pattern),
                cb.like(cb.lower(root.get("resumen")), pattern),
                cb.like(cb.lower(root.get("numDocumento")), pattern)
            );
        };
    }

    /**
     * Filtra por ID de tipo de documento.
     * @param idTipoDocumento
     * @return 
     */
    public static Specification<Documento> conTipoDocumento(Integer idTipoDocumento) {
        return (root, query, cb) -> 
            cb.equal(root.get("tipoDocumento").get("idTipoDocumento"), idTipoDocumento);
    }

    /**
     * Busca en más campos incluyendo sector y palabras clave.
     * @param searchTerm
     * @return 
     */
    public static Specification<Documento> busquedaAvanzada(String searchTerm) {
        return (root, query, cb) -> {
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            
            return cb.or(
                cb.like(cb.lower(root.get("titulo")), pattern),
                cb.like(cb.lower(root.get("resumen")), pattern),
                cb.like(cb.lower(root.get("numDocumento")), pattern),
                cb.like(cb.lower(root.get("sector").get("nombre")), pattern),
                cb.like(cb.lower(root.get("tipoDocumento").get("nombre")), pattern)
            );
        };
    }
}
