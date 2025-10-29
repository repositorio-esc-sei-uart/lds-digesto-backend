/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.sector;

import dev.kosten.digesto_system.sector.SectorDTO;
import dev.kosten.digesto_system.sector.SectorMapper;
import dev.kosten.digesto_system.sector.SectorService;
import dev.kosten.digesto_system.sector.Sector;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para el recurso {@link Sector}.
 * 
 * Expone endpoints para gestionar los sectores a través de la API,
 * incluyendo operaciones de listado, creación, obtención por ID y eliminación.
 * 
 * Utiliza {@link SectorService} para la lógica de negocio
 * y {@link SectorMapper} para la conversión entre entidades y DTOs.
 * 
 */
@RestController
@RequestMapping("/api/v1/sectores")
public class SectorController {

    @Autowired
    private SectorService servicio;


    /**
     * Lista todos los sectores registrados.
     * 
     * @return lista de objetos {@link SectorDTO}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)        
    public List<SectorDTO> listarSectores() {
        List<Sector> sectores = servicio.listarSectores();
        List<SectorDTO> dtos = new ArrayList();
        
        for(Sector sector : sectores){
            dtos.add(SectorMapper.toDTO(sector));
        }
        
        return dtos;
    }
    
    /**
     * Crea un nuevo sector.
     * 
     * @param dto objeto {@link SectorDTO} con los datos del nuevo sector
     * @return el {@link SectorDTO} creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SectorDTO crear(@Valid @RequestBody SectorDTO dto) {
        Sector nuevoSector = servicio.crearSector(SectorMapper.toEntity(dto));
        
        return SectorMapper.toDTO(nuevoSector);
    }
    
    /**
     * Obtiene un sector según su identificador.
     * 
     * @param id identificador del sector
     * @return el {@link SectorDTO} correspondiente
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Código 200 si la operación fue exitosa
    public SectorDTO obtenerPorId(@PathVariable Integer id) {
        Sector sector= servicio.obtenerPorId(id);
        
        return SectorMapper.toDTO(sector); 
    }
    
    /**
     * Elimina un sector según su identificador.
     * 
     * @param id identificador del sector a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarPorID(@PathVariable Integer id){
        Sector sector = servicio.obtenerPorId(id);
        
        servicio.eliminarPorID(id);
    }
    
}


