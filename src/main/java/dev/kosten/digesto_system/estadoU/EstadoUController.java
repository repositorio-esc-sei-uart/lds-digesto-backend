/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.estadoU;

import dev.kosten.digesto_system.estadoU.EstadoUDTO;
import dev.kosten.digesto_system.estadoU.EstadoUMapper;
import dev.kosten.digesto_system.estadoU.EstadoU;
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
 * Controlador REST que gestiona los estados de usuario ({@link EstadoU}).
 * 
 * Expone endpoints para listar, crear y eliminar estados de usuario.
 * Utiliza {@link EstadoUService} para la lógica de negocio y la conversión a DTOs.
 * 
 * La URL base para los endpoints es "/api/v1/estadosU".
 * 
 */
@RestController
@RequestMapping("/api/v1/estadosU")
public class EstadoUController {


    @Autowired
    private EstadoUService servicio;

    /**
     * Lista todos los estados de usuario.
     * 
     * @return lista de {@link EstadoU} existentes
     */
    @GetMapping
    public List<EstadoU> listar() {
        return servicio.listarEstados();
    }
    
    /**
     * Crea un nuevo estado de usuario.
     * 
     * @param dto objeto {@link EstadoUDTO} con los datos del nuevo estado
     * @return {@link EstadoUDTO} creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoUDTO crear(@Valid @RequestBody EstadoUDTO dto) {
        return servicio.crearEstado(dto);
    }
    
    /**
     * Elimina un estado de usuario por su identificador.
     * 
     * @param id identificador del estado a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPorId(@PathVariable Integer id){
        servicio.eliminarPorID(id);
    }
    
}
