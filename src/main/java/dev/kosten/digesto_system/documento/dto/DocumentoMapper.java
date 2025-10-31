package dev.kosten.digesto_system.documento.dto;

import dev.kosten.digesto_system.archivo.dto.ArchivoMapper;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.palabraclave.dto.PalabraClaveMapper;
import dev.kosten.digesto_system.tipodocumento.dto.TipoDocumentoMapper;
import java.util.stream.Collectors;

/**
 * Clase de utilidad (Mapper) para convertir entre la Entidad Documento y DocumentoDTO.
 * Se encarga de la "traducción" de datos.
 * @author micael
 * @author Quique
 */

public class DocumentoMapper {
    /**
     * Convierte una Entidad (con todos sus objetos) a un DTO (con datos simples).
     * Este método se usa al ENVIAR datos al frontend.
     * @param documento La entidad Documento leída de la base de datos.
     * @return Un DTO listo para ser enviado como JSON.
     */
    public static DocumentoDTO toDTO(Documento documento) {
        if (documento == null) {
            return null;
        }
        return DocumentoDTO.builder()
            // Campos simples
            .idDocumento(documento.getIdDocumento())
            .titulo(documento.getTitulo())
            .resumen(documento.getResumen())
            .numDocumento(documento.getNumDocumento())
            .fechaCreacion(documento.getFechaCreacion())

            // Campos "planos"
            .nombreEstado(documento.getEstado().getNombre())
            .nombreTipoDocumento(documento.getTipoDocumento().getNombre())
            .nombreSector(documento.getSector().getNombre())

            // Mapeo de Listas (usando .stream())
            // Nota: Esto asume que tus listas en 'Documento.java' están inicializadas
            // (ej. private List<Archivo> archivos = new ArrayList<>();)
            // Si no lo están, este código podría fallar con un NullPointerException.
            
            .archivos(
                documento.getArchivos().stream()
                    .map(ArchivoMapper::toDTO)
                    .collect(Collectors.toList())
            )
            .palabrasClave(
                documento.getPalabrasClave().stream()
                    .map(PalabraClaveMapper::toDTO)
                    .collect(Collectors.toList())
            )
            .referencias(
                documento.getReferencias().stream()
                    .map(DocumentoMapper::toReferenciaDTO)
                    .collect(Collectors.toList())
            )
            .referenciadoPor(
                documento.getReferenciadoPor().stream()
                    .map(DocumentoMapper::toReferenciaDTO)
                    .collect(Collectors.toList())
            )
            .build();
    }
    
    /**
     * NUEVO MÉTODO: Convierte una Entidad Documento a un DTO de Referencia simple.
     * Se crea este DTO simple (DocumentoReferenciaDTO) para evitar bucles
     * infinitos de serialización (un documento que referencia a otro y viceversa).
     */
    public static DocumentoReferenciaDTO toReferenciaDTO(Documento documento) {
        if (documento == null) {
            return null;
        }
        
        return DocumentoReferenciaDTO.builder()
            .idDocumento(documento.getIdDocumento())
            .numDocumento(documento.getNumDocumento())
            .build();
    }

    /**
     * Optimizado para el frontend, este DTO genera el objeto exacto que consume el
     * `Home` de Angular para renderizar las "cards" de la página principal.
     * @param documento La entidad Documento completa, obtenida de la base de datos.
     * @return Un DocumentoTablaDTO listo para ser enviado al frontend,
     * o null si la entidad de entrada era nula.
     */
    public static DocumentoTablaDTO toTablaDTO(Documento documento) {
        if (documento == null) {
            return null;
        }

        return DocumentoTablaDTO.builder()
            .idDocumento(documento.getIdDocumento())
            .titulo(documento.getTitulo())
            .numDocumento(documento.getNumDocumento())
            .resumen(documento.getResumen())
            .fechaCreacion(documento.getFechaCreacion())
            .tipoDocumento(TipoDocumentoMapper.toDTO(documento.getTipoDocumento()))
            .build();
    }
}
