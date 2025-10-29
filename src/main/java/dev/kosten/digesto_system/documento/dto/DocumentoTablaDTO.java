/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.documento.dto;

import lombok.Data;

/**
 *
 * @author micae
 */
/**
 * DTO "liviano" usado para poblar las tablas principales en el frontend.
 * Contiene solo la información esencial para una fila de la tabla.
 */
@Data
public class DocumentoTablaDTO {
    private Integer idDocumento;
    private String titulo;
    private String numDocumento;
    private String nombreEstado;
    private String nombreTipoDocumento;
}
