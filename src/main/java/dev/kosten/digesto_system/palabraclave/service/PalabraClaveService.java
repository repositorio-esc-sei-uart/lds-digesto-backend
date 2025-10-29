/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.palabraclave.service;

import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import dev.kosten.digesto_system.palabraclave.repository.PalabraClaveRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author micae
 */

@Service
public class PalabraClaveService {
    @Autowired
    private PalabraClaveRepository palabraClaveRepository;

    public List<PalabraClave> listarTodos() {
        return palabraClaveRepository.findAll();
    }

    public PalabraClave obtenerPorId(Integer id) {
        return palabraClaveRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("PalabraClave no encontrada con ID: " + id));
    }

    public PalabraClave crearPalabraClave(PalabraClave palabraClave) {
        // Validamos que el nombre no esté repetido usando el método del repositorio
        Optional<PalabraClave> existente = palabraClaveRepository.findByNombre(palabraClave.getNombre());
        if (existente.isPresent()) {
            throw new RecursoDuplicadoException("Ya existe una palabra clave con el nombre: " + palabraClave.getNombre());
        }
        
        return palabraClaveRepository.save(palabraClave);
    }

    public PalabraClave actualizarPalabraClave(Integer id, PalabraClave datosNuevos) {
        PalabraClave existente = obtenerPorId(id);
        
        // Opcional: validar que el nuevo nombre no choque con otro existente
        Optional<PalabraClave> otroConMismoNombre = palabraClaveRepository.findByNombre(datosNuevos.getNombre());
        if (otroConMismoNombre.isPresent() && !otroConMismoNombre.get().getIdPalabraClave().equals(id)) {
            throw new RecursoDuplicadoException("El nombre '" + datosNuevos.getNombre() + "' ya está en uso por otra palabra clave.");
        }

        existente.setNombre(datosNuevos.getNombre());
        existente.setDescripcion(datosNuevos.getDescripcion());
        
        return palabraClaveRepository.save(existente);
    }

    public void eliminarPalabraClave(Integer id) {
        PalabraClave existente = obtenerPorId(id);
        // Ojo: Deberías validar que esta palabra clave no esté siendo usada por ningún documento
        // antes de borrarla, o manejar la eliminación en cascada.
        palabraClaveRepository.delete(existente);
    }
}
