/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.unidadEjecutora;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Objeto de transferencia de datos (DTO) para la entidad {@link UnidadEjecutora}.
 * 
 * Se utiliza para intercambiar información de la unidadEjecutora entre las capas
 * del sistema sin exponer directamente la entidad.
 * 
 * Incluye validaciones para los campos de entrada.
 * @author Matias
 */
@Data
@NoArgsConstructor
@AllArgsConstructor


public class UnidadEjecutoraDTO {
    
    /**
     * Identificador único de la unidad Ejecutora.
     */
    private Integer idUnidadEjecutora;
    
    /**
     * Nombre de la unidad Ejecutora.
     * No puede estar en blanco.
     */
    @NotBlank
    private String nombre;
    
    /**
     * Descripción de la unidad Ejecutora.
     */
    private String descripcion;
    
    /**
     * Siglas de la unidad Ejecutora.
     */
    private String nomenclatura;
}
