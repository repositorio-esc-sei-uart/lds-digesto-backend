package dev.kosten.digesto_system.documento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Un DTO simple solo para mostrar las referencias de un documento,
 * evitando bucles de serialización.
 * @author micael
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentoReferenciaDTO {
    private Integer idDocumento;
    private String numDocumento;
    private boolean activo;
}
