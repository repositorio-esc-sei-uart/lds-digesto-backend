/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.rol;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un rol dentro del sistema.
 * Contiene información básica como el nombre y la descripción del rol.
 * 
 * Mapeada a la tabla "rol" en la base de datos.
 * 
 */

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol {
    
    /**
     * Identificador único del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRol")   // coincide con tu tabla
    private Integer idRol;
    
    /**
     * Nombre del rol.
     * Debe ser único y no nulo.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Descripción breve del rol.
     */
    @Column(name = "descripcion")
    private String descripcion;


}

