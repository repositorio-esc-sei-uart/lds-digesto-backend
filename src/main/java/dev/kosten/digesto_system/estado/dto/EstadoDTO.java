package dev.kosten.digesto_system.estado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para la entidad Estado.
 * Representa la versión simplificada de un Estado que se expone a la API
 * y se consume en el frontend, desacoplando la vista de la entidad de persistencia.
 * @author micael
 * @author Quique
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {
    private Integer idEstado;
    private String nombre;
    private String descripcion;
}
