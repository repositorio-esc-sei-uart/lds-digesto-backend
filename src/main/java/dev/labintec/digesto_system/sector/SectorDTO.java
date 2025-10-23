/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.sector;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Objeto de transferencia de datos (DTO) para la entidad {@link Sector}.
 * 
 * Se utiliza para intercambiar información del sector entre las capas
 * del sistema sin exponer directamente la entidad.
 * 
 * Incluye validaciones para los campos de entrada.
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor


public class SectorDTO {
    
    /**
     * Identificador único del sector.
     */
    private Integer idSector;
    
    /**
     * Nombre del sector.
     * No puede estar en blanco.
     */
    @NotBlank
    private String nombre;
    
    /**
     * Descripción del sector.
     */
    private String descripcion;
}
