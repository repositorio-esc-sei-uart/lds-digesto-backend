/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.estadoU;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la entidad {@link EstadoU}.
 * 
 * Permite enviar y recibir información sobre los estados de usuario
 * sin exponer directamente la entidad de persistencia.
 * 
 * Incluye validaciones básicas de campos como no nulo y tamaño máximo.
 * 
 */
@Data
public class EstadoUDTO {
    
    /**
     * Identificador único del estado de usuario.
     */
    private Integer idEstadoU;
    
    /**
     * Nombre del estado de usuario. No puede estar vacío y máximo 45 caracteres.
     */
    @NotBlank
    @Size(max = 45)
    private String nombre;
    
    /**
     * Descripción opcional del estado de usuario. Máximo 45 caracteres.
     */
    @Size(max = 45)
    private String descripcion;
}

