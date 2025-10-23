/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.tipodocumento.dto;

import dev.labintec.digesto_system.tipodocumento.entity.TipoDocumento;

/**
 *
 * @author micae
 */
public class TipoDocumentoMapper {
    
    public static TipoDocumentoDTO toDTO(TipoDocumento entity) {
        if (entity == null) {
            return null;
        }
        TipoDocumentoDTO dto = new TipoDocumentoDTO();
        dto.setIdTipoDocumento(entity.getIdTipoDocumento());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }
    
    
    
    public static TipoDocumento toEntity(TipoDocumentoDTO dto) {
        if (dto == null) {
            return null;
        }
        TipoDocumento entity = new TipoDocumento();
        entity.setIdTipoDocumento(dto.getIdTipoDocumento());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }
}
