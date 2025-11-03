package dev.kosten.digesto_system.estado.dto;

import dev.kosten.digesto_system.estado.entity.Estado;
import org.springframework.stereotype.Component;

/**
 * /**
 * Componente de mapeo para conversiones entre la entidad Estado y EstadoDTO.
 * Este mapper utiliza el patrón Builder (asumiendo que las clases DTO y Entidad
 * están anotadas con @Builder para una instanciación fluida y legible.
 * @author micael
 * @author Quique
 */
@Component
public class EstadoMapper {
    /**
     * Convierte una entidad Estado en su DTO correspondiente.
     * @param entity La entidad de la base de datos a convertir.
     * @return Un EstadoDTO poblado con los datos de la entidad.
     */
    public EstadoDTO toDTO(Estado entity) {
        if (entity == null) {
            return null;
        }
        
        return EstadoDTO.builder()
            .idEstado(entity.getIdEstado())
            .nombre(entity.getNombre())
            .descripcion(entity.getDescripcion())
            .build();
    }

    /**
     * Convierte un EstadoDTO en su entidad Estado correspondiente.
     * @param dto El Data Transfer Object a convertir.
     * @return Una entidad Estado lista para ser persistida o actualizada.
     */
    public Estado toEntity(EstadoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Estado.builder()
                .idEstado(dto.getIdEstado())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }
}
