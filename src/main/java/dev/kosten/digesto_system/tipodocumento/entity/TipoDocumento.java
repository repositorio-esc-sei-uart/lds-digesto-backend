/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.tipodocumento.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author micae
 */

@Entity
@Table(name = "tipoDocumento")
@Getter
@Setter
@NoArgsConstructor
public class TipoDocumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipoDocumento") // Buena práctica
    private Integer idTipoDocumento;

    // Script dice UNIQUE NULL
    // Corregir 'anulable = falso' a 'nullable = true' si el script dice NULL
    @Column(name = "nombre", unique = true, length = 45, nullable = true) 
    private String nombre;

    // Script dice VARCHAR(60) NULL
    @Column(name = "descripcion", length = 60) 
    private String descripcion;
}
