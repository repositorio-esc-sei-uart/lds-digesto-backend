/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.archivo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la entidad Archivo.
 * Transporta los datos esenciales del archivo entre el backend y el frontend.
 * @author micael
 * @author Quique
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchivoDTO {
    private Integer idArchivo;
    private String nombre;
    private String url;
}
