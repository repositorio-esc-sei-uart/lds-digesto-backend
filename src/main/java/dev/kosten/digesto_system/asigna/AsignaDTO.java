/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.asigna;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la entidad {@link Asigna}.
 * 
 * Representa la asignación de un {@link Permiso} a un {@link Rol} de manera simplificada
 * para la capa de presentación o intercambio a través de la API.
 * 
 * Incluye opcionalmente campos de solo lectura para mostrar información adicional
 * como los nombres del rol y del permiso.
 * 
 * Separa la entidad de la capa de presentación, evitando exponer directamente la entidad JPA.
 * 
 */
@Data
public class AsignaDTO {
    
    /**
     * Identificador del rol.
     */
    private Integer idRol;
    
    /**
     * Identificador del permiso.
     */
    private Integer idPermiso;
    
    
    // Opcional: campos de solo lectura para mostrar info adicional
    
    /**
     * Nombre del rol (solo lectura, opcional).
     */
    private String nombreRol;
    
    /**
     * Nombre del permiso (solo lectura, opcional).
     */
    private String nombrePermiso;

}


