/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.estado.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author micae
 */

@Entity
@Table(name = "estado")
@Getter
@Setter
@NoArgsConstructor
public class Estado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstado") // Buena práctica
    private Integer idEstado;

    // Script dice UNIQUE NULL
    @Column(name = "nombre", length = 45, unique = true, nullable = true) // nullable=true porque el script dice NULL
    private String nombre;

    // Script dice VARCHAR(60) NULL
    @Column(name = "descripcion", length = 60) 
    private String descripcion;

}
