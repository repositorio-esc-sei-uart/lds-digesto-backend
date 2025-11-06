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

    // --- CAMPOS ENRIQUECIDOS ---
    /**
    * Nombre y Apellido del usuario que realizó la operación.
    */
    private String nombreUsuario;
    
    /**
    * Legajo del usuario.
    */
    private String legajoUsuario;
    
    /**
    * Número del documento afectado (Ej: "RES-001/24").
    */
    private String numDocumento;
    
    /**
    * Título del documento afectado.
    */
    private String tituloDocumento;
}
