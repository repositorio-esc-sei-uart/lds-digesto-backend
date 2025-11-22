/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.unidadEjecutora;

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
 * Controlador REST para el recurso {@link UnidadEjecutora}.
 * 
 * Expone endpoints para gestionar los unidadEjecutora a través de la API,
 * incluyendo operaciones de listado, creación, obtención por ID y eliminación.
 * 
 * Utiliza {@link unidadEjecutoractorService} para la lógica de negocio
 * y {@link unidadEjecutoraMapper} para la conversión entre entidades y DTOs.
 * @author Matias
 */
@RestController
@RequestMapping("/api/v1/unidadEjecutora")
public class UnidadEjecutoraController {

    @Autowired
    private UnidadEjecutoraService servicio;


    /**
     * Lista todos los unidadEjecutoras registrados.
     * 
     * @return lista de objetos {@link unidadEjecutoraDTO}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)        
    public List<UnidadEjecutoraDTO> listarunidadEjecutoras() {
        List<UnidadEjecutora> unidadEjecutoras = servicio.listarUnidadEjecutoras();
        List<UnidadEjecutoraDTO> dtos = new ArrayList<>();
        
        for(UnidadEjecutora unidadEjecutora : unidadEjecutoras){
            dtos.add(UnidadEjecutoraMapper.toDTO(unidadEjecutora));
        }
        
        return dtos;
    }
    
    /**
     * Crea un nueva unidadEjecutora.
     * 
     * @param dto objeto {@link UnidadEjecutoraDTO} con los datos de la nueva unidadEjecutora
     * @return el {@link UnidadEjecutoraDTO} creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadEjecutoraDTO crear(@Valid @RequestBody UnidadEjecutoraDTO dto) {
        UnidadEjecutora nuevaUnidadEjecutora = servicio.crearUnidadEjecutora(UnidadEjecutoraMapper.toEntity(dto));
        
        return UnidadEjecutoraMapper.toDTO(nuevaUnidadEjecutora);
    }
    
    /**
     * Obtiene un unidadEjecutora según su identificador.
     * 
     * @param id identificador de la unidadEjecutora
     * @return el {@link UnidadEjecutoraDTO} correspondiente
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Código 200 si la operación fue exitosa
    public UnidadEjecutoraDTO obtenerPorId(@PathVariable Integer id) {
        UnidadEjecutora unidadEjecutora= servicio.obtenerPorId(id);
        
        return UnidadEjecutoraMapper.toDTO(unidadEjecutora); 
    }
    
    /**
     * Elimina una unidadEjecutora según su identificador.
     * 
     * @param id identificador de la unidadEjecutora a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarPorID(@PathVariable Integer id){
        UnidadEjecutora unidadEjecutora = servicio.obtenerPorId(id);
        
        servicio.eliminarPorID(id);
    }
    
}


