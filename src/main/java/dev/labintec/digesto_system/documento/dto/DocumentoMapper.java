/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.documento.dto;

import dev.labintec.digesto_system.archivo.dto.ArchivoMapper;
import dev.labintec.digesto_system.documento.entity.Documento;
import dev.labintec.digesto_system.palabraclave.dto.PalabraClaveMapper;
import java.util.stream.Collectors;

/**
 *
 * @author micae
 */

/**
 * Clase de utilidad (Mapper) para convertir entre la Entidad Documento y DocumentoDTO.
 * Se encarga de la "traducción" de datos.
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
        DocumentoDTO dto = new DocumentoDTO();

        // 1. Mapea los campos simples
        dto.setIdDocumento(documento.getIdDocumento());
        dto.setTitulo(documento.getTitulo());
        dto.setResumen(documento.getResumen());
        dto.setNumDocumento(documento.getNumDocumento());

        // 2. Mapea los campos de relaciones (NUEVO)
        // (Verifica si el objeto existe antes de pedir su nombre para evitar errores)
        if (documento.getEstado() != null) {
            dto.setNombreEstado(documento.getEstado().getNombre());
        }
        if (documento.getTipoDocumento() != null) {
            dto.setNombreTipoDocumento(documento.getTipoDocumento().getNombre());
        }
        if (documento.getSector() != null) {
            dto.setNombreSector(documento.getSector().getNombre());
        }

        // 3. Mapea las listas usando los otros Mappers (NUEVO)
        if (documento.getArchivos() != null) {
            dto.setArchivos(
                documento.getArchivos().stream()
                    .map(ArchivoMapper::toDTO) // Llama al Mapper de Archivo
                    .collect(Collectors.toList())
            );
        }
        if (documento.getPalabrasClave() != null) {
            dto.setPalabrasClave(
                documento.getPalabrasClave().stream()
                    .map(PalabraClaveMapper::toDTO) // Llama al Mapper de PalabraClave
                    .collect(Collectors.toList())
            );
        }
        
        // (Añadido para la nueva tabla 'referencia')
        if (documento.getReferencias() != null) {
            dto.setReferencias(
                documento.getReferencias().stream()
                    .map(DocumentoMapper::toReferenciaDTO) // Llama al nuevo método
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }
    
    

    /**
     * Convierte un DTO a una Entidad.
     * NOTA: Este método NO se usa para crear/actualizar, porque la lógica
     * de buscar los objetos por ID (ej. buscar Estado por idEstado)
     * la maneja el Servicio, no el Mapper.
     */
    // public static Documento toEntity(DocumentoDTO dto) { ... }
    
    
    /**
     * NUEVO MÉTODO: Convierte una Entidad Documento a un DTO de Referencia simple.
     * Se crea este DTO simple (DocumentoReferenciaDTO) para evitar bucles
     * infinitos de serialización (un documento que referencia a otro y viceversa).
     */
    public static DocumentoReferenciaDTO toReferenciaDTO(Documento documento) {
        if (documento == null) return null;
        
        DocumentoReferenciaDTO refDto = new DocumentoReferenciaDTO();
        
        refDto.setIdDocumento(documento.getIdDocumento());
        refDto.setTitulo(documento.getTitulo());
        return refDto;
    }
    /**
     * MÉTODO "SIMPLE": Convierte una Entidad a un DTO "liviano" solo para tablas.
     */
    public static DocumentoTablaDTO toTablaDTO(Documento documento) {
        if (documento == null) {
            return null;
        }
        DocumentoTablaDTO dto = new DocumentoTablaDTO();

        dto.setIdDocumento(documento.getIdDocumento());
        dto.setTitulo(documento.getTitulo());
        dto.setNumDocumento(documento.getNumDocumento());
        
        if (documento.getEstado() != null) {
            dto.setNombreEstado(documento.getEstado().getNombre());
        }
        if (documento.getTipoDocumento() != null) {
            dto.setNombreTipoDocumento(documento.getTipoDocumento().getNombre());
        }
        
        return dto;
    }
    
    
    
    
}
