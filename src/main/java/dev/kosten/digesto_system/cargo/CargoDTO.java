/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.cargo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Objeto de transferencia de datos (DTO) para la clase Cargo.
 * 
 * Se utiliza para enviar y recibir información de los cargos entre la API y el cliente,
 * sin exponer directamente la entidad.
 * Contiene el identificador, nombre y descripción del cargo.
 * 
 */
@Data                   // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor      // Crea un constructor vacío
@AllArgsConstructor     // Crea un constructor con todos los campos
public class CargoDTO {
    private Integer idCargo;

    @NotBlank
    private String nombre;

    private String descripcion;
}


 