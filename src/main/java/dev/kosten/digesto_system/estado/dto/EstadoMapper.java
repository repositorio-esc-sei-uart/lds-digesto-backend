package dev.kosten.digesto_system.estado.dto;

import dev.kosten.digesto_system.estado.entity.Estado;
import org.springframework.stereotype.Component;

/**
 *
 * @author micael
 * @author Quique
 */
@Component
public class EstadoMapper {
    public static EstadoDTO toDTO(Estado entity) {
        if (entity == null) {
            return null;
        }
        EstadoDTO dto = new EstadoDTO();
        dto.setIdEstado(entity.getIdEstado());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    public static Estado toEntity(EstadoDTO dto) {
        if (dto == null) {
            return null;
        }
        Estado entity = new Estado();
        entity.setIdEstado(dto.getIdEstado());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }
}
