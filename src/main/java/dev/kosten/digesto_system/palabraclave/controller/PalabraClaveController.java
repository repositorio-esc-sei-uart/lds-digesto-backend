/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.palabraclave.controller;

import dev.kosten.digesto_system.palabraclave.dto.PalabraClaveDTO;
import dev.kosten.digesto_system.palabraclave.dto.PalabraClaveMapper;
import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import dev.kosten.digesto_system.palabraclave.service.PalabraClaveService;
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
@RequestMapping("/api/v1/palabras-clave")
public class PalabraClaveController {
    @Autowired
    private PalabraClaveService palabraClaveService;

    @GetMapping
    public List<PalabraClaveDTO> obtenerTodos() {
        return palabraClaveService.listarTodos().stream()
                .map(PalabraClaveMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PalabraClaveDTO> obtenerPorId(@PathVariable Integer id) {
        PalabraClave palabraClave = palabraClaveService.obtenerPorId(id);
        return ResponseEntity.ok(PalabraClaveMapper.toDTO(palabraClave));
    }

    @PostMapping
    public ResponseEntity<PalabraClaveDTO> crear(@RequestBody PalabraClaveDTO dto) {
        PalabraClave palabraClaveParaGuardar = PalabraClaveMapper.toEntity(dto);
        PalabraClave palabraClaveGuardada = palabraClaveService.crearPalabraClave(palabraClaveParaGuardar);
        return ResponseEntity.status(HttpStatus.CREATED).body(PalabraClaveMapper.toDTO(palabraClaveGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PalabraClaveDTO> actualizar(@PathVariable Integer id, @RequestBody PalabraClaveDTO dto) {
        PalabraClave datosNuevos = PalabraClaveMapper.toEntity(dto);
        PalabraClave palabraClaveActualizada = palabraClaveService.actualizarPalabraClave(id, datosNuevos);
        return ResponseEntity.ok(PalabraClaveMapper.toDTO(palabraClaveActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        palabraClaveService.eliminarPalabraClave(id);
        return ResponseEntity.noContent().build();
    }
}
