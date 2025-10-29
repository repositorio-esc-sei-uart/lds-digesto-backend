/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.asigna;

import dev.kosten.digesto_system.asigna.AsignaDTO;
import dev.kosten.digesto_system.asigna.Asigna;
import dev.kosten.digesto_system.asigna.AsignaId;
import dev.kosten.digesto_system.permiso.Permiso;
import dev.kosten.digesto_system.rol.Rol;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre la entidad {@link Asigna} y su
 * correspondiente objeto de transferencia de datos {@link AsignaDTO}.
 * 
 * Proporciona métodos para convertir de entidad a DTO y viceversa,
 * permitiendo separar la capa de persistencia de la capa de presentación.
 * 
 * Permite enriquecer el DTO con información adicional como los nombres
 * del rol y del permiso asociados.
 * 
 */
@Component
public class AsignaMapper {
    
    /**
     * Convierte una entidad {@link Asigna} en un {@link AsignaDTO}.
     * 
     * @param asigna entidad a convertir
     * @return objeto {@link AsignaDTO} resultante
     */
    public AsignaDTO toDTO(Asigna asigna) {
        AsignaDTO dto = new AsignaDTO();
        dto.setIdRol(asigna.getRol().getIdRol());
        dto.setIdPermiso(asigna.getPermiso().getIdPermiso());

        // Opcional: enriquecer con nombres
        dto.setNombreRol(asigna.getRol().getNombre());
        dto.setNombrePermiso(asigna.getPermiso().getNombre());

        return dto;
    }
    
    /**
     * Convierte un {@link AsignaDTO} en una entidad {@link Asigna}.
     * 
     * @param dto objeto de transferencia de datos
     * @param rol entidad {@link Rol} asociada
     * @param permiso entidad {@link Permiso} asociada
     * @return entidad {@link Asigna} resultante
     */
    public Asigna toEntity(AsignaDTO dto, Rol rol, Permiso permiso) {
        Asigna asigna = new Asigna();
        asigna.setId(new AsignaId(dto.getIdRol(), dto.getIdPermiso()));
        asigna.setRol(rol);
        asigna.setPermiso(permiso);
        return asigna;
    }
    
}

