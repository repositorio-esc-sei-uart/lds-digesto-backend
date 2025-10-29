/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.tipodocumento.service;

import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import dev.kosten.digesto_system.tipodocumento.repository.TipoDocumentoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author micae
 */

@Service
public class TipoDocumentoService {
    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    public List<TipoDocumento> listarTodos() {
        return tipoDocumentoRepository.findAll();
    }
    
    public TipoDocumento obtenerPorId(Integer id) {
        return tipoDocumentoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("TipoDocumento no encontrado con ID: " + id));
    }

    public TipoDocumento crearTipoDocumento(TipoDocumento tipoDocumento) {
        // Aquí podrías agregar validaciones (ej. que el nombre no esté repetido)
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    public TipoDocumento actualizarTipoDocumento(Integer id, TipoDocumento datosNuevos) {
        // 1. Asegurarse de que existe
        TipoDocumento tipoExistente = obtenerPorId(id);
        
        // 2. Actualizar los campos
        tipoExistente.setNombre(datosNuevos.getNombre());
        tipoExistente.setDescripcion(datosNuevos.getDescripcion());
        
        // 3. Guardar
        return tipoDocumentoRepository.save(tipoExistente);
    }

    public void eliminarTipoDocumento(Integer id) {
        // 1. Asegurarse de que existe
        TipoDocumento tipoExistente = obtenerPorId(id);
        
        // 2. Eliminar
        tipoDocumentoRepository.delete(tipoExistente);
    }
}
