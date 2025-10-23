/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.documento.dto;

// --- Imports de los DTOs de los otros módulos ---
import dev.labintec.digesto_system.archivo.dto.ArchivoDTO;
import dev.labintec.digesto_system.palabraclave.dto.PalabraClaveDTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author micae
 */

/**
 * Data Transfer Object (DTO) para la entidad Documento.
 * Se usa para transferir datos de forma segura entre el frontend y el backend,
 * evitando exponer la entidad de la base de datos directamente.
 */
@Data
@NoArgsConstructor
public class DocumentoDTO {
    
    // --- Campos propios del Documento ---
    private Integer idDocumento;
    private String titulo;
    private String resumen;
    private String numDocumento; // Correcto que sea String

    // --- Campos para ENVIAR al Frontend (GET) ---
    private String nombreEstado;
    private String nombreTipoDocumento;
    private String nombreSector;
    private List<ArchivoDTO> archivos;
    private List<PalabraClaveDTO> palabrasClave;
    private List<DocumentoReferenciaDTO> referencias; // Este ya lo tenías

    // --- Campos para RECIBIR del Frontend (POST / PUT) ---
    private Integer idEstado;
    private Integer idTipoDocumento;
    private Integer idSector;
    private List<Integer> idsPalabrasClave;
    
    // --- ¡ESTE CAMPO FALTABA! ---
    private List<Integer> idsReferencias; // Para recibir los IDs al crear/actualizar
    // --- FIN DE LO QUE FALTABA ---
}
