/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.rol;

import dev.labintec.digesto_system.rol.RolDTO;
import dev.labintec.digesto_system.rol.Rol;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre la entidad {@link Rol} y su
 * correspondiente objeto de transferencia de datos {@link RolDTO}.
 * 
 * Proporciona métodos estáticos para convertir entre ambos tipos.
 * Permite separar la entidad de la capa de presentación y la API.
 * 
 */
@Component
public class RolMapper {
    
    /**
     * Convierte un objeto {@link RolDTO} en una entidad {@link Rol}.
     * 
     * @param dto objeto de transferencia de datos a convertir
     * @return entidad {@link Rol} resultante
     */
    public static Rol toEntity(RolDTO dto) {
        Rol rol = new Rol();
        rol.setIdRol(dto.getIdRol());
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        return rol;
    }
    
    /**
     * Convierte una entidad {@link Rol} en un objeto {@link RolDTO}.
     * 
     * @param rol entidad a convertir
     * @return objeto {@link RolDTO} resultante
     */
    public static RolDTO toDTO(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setIdRol(rol.getIdRol());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        return dto;
    }
}

