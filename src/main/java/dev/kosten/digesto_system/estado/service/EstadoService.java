/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.estado.service;

import dev.kosten.digesto_system.estado.entity.Estado;
import dev.kosten.digesto_system.estado.repository.EstadoRepository;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author micae
 */
@Service
public class EstadoService {
    
    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> listarTodos() {
        return estadoRepository.findAll();
    }

    public Estado obtenerPorId(Integer id) {
        return estadoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));
    }

    public Estado crearEstado(Estado estado) {
        return estadoRepository.save(estado);
    }

    public Estado actualizarEstado(Integer id, Estado datosNuevos) {
        Estado estadoExistente = obtenerPorId(id);
        
        estadoExistente.setNombre(datosNuevos.getNombre());
        estadoExistente.setDescripcion(datosNuevos.getDescripcion());
        
        return estadoRepository.save(estadoExistente);
    }

    public void eliminarEstado(Integer id) {
        Estado estadoExistente = obtenerPorId(id);
        estadoRepository.delete(estadoExistente);
    }
}
