/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.unidadEjecutora;

import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre la entidad {@link UnidadEjecutora} y su
 * correspondiente objeto de transferencia de datos {@link UnidadEjecutoraDTO}.
 * 
 * Proporciona métodos estáticos para convertir entre ambos tipos.
 * Permite separar la entidad de la capa de presentación y la API.
 * @author Matias
 */


@Component
public class UnidadEjecutoraMapper {
    
    /**
     * Convierte un objeto {@link UnidadEjecutoraDTO} en una entidad {@link UnidadEjecutora}.
     * 
     * @param dto objeto de transferencia de datos a convertir
     * @return entidad {@link UnidadEjecutora} resultante
     */
    public static UnidadEjecutora toEntity(UnidadEjecutoraDTO dto) {
        UnidadEjecutora u = new UnidadEjecutora();
        u.setIdUnidadEjecutora(dto.getIdUnidadEjecutora());
        u.setNombre(dto.getNombre());
        u.setDescripcion(dto.getDescripcion());
        u.setNomenclatura(dto.getNomenclatura());
        return u;
    }
    
    /**
     * Convierte una entidad {@link UnidadEjecutora} en un objeto {@link UnidadEjecutoraDTO}.
     * 
     * @param s entidad a convertir
     * @return objeto {@link UnidadEjecutoraDTO} resultante
     */
    public static UnidadEjecutoraDTO toDTO(UnidadEjecutora u) {
        UnidadEjecutoraDTO dto = new UnidadEjecutoraDTO();
        dto.setIdUnidadEjecutora(u.getIdUnidadEjecutora());
        dto.setNombre(u.getNombre());
        dto.setDescripcion(u.getDescripcion());
        dto.setNomenclatura(u.getNomenclatura());
        return dto;
    }
}

