package dev.kosten.digesto_system.documento.dto;

import dev.kosten.digesto_system.estado.dto.EstadoDTO;
import dev.kosten.digesto_system.tipodocumento.dto.TipoDocumentoDTO;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO "liviano" usado para poblar las tablas principales en el frontend.
 * Contiene solo la información esencial para una fila de la tabla.
 * @author micael
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentoTablaDTO {
    private Integer idDocumento;
    private String titulo;
    private String numDocumento;
    private Date fechaCreacion;
    private String resumen;
    private TipoDocumentoDTO tipoDocumento;
    private EstadoDTO estado;
}
