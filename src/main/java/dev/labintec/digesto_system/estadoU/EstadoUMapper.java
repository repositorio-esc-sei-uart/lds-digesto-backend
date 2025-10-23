/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.estadoU;

import dev.labintec.digesto_system.estadoU.EstadoUDTO;
import dev.labintec.digesto_system.estadoU.EstadoU;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre la entidad {@link EstadoU} y su
 * correspondiente objeto de transferencia de datos {@link EstadoUDTO}.
 * 
 * Proporciona métodos para convertir de entidad a DTO y viceversa,
 * permitiendo separar la capa de persistencia de la capa de presentación.
 * 
 * Facilita la manipulación de datos en la API sin exponer directamente la entidad.
 * 
 */
@Component
public class EstadoUMapper {
    
    /**
     * Convierte un {@link EstadoUDTO} en una entidad {@link EstadoU}.
     * 
     * @param dto objeto de transferencia de datos
     * @return entidad {@link EstadoU} resultante
     */
    public EstadoU toEntity(EstadoUDTO dto) {
        EstadoU e = new EstadoU();
        e.setIdEstadoU(dto.getIdEstadoU());
        e.setNombre(dto.getNombre());
        e.setDescripcion(dto.getDescripcion());
        return e;
    }
    
    /**
     * Convierte una entidad {@link EstadoU} en un {@link EstadoUDTO}.
     * 
     * @param e entidad a convertir
     * @return objeto {@link EstadoUDTO} resultante
     */
    public EstadoUDTO toDTO(EstadoU e) {
        EstadoUDTO dto = new EstadoUDTO();
        dto.setIdEstadoU(e.getIdEstadoU());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        return dto;
    }
    
}

