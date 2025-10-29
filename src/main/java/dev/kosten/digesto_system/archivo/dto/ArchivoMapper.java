/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.archivo.dto;

import dev.kosten.digesto_system.archivo.entity.Archivo;

/**
 *
 * @author micae
 */
public class ArchivoMapper {
    
    public static ArchivoDTO toDTO(Archivo entity) {
        if (entity == null) {
            return null;
        }
        ArchivoDTO dto = new ArchivoDTO();
        dto.setIdArchivo(entity.getIdArchivo());
        dto.setNombre(entity.getNombre());
        dto.setUrl(entity.getUrl());
        return dto;
    }

    public static Archivo toEntity(ArchivoDTO dto) {
        if (dto == null) {
            return null;
        }
        Archivo entity = new Archivo();
        entity.setIdArchivo(dto.getIdArchivo());
        entity.setNombre(dto.getNombre());
        entity.setUrl(dto.getUrl());
        return entity;
    }
}
