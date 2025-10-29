/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.sector;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;



/**
 * Entidad que representa un sector dentro del sistema.
 * Contiene la información básica del sector, como su nombre y descripción.
 * 
 * Mapeada a la tabla "sector" en la base de datos.
 * 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "sector")

public class Sector {
    
    /**
     * Identificador único del sector.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSector")
    private Integer idSector;
    
    /**
     * Nombre del sector.
     * Debe ser único y no nulo.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Descripción breve del sector.
     */
    @Column(name = "descripcion")
    private String descripcion;
    
}

