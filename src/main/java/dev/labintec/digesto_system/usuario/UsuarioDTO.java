/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Objeto de transferencia de datos (DTO) para la clase Usuario.
 * 
 * Data Transfer Object (DTO) que representa los datos de un usuario 
 * utilizados para el intercambio de información entre el backend y otras capas 
 * del sistema (por ejemplo, controladores o vistas).
 * 
 * Este objeto no mapea directamente a una tabla, sino que se usa para 
 * transportar información simplificada del usuario, incluyendo datos de 
 * sus relaciones con otras entidades como Rol, Sector, Estado y Cargo.
 * 
 * Incluye validaciones para asegurar la consistencia de los datos antes 
 * de ser procesados o almacenados.
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioDTO {
    
    
    private Integer idUsuario;

    private Integer dni;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String nombre;

    private String apellido;

    private String legajo;

    @NotNull
    private Integer idRol;
    private String nombreRol;

    @NotNull
    private Integer idSector;
    private String nombreSector;

    @NotNull
    private Integer idEstadoU;
    private String nombreEstado;

    @NotNull
    private Integer idCargo;
    private String nombreCargo;


}


