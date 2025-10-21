/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.permiso;

import dev.labintec.digesto_system.permiso.PermisoDTO;
import dev.labintec.digesto_system.permiso.Permiso;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre la entidad {@link Permiso} y su
 * correspondiente objeto de transferencia de datos {@link PermisoDTO}.
 * 
 * Proporciona métodos estáticos para convertir entre ambos tipos.
 * Permite separar la entidad de la capa de presentación y la API.
 * 
 */
@Component
public class PermisoMapper {
    
    /**
     * Convierte un objeto {@link PermisoDTO} en una entidad {@link Permiso}.
     * 
     * @param dto objeto de transferencia de datos a convertir
     * @return entidad {@link Permiso} resultante
     */
    public static Permiso toEntity(PermisoDTO dto) {
        Permiso permiso = new Permiso();
        permiso.setIdPermiso(dto.getIdPermiso());
        permiso.setNombre(dto.getNombre());
        permiso.setDescripcion(dto.getDescripcion());
        return permiso;
    }
    
    /**
     * Convierte una entidad {@link Permiso} en un objeto {@link PermisoDTO}.
     * 
     * @param permiso entidad a convertir
     * @return objeto {@link PermisoDTO} resultante
     */
    public static PermisoDTO toDTO(Permiso permiso) {
        PermisoDTO dto = new PermisoDTO();
        dto.setIdPermiso(permiso.getIdPermiso());
        dto.setNombre(permiso.getNombre());
        dto.setDescripcion(permiso.getDescripcion());
        return dto;
    }
}

