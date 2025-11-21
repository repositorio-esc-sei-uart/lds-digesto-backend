package dev.kosten.digesto_system.tipodocumento.dto;

import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import org.springframework.stereotype.Component;

/**
 *
 * @author micael
 * @author Quique
 */
@Component
public class TipoDocumentoMapper {
    
    public TipoDocumentoDTO toDTO(TipoDocumento entity) {
        if (entity == null) {
            return null;
        }
        TipoDocumentoDTO dto = new TipoDocumentoDTO();
        dto.setIdTipoDocumento(entity.getIdTipoDocumento());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setNomenclatura(entity.getNomenclatura());
        return dto;
    }
    
    
    
    public TipoDocumento toEntity(TipoDocumentoDTO dto) {
        if (dto == null) {
            return null;
        }
        TipoDocumento entity = new TipoDocumento();
        entity.setIdTipoDocumento(dto.getIdTipoDocumento());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setNomenclatura(dto.getNomenclatura());
        return entity;
    }
}
