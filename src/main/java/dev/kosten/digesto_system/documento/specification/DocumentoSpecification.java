package dev.kosten.digesto_system.documento.specification;

import dev.kosten.digesto_system.documento.entity.Documento;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
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
            // 1. Divide la búsqueda (ej: "resolución 101") en palabras: ["resolución", "101"]
            String[] words = searchTerm.toLowerCase().split("\\s+");

            // 2. Lista para guardar las condiciones AND (una por cada palabra)
            List<Predicate> mainAndPredicates = new ArrayList<>();

            // 3. Define los campos donde se buscará
            var tituloPath = cb.lower(root.get("titulo"));
            var resumenPath = cb.lower(root.get("resumen"));
            var numDocPath = cb.lower(root.get("numDocumento"));
            var tipoDocPath = cb.lower(root.get("tipoDocumento").get("nombre"));

            // 4. Itera sobre cada palabra ("resolución", luego "101")
            for (String word : words) {
                if (word.isEmpty()) continue;
                String pattern = "%" + word + "%";

                // 5. Crea un grupo OR para ESA palabra en CUALQUIER campo
                // (ej: titulo LIKE '%resolución%' OR resumen LIKE '%resolución%' OR ...)
                Predicate wordInAnyField = cb.or(
                    cb.like(tituloPath, pattern),
                    cb.like(resumenPath, pattern),
                    cb.like(numDocPath, pattern),
                    cb.like(tipoDocPath, pattern)
                );

                // 6. Agrega ese grupo a la lista principal
                mainAndPredicates.add(wordInAnyField);
            }

            // 7. Combina todos los grupos con AND
            // (ej: (palabra1 en campos) AND (palabra2 en campos))
            return cb.and(mainAndPredicates.toArray(new Predicate[0]));
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
