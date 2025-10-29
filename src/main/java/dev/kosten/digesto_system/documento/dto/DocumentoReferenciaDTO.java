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
 * Un DTO simple solo para mostrar las referencias de un documento,
 * evitando bucles de serialización.
 */
@Data
public class DocumentoReferenciaDTO {
    private Integer idDocumento;
    private String titulo;
}
