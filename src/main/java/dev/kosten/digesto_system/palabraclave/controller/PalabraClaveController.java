
package dev.kosten.digesto_system.palabraclave.controller;

import dev.kosten.digesto_system.palabraclave.dto.PalabraClaveDTO;
import dev.kosten.digesto_system.palabraclave.dto.PalabraClaveMapper;
import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import dev.kosten.digesto_system.palabraclave.service.PalabraClaveService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/palabras-clave")
@RequiredArgsConstructor
public class PalabraClaveController {
    private final PalabraClaveService palabraClaveService;
    private final PalabraClaveMapper palabraClaveMapper;

    @GetMapping
    public List<PalabraClaveDTO> obtenerTodos() {
        return palabraClaveService.listarTodos().stream()
                .map(palabraClaveMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PalabraClaveDTO> obtenerPorId(@PathVariable Integer id) {
        PalabraClave palabraClave = palabraClaveService.obtenerPorId(id);
        return ResponseEntity.ok(palabraClaveMapper.toDTO(palabraClave));
    }

    @PostMapping
    public ResponseEntity<PalabraClaveDTO> crear(@RequestBody PalabraClaveDTO dto) {
        PalabraClave palabraClaveParaGuardar = palabraClaveMapper.toEntity(dto);
        PalabraClave palabraClaveGuardada = palabraClaveService.crearPalabraClave(palabraClaveParaGuardar);
        return ResponseEntity.status(HttpStatus.CREATED).body(palabraClaveMapper.toDTO(palabraClaveGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PalabraClaveDTO> actualizar(@PathVariable Integer id, @RequestBody PalabraClaveDTO dto) {
        PalabraClave datosNuevos = palabraClaveMapper.toEntity(dto);
        PalabraClave palabraClaveActualizada = palabraClaveService.actualizarPalabraClave(id, datosNuevos);
        return ResponseEntity.ok(palabraClaveMapper.toDTO(palabraClaveActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        palabraClaveService.eliminarPalabraClave(id);
        return ResponseEntity.noContent().build();
    }
}
