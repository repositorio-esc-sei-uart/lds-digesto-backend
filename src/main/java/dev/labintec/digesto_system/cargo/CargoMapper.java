/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.cargo;

import dev.labintec.digesto_system.cargo.CargoDTO;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre la entidad {@link Cargo} y su
 * correspondiente objeto de transferencia de datos {@link CargoDTO}.
 * 
 * Proporciona métodos estáticos para convertir entre ambos tipos.
 * 
 */
@Component
public class CargoMapper {
    
    /**
     * Convierte un objeto {@link CargoDTO} en una entidad {@link Cargo}.
     * 
     * @param dto objeto de transferencia de datos a convertir
     * @return entidad {@link Cargo} resultante
     */
    public static Cargo toEntity(CargoDTO dto) {
        Cargo c = new Cargo();
        c.setIdCargo(dto.getIdCargo());
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
        return c;
    }
    
    /**
     * Convierte una entidad {@link Cargo} en un objeto {@link CargoDTO}.
     * 
     * @param c entidad a convertir
     * @return objeto {@link CargoDTO} resultante
     */
    public static CargoDTO toDTO(Cargo c) {
        CargoDTO dto = new CargoDTO();
        dto.setIdCargo(c.getIdCargo());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        return dto;
    }
}


