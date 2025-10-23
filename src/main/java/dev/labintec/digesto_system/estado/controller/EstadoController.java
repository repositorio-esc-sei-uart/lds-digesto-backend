/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.estado.controller;

import dev.labintec.digesto_system.estado.dto.EstadoDTO;
import dev.labintec.digesto_system.estado.dto.EstadoMapper;
import dev.labintec.digesto_system.estado.entity.Estado;
import dev.labintec.digesto_system.estado.service.EstadoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author micae
 */

@RestController
@RequestMapping("/api/estados")
public class EstadoController {
    
    @Autowired
    private EstadoService estadoService;

    @GetMapping
    public List<EstadoDTO> obtenerTodos() {
        return estadoService.listarTodos().stream()
                .map(EstadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> obtenerPorId(@PathVariable Integer id) {
        Estado estado = estadoService.obtenerPorId(id);
        return ResponseEntity.ok(EstadoMapper.toDTO(estado));
    }

    @PostMapping
    public ResponseEntity<EstadoDTO> crear(@RequestBody EstadoDTO dto) {
        Estado estadoParaGuardar = EstadoMapper.toEntity(dto);
        Estado estadoGuardado = estadoService.crearEstado(estadoParaGuardar);
        return ResponseEntity.status(HttpStatus.CREATED).body(EstadoMapper.toDTO(estadoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> actualizar(@PathVariable Integer id, @RequestBody EstadoDTO dto) {
        Estado datosNuevos = EstadoMapper.toEntity(dto);
        Estado estadoActualizado = estadoService.actualizarEstado(id, datosNuevos);
        return ResponseEntity.ok(EstadoMapper.toDTO(estadoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        estadoService.eliminarEstado(id);
        return ResponseEntity.noContent().build();
    }
}
