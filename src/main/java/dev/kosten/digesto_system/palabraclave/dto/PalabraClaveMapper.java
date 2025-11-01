package dev.kosten.digesto_system.palabraclave.dto;

import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import org.springframework.stereotype.Component;

/**
 *
 * @author micael
 * @author Quique
 */
@Component
public class PalabraClaveMapper {
    
    public PalabraClaveDTO toDTO(PalabraClave entity) {
        if (entity == null) {
            return null;
        }
        PalabraClaveDTO dto = new PalabraClaveDTO();
        dto.setIdPalabraClave(entity.getIdPalabraClave());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    public PalabraClave toEntity(PalabraClaveDTO dto) {
        if (dto == null) {
            return null;
        }
        PalabraClave entity = new PalabraClave();
        entity.setIdPalabraClave(dto.getIdPalabraClave());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }
}
