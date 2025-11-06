package dev.kosten.digesto_system.registro.service;

import dev.kosten.digesto_system.log.LogService;
import dev.kosten.digesto_system.registro.dto.RegistroDTO;
import dev.kosten.digesto_system.registro.dto.RegistroMapper;
import dev.kosten.digesto_system.registro.repository.RegistroRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para la entidad Registro (Auditoría).
 * Encapsula la lógica de LECTURA de los registros de auditoría.
 * @author Quique
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final RegistroMapper registroMapper;
    private final LogService logService; 

    /**
     * Obtiene la lista completa de todos los registros de auditoría.
     * @return Lista de {@link RegistroDTO}.
     */
    public List<RegistroDTO> listarTodos() {
        logService.info("Solicitud para listar todo el historial de auditoría.");
        return registroRepository.findAll()
                .stream()
                .map(registroMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el historial de registros de un documento específico.
     * @param idDocumento El ID del documento a consultar.
     * @return Lista de {@link RegistroDTO}.
     */
    public List<RegistroDTO> obtenerHistorialPorDocumento(Integer idDocumento) {
        logService.info("Buscando historial para documento ID: " + idDocumento);
        return registroRepository.findByDocumentoIdDocumento(idDocumento)
                .stream()
                .map(registroMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el historial de registros de un usuario específico.
     * @param idUsuario El ID del usuario a consultar.
     * @return Lista de {@link RegistroDTO}.
     */
    public List<RegistroDTO> obtenerHistorialPorUsuario(Integer idUsuario) {
        logService.info("Buscando historial para usuario ID: " + idUsuario);
        return registroRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(registroMapper::toDTO)
                .collect(Collectors.toList());
    }
}
