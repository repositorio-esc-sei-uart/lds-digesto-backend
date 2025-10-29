/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.palabraclave.dto;

import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;

/**
 *
 * @author micae
 */
public class PalabraClaveMapper {
    
    public static PalabraClaveDTO toDTO(PalabraClave entity) {
        if (entity == null) {
            return null;
        }
        PalabraClaveDTO dto = new PalabraClaveDTO();
        dto.setIdPalabraClave(entity.getIdPalabraClave());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    public static PalabraClave toEntity(PalabraClaveDTO dto) {
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
