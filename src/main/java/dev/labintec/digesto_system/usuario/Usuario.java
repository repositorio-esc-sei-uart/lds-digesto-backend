/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.usuario;

import dev.labintec.digesto_system.cargo.Cargo;
import dev.labintec.digesto_system.estadoU.EstadoU;
import dev.labintec.digesto_system.rol.Rol;
import dev.labintec.digesto_system.sector.Sector;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Representa un usuario dentro del sistema. 
 * Contiene información personal, de acceso y relaciones con otras entidades 
 * como Rol, Sector, EstadoU y Cargo.
 * 
 * La clase está mapeada a la tabla "usuario" de la base de datos.
 * 
 */

@Entity
@Table(name = "usuario")
@Data       
@NoArgsConstructor      // Lombok: genera un constructor vacío
@RequiredArgsConstructor// Lombok: genera un constructor con los campos marcados como @NonNull 

public class Usuario {
    
    
    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;
    
    
    /**
     * Número de documento del usuario.
     */
    @NonNull 
    @Column(name = "dni")
    private Integer dni;
    
    
    /**
     * Correo electrónico del usuario.
     */
    @NonNull
    @Column(name = "email")
    @Email
    private String email;
    
    
    /**
     * Contraseña cifrada del usuario.
     */
    @NonNull
    @Column(name = "password", nullable = false)
    private String password;
    
    
    /**
     * Nombre del usuario.
     */
    @NotBlank
    @Column(name = "nombre")
    private String nombre;
    
    
    /**
     * Apellido del usuario.
     */
    @NonNull
    @Column(name = "apellido")
    private String apellido;
    
    
    /**
     * Número de legajo institucional del usuario.
     */
    @NonNull
    @Column(name = "legajo")
    private Integer legajo;
    
    
    /**
     * Rol asignado al usuario (por ejemplo: administrador, editor, etc.).
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_idRol")
    private Rol rol;
    
    
    /**
     * Sector al que pertenece el usuario dentro de la institución.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "sector_idSector")
    private Sector sector;
    
    
    /**
     * Estado actual del usuario (activo, inactivo, suspendido, etc.).
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "estadoU_idEstadoU")
    private EstadoU estado;
    
    
    /**
     * Cargo o puesto del usuario dentro de la institución.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "cargo_idCargo")
    private Cargo cargo;


}

