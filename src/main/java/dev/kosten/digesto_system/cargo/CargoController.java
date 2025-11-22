/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.cargo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * Controlador REST para el recurso {@link Cargo}.
 * 
 * Expone endpoints para gestionar los cargos a través de la API,
 * incluyendo operaciones de listado, creación y eliminación.
 * 
 * Utiliza {@link CargoService} para la lógica de negocio
 * y {@link CargoMapper} para la conversión entre entidades y DTOs.
 * 
 */

@RestController
@RequestMapping("/api/v1/cargos")
public class CargoController {

    @Autowired
    private CargoService servicio;


    /**
     * Lista todos los cargos registrados.
     * 
     * @return lista de objetos {@link CargoDTO}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CargoDTO> listarCargos() {
        List<Cargo> cargos = servicio.listarCargos();
        List<CargoDTO> dtos = new ArrayList<>();
        
        for(Cargo cargo : cargos){
            dtos.add(CargoMapper.toDTO(cargo));
        }
        
        return dtos;
    }
    
    /**
     * Crea un nuevo cargo.
     * 
     * @param dto objeto {@link CargoDTO} con los datos del nuevo cargo
     * @return el {@link CargoDTO} creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CargoDTO crearCargo(@Valid @RequestBody CargoDTO dto) {
        Cargo nuevoCargo = servicio.crearCargo(CargoMapper.toEntity(dto));
        
        return CargoMapper.toDTO(nuevoCargo);
    }
    
    /**
     * Elimina un cargo según su identificador.
     * 
     * @param id identificador del cargo a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarPorID(@PathVariable Integer id){
        Cargo cargo = servicio.obtenerPorId(id);
        
        servicio.eliminarPorID(id);
    }
    
}

