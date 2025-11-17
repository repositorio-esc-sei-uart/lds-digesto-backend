package dev.kosten.digesto_system.documento.specification;

import dev.kosten.digesto_system.documento.entity.Documento;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import java.util.Date;
import org.springframework.data.jpa.domain.Specification;

/**
 * Especificaciones reutilizables para consultas de Documento.
 * Permite construcción dinámica de queries con filtros opcionales.
 */
public class DocumentoSpecification {

    /**
     * Busca el término en título, resumen y número de documento (case-insensitive).
     * @param searchTerm el término de búsqueda (puede contener múltiples palabras separadas por espacios)
     * @return una especificación JPA para filtrar documentos que contengan todas las palabras del término
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
     * (Avanzado) Busca palabras con lógica AND solo en el TÍTULO.
     * @param tituloTerm el término a buscar en el título (puede contener múltiples palabras)
     * @return una especificación JPA para filtrar por título
     */
    public static Specification<Documento> conTitulo(String tituloTerm) {
        return (root, query, cb) -> {
            String[] words = tituloTerm.toLowerCase().split("\\s+");
            Predicate[] predicates = new Predicate[words.length];
            for (int i = 0; i < words.length; i++) {
                if (!words[i].isEmpty()) {
                    predicates[i] = cb.like(cb.lower(root.get("titulo")), "%" + words[i] + "%");
                }
            }
            // Devuelve un AND de todas las palabras buscadas en el título
            return cb.and(predicates);
        };
    }

    /**
     * (Avanzado) Busca una coincidencia LIKE simple solo en el NÚMERO de documento.
     * @param numDocumento el número o fragmento a buscar
     * @return una especificación JPA para filtrar por número de documento
     */
    public static Specification<Documento> conNumero(String numDocumento) {
        return (root, query, cb) -> 
            cb.like(cb.lower(root.get("numDocumento")), "%" + numDocumento.toLowerCase() + "%");
    }

    /**
     * (Avanzado y Simple) Filtra por ID de TipoDocumento.
     * @param idTipoDocumento el identificador del tipo de documento
     * @return una especificación JPA para filtrar por tipo
     */
    public static Specification<Documento> conTipoDocumento(Integer idTipoDocumento) {
        return (root, query, cb) -> 
            cb.equal(root.get("tipoDocumento").get("idTipoDocumento"), idTipoDocumento);
    }

    /**
     * (Avanzado) Filtra por ID de Sector.
     * @param idSector el identificador del sector
     * @return una especificación JPA para filtrar por sector
     */
    public static Specification<Documento> conSector(Integer idSector) {
        return (root, query, cb) -> 
            cb.equal(root.get("sector").get("idSector"), idSector);
    }

    /**
     * (Avanzado) Filtra por ID de Estado.
     * @param idEstado el identificador del estado
     * @return una especificación JPA para filtrar por estado
     */
    public static Specification<Documento> conEstado(Integer idEstado) {
        return (root, query, cb) -> 
            cb.equal(root.get("estado").get("idEstado"), idEstado);
    }

    /**
     * (Avanzado) Filtra por un rango de fechas de creación.
     * Si solo se proporciona fechaDesde, busca documentos de ESA fecha exacta.
     * Si solo se proporciona fechaHasta, busca hasta esa fecha.
     * Si se proporcionan ambas, busca en el rango inclusivo.
     * @param fechaDesde Fecha de inicio (inclusive) o fecha exacta si fechaHasta es null
     * @param fechaHasta Fecha de fin (inclusive) o null
     * @return Specification para filtrar por fechas
     */
    public static Specification<Documento> conRangoDeFechas(Date fechaDesde, Date fechaHasta) {
        return (root, query, cb) -> {
            var pathFecha = root.<Date>get("fechaCreacion");

            if (fechaDesde != null && fechaHasta != null) {
                return cb.between(pathFecha, fechaDesde, fechaHasta);

            } else if (fechaDesde != null) {
                // Comparar solo la parte DATE de ambos lados
                return cb.equal(
                    cb.function("DATE", Date.class, pathFecha), 
                    cb.function("DATE", Date.class, cb.literal(fechaDesde))
                );

            } else if (fechaHasta != null) {
                return cb.lessThanOrEqualTo(pathFecha, fechaHasta);
            }

            return cb.conjunction();
        };
    }

    /**
     * (Avanzado) Excluye documentos que contengan CUALQUIERA
     * de las palabras de exclusión en los 4 campos principales.
     * @param excluirTerminos cadena con palabras a excluir, separadas por espacios
     * @return una especificación JPA para excluir documentos con los términos especificados,
     */
    public static Specification<Documento> conPalabrasExcluidas(String excluirTerminos) {
        return (root, query, cb) -> {
            
            List<Predicate> exclusionPredicates = new ArrayList<>();
            String[] words = excluirTerminos.toLowerCase().split("\\s+");

            // Define los campos donde se buscará la exclusión
            var tituloPath = cb.lower(root.get("titulo"));
            var resumenPath = cb.lower(root.get("resumen"));
            var numDocPath = cb.lower(root.get("numDocumento"));
            var tipoDocPath = cb.lower(root.get("tipoDocumento").get("nombre"));

            for (String word : words) {
                if (word.isEmpty()) continue;
                String pattern = "%" + word + "%";

                // 1. Busca si la palabra está EN CUALQUIER CAMPO (lógica OR)
                Predicate wordInAnyField = cb.or(
                    cb.like(tituloPath, pattern),
                    cb.like(resumenPath, pattern),
                    cb.like(numDocPath, pattern),
                    cb.like(tipoDocPath, pattern)
                );
                
                // 2. Niega ese predicado (NOT) y lo añade a la lista
                // Esto crea la condición: NOT (campo1 LIKE %word% OR campo2 LIKE %word% ...)
                exclusionPredicates.add(cb.not(wordInAnyField));
            }

            if (exclusionPredicates.isEmpty()) {
                return cb.conjunction();
            }
            
            // 3. Combina todas las exclusiones con AND
            // (ej: (NOT (...palabra1...)) AND (NOT (...palabra2...)))
            return cb.and(exclusionPredicates.toArray(new Predicate[0]));
        };
    }
}
