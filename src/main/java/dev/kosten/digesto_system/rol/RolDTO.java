/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la entidad {@link Rol}.
 * 
 * Se utiliza para intercambiar información del rol entre las capas
 * del sistema sin exponer directamente la entidad.
 * 
 * Incluye validaciones para los campos de entrada.
 * 
 */
@Data
public class RolDTO {
    
    /**
     * Identificador único del rol.
     */
    private Integer idRol;
    
    /**
     * Nombre del rol.
     * No puede estar en blanco.
     */
    @NotBlank
    private String nombre;
    
    /**
     * Descripción del rol.
     */
    private String descripcion;

}


