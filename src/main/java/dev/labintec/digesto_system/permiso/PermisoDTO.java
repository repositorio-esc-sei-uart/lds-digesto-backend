/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.permiso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la entidad {@link Permiso}.
 * 
 * Se utiliza para intercambiar información del permiso entre las capas
 * del sistema sin exponer directamente la entidad.
 * 
 * Incluye validaciones para los campos de entrada.
 * 
 */
@Data
public class PermisoDTO {
    
    /**
     * Identificador único del permiso.
     */
    private Integer idPermiso;
    
    /**
     * Nombre del permiso.
     * No puede estar en blanco y tiene un máximo de 45 caracteres.
     */
    @NotBlank
    private String nombre;
    
    /**
     * Descripción del permiso.
     * Longitud máxima de 45 caracteres.
     */
    private String descripcion;
}
