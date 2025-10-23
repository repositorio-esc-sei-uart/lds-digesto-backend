/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.estadoU;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa los posibles estados de un {@link Usuario} en el sistema.
 * 
 * Contiene un identificador único, un nombre y una descripción opcional.
 * Se mapea a la tabla "estadoU" en la base de datos.
 * 
 */
@Entity
@Table(name = "estadoU")
@Getter
@Setter


public class EstadoU {
    
    /**
     * Identificador único del estado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoU")
    private Integer idEstadoU;
    
    /**
     * Nombre del estado. Debe ser único y no nulo.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Descripción opcional del estado.
     */
    @Column(name = "descripcion")
    private String descripcion;
}


