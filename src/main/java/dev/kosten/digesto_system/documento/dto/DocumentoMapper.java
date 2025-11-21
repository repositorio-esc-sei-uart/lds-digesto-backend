package dev.kosten.digesto_system.documento.dto;

import dev.kosten.digesto_system.archivo.dto.ArchivoMapper;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.estado.dto.EstadoMapper;
import dev.kosten.digesto_system.palabraclave.dto.PalabraClaveMapper;
import dev.kosten.digesto_system.tipodocumento.dto.TipoDocumentoMapper;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Componente Mapper para conversiones entre la entidad {@link Documento} y sus DTOs.
 * Se encarga de la "traducción" de datos entre la capa de persistencia y la capa de API,
 * inyectando otros mappers (Archivo, PalabraClave) para componer objetos complejos.
 * @author micael
 * @author Quique
 */
@Component
@RequiredArgsConstructor
public class DocumentoMapper {

    // Dependencias inyectadas para mapeo de sub-objetos
    private final ArchivoMapper archivoMapper;
    private final PalabraClaveMapper palabraClaveMapper;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final EstadoMapper estadoMapper;
    /**
     * Convierte una Entidad (con todos sus objetos) a un DTO (con datos simples).
     * Este método se usa al ENVIAR datos al frontend.
     * @param documento La entidad Documento leída de la base de datos.
     * @return Un DTO listo para ser enviado como JSON.
     */
    public DocumentoDTO toDTO(Documento documento) {
        if (documento == null) {
            return null;
        }
        return DocumentoDTO.builder()
            // Campos simples
            .idDocumento(documento.getIdDocumento())
            .titulo(documento.getTitulo())
            .activo(documento.isActivo())
            .resumen(documento.getResumen())
            .numDocumento(documento.getNumDocumento())
            .fechaCreacion(documento.getFechaCreacion())

            // Campos "planos"
            .nombreEstado(documento.getEstado().getNombre())
            .nombreTipoDocumento(documento.getTipoDocumento().getNombre())
            .nombreSector(documento.getSector().getNombre())
            .nombreUnidadEjecutora(documento.getUnidadEjecutora().getNombre())

            // --- Mapeo de Colecciones (Listas) ---
            .archivos(
                documento.getArchivos().stream()
                    .map(archivoMapper::toDTO)
                    .collect(Collectors.toList())
            )
            .palabrasClave(
                documento.getPalabrasClave().stream()
                    .map(palabraClaveMapper::toDTO)
                    .collect(Collectors.toList())
            )
            .referencias(
                documento.getReferencias().stream()
                    .map(this::toReferenciaDTO)
                    .collect(Collectors.toList())
            )
            .referenciadoPor(
                documento.getReferenciadoPor().stream()
                    .map(this::toReferenciaDTO)
                    .collect(Collectors.toList())
            )
            .build();
    }
    
    /**
     * Convierte una entidad Documento a un DTO de "Referencia".
     * Este DTO simple (solo ID y número) se usa para las listas de referencias
     * y así evitar bucles de serialización infinitos (un documento A
     * referencia a B, y B referencia a A).
     * @param documento La entidad Documento a la que se quiere referenciar.
     * @return Un {@link DocumentoReferenciaDTO} simple.
     */
    public DocumentoReferenciaDTO toReferenciaDTO(Documento documento) {
        if (documento == null) {
            return null;
        }
        return DocumentoReferenciaDTO.builder()
            .idDocumento(documento.getIdDocumento())
            .numDocumento(documento.getNumDocumento())
            .build();
    }

    /**
     * Convierte una entidad Documento a un DTO de "Tabla".
     * DTO "ligero" optimizado para poblar tablas o listas en el frontend.
     * Contiene solo la información esencial para una fila, evitando la sobrecarga
     * de cargar colecciones (@ManyToMany, @OneToMany) innecesarias.
     * @param documento La entidad Documento completa.
     * @return Un DocumentoTablaDTO listo para una fila de la tabla.
     */
    public DocumentoTablaDTO toTablaDTO(Documento documento) {
        if (documento == null) {
            return null;
        }
        return DocumentoTablaDTO.builder()
            .idDocumento(documento.getIdDocumento())
            .titulo(documento.getTitulo())
            .numDocumento(documento.getNumDocumento())
            .resumen(documento.getResumen())
            .fechaCreacion(documento.getFechaCreacion())
            .tipoDocumento(tipoDocumentoMapper.toDTO(documento.getTipoDocumento()))
            .estado(estadoMapper.toDTO(documento.getEstado())) 
            .build();
    }
}
