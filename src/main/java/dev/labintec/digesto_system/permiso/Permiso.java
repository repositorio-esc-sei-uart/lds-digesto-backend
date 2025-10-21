/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.permiso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


/**
 * Entidad que representa un permiso dentro del sistema.
 * Contiene información básica como el nombre y la descripción del permiso.
 * 
 * Mapeada a la tabla "permiso" en la base de datos.
 * 
 */
@Entity
@Table(name = "permiso")
@Data

public class Permiso {
    
    /**
     * Identificador único del permiso.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpermiso")   
    private Integer idPermiso;
    
    /**
     * Nombre del permiso.
     * Debe ser único y no nulo.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Descripción breve del permiso.
     */
    @Column(name = "descripcion")
    private String descripcion;


}