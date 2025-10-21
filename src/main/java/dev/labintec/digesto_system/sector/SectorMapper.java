/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.sector;

import dev.labintec.digesto_system.sector.SectorDTO;
import dev.labintec.digesto_system.sector.Sector;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre la entidad {@link Sector} y su
 * correspondiente objeto de transferencia de datos {@link SectorDTO}.
 * 
 * Proporciona métodos estáticos para convertir entre ambos tipos.
 * Permite separar la entidad de la capa de presentación y la API.
 * 
 */


@Component
public class SectorMapper {
    
    /**
     * Convierte un objeto {@link SectorDTO} en una entidad {@link Sector}.
     * 
     * @param dto objeto de transferencia de datos a convertir
     * @return entidad {@link Sector} resultante
     */
    public static Sector toEntity(SectorDTO dto) {
        Sector s = new Sector();
        s.setIdSector(dto.getIdSector());
        s.setNombre(dto.getNombre());
        s.setDescripcion(dto.getDescripcion());
        return s;
    }
    
    /**
     * Convierte una entidad {@link Sector} en un objeto {@link SectorDTO}.
     * 
     * @param s entidad a convertir
     * @return objeto {@link SectorDTO} resultante
     */
    public static SectorDTO toDTO(Sector s) {
        SectorDTO dto = new SectorDTO();
        dto.setIdSector(s.getIdSector());
        dto.setNombre(s.getNombre());
        dto.setDescripcion(s.getDescripcion());
        return dto;
    }
}

