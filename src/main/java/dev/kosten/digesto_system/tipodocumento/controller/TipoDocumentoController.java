package dev.kosten.digesto_system.tipodocumento.controller;

import dev.kosten.digesto_system.tipodocumento.dto.TipoDocumentoDTO;
import dev.kosten.digesto_system.tipodocumento.dto.TipoDocumentoMapper;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import dev.kosten.digesto_system.tipodocumento.service.TipoDocumentoService;
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
 * @author micael
 * @author Quique
 */
@RestController
@RequestMapping("/api/v1/tipos-documento")
public class TipoDocumentoController {
    @Autowired
    private TipoDocumentoService tipoDocumentoService;
    private TipoDocumentoMapper tipoDocumentoMapper;

    @GetMapping
    public List<TipoDocumentoDTO> obtenerTodos() {
        return tipoDocumentoService.listarTodos().stream()
                .map(tipoDocumentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> obtenerPorId(@PathVariable Integer id) {
        TipoDocumento tipo = tipoDocumentoService.obtenerPorId(id);
        return ResponseEntity.ok(tipoDocumentoMapper.toDTO(tipo));
    }

    @PostMapping
    public ResponseEntity<TipoDocumentoDTO> crear(@RequestBody TipoDocumentoDTO dto) {
        TipoDocumento tipoParaGuardar = tipoDocumentoMapper.toEntity(dto);
        TipoDocumento tipoGuardado = tipoDocumentoService.crearTipoDocumento(tipoParaGuardar);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoDocumentoMapper.toDTO(tipoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> actualizar(@PathVariable Integer id, @RequestBody TipoDocumentoDTO dto) {
        TipoDocumento datosNuevos = tipoDocumentoMapper.toEntity(dto);
        TipoDocumento tipoActualizado = tipoDocumentoService.actualizarTipoDocumento(id, datosNuevos);
        return ResponseEntity.ok(tipoDocumentoMapper.toDTO(tipoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoDocumentoService.eliminarTipoDocumento(id);
        return ResponseEntity.noContent().build();
    }
}
