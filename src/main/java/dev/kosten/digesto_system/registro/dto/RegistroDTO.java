package dev.kosten.digesto_system.registro.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Registro.
 * Se utiliza para mostrar el historial de auditoría.
 * @author Quique
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {

    /**
     * ID del registro de auditoría.
     */
    private Integer idRegistro;

    /**
     * Fecha de la operación.
     */
    private Date fechaCarga;

    /**
     * ID del documento afectado.
     */
    private Integer idDocumento;
    
    /**
     * ID del usuario que realizó la operación.
     */
    private Integer idUsuario;
}
