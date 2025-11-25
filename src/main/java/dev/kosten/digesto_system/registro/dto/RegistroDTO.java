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

    private Integer idRegistro;
    private Date fechaCarga;
    private String tipoOperacion; // "Registrar", "Modificar", "Activar", "Desactivar"

    // --- RESPONSABLE (Quien hizo la acción) ---
    private String nombreUsuarioResponsable;
    private String legajoUsuarioResponsable;

    // --- DOCUMENTO AFECTADO (Si aplica) ---
    private String numDocumentoAfectado;
    private String tituloDocumentoAfectado;

    // --- USUARIO AFECTADO (Si aplica, para auditoría de usuarios) ---
    private String nombreUsuarioAfectado;
    private String legajoUsuarioAfectado;
}