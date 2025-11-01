package dev.kosten.digesto_system.documento.dto;

import dev.kosten.digesto_system.archivo.dto.ArchivoDTO;
import dev.kosten.digesto_system.palabraclave.dto.PalabraClaveDTO;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para la entidad Documento.
 * Se usa para transferir datos de forma segura entre el frontend y el backend,
 * evitando exponer la entidad de la base de datos directamente.
 * @author micael
 * @author Quique
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentoDTO {
    
    // --- Campos de Identificación y Datos Comunes ---
    private Integer idDocumento;
    private String titulo;
    private String resumen;
    private String numDocumento;
    private Date fechaCreacion;

    // --- Campos para LECTURA (GET - Enviar al Frontend) ---
    private String nombreEstado;
    private String nombreTipoDocumento;
    private String nombreSector;
    private List<ArchivoDTO> archivos;
    private List<PalabraClaveDTO> palabrasClave;
    
    // Lista de documentos a los que este "hace referencia".
    private List<DocumentoReferenciaDTO> referencias;
    // Lista de documentos a los que es "referenciado por".
    private List<DocumentoReferenciaDTO> referenciadoPor;

    // --- Campos para ESCRITURA (POST/PUT - Recibir del Frontend) ---
    private Integer idEstado;
    private Integer idTipoDocumento;
    private Integer idSector;
    private List<Integer> idsPalabrasClave;
    
    // Lista de documentos a los que este "hace referencia".
    private List<Integer> idsReferencias;
}
