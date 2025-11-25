package dev.kosten.digesto_system.registro.dto;

import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.registro.entity.Registro;
import dev.kosten.digesto_system.usuario.Usuario;
import org.springframework.stereotype.Component;

/**
 * Componente Mapper para conversiones entre la entidad Registro
 * y su DTO (RegistroDTO).
 * @author Quique
 */
@Component
public class RegistroMapper {

    public RegistroDTO toDTO(Registro entity) {
        if (entity == null) {
            return null;
        }

        // Valores por defecto (para evitar nulls en el front)
        String nombreUsrResp = "N/A";
        String legajoUsrResp = "";
        String numDoc = null; // Lo dejamos null para usar @if en el front
        String tituloDoc = "";
        String nombreUsrAfectado = null; // Lo dejamos null para usar @if en el front
        String legajoUsrAfectado = "";

        // 1. Mapear Responsable (Quién lo hizo)
        if (entity.getUsuarioResponsable() != null) {
            nombreUsrResp = entity.getUsuarioResponsable().getNombre() + " " + entity.getUsuarioResponsable().getApellido();
            legajoUsrResp = entity.getUsuarioResponsable().getLegajo();
        }

        // 2. Mapear Documento Afectado (Si existe)
        if (entity.getDocumentoAfectado() != null) {
            numDoc = entity.getDocumentoAfectado().getNumDocumento();
            tituloDoc = entity.getDocumentoAfectado().getTitulo();
        }

        // 3. Mapear Usuario Afectado (Si existe)
        if (entity.getUsuarioAfectado() != null) {
            nombreUsrAfectado = entity.getUsuarioAfectado().getNombre() + " " + entity.getUsuarioAfectado().getApellido();
            legajoUsrAfectado = entity.getUsuarioAfectado().getLegajo();
        }

        return RegistroDTO.builder()
                .idRegistro(entity.getIdRegistro())
                .fechaCarga(entity.getFechaCarga())
                .tipoOperacion(entity.getTipoOperacion())
                // Campos Planos
                .nombreUsuarioResponsable(nombreUsrResp)
                .legajoUsuarioResponsable(legajoUsrResp)
                .numDocumentoAfectado(numDoc)
                .tituloDocumentoAfectado(tituloDoc)
                .nombreUsuarioAfectado(nombreUsrAfectado)
                .legajoUsuarioAfectado(legajoUsrAfectado)
                .build();
    }

    // Este método es para cuando creas el registro desde la entidad (no suele cambiar)
    public Registro toEntity(RegistroDTO dto, Usuario usuario, Documento documento) {
        if (dto == null) return null;
        return Registro.builder()
                .idRegistro(dto.getIdRegistro())
                .fechaCarga(dto.getFechaCarga())
                .tipoOperacion(dto.getTipoOperacion())
                .usuarioResponsable(usuario)
                .documentoAfectado(documento)
                .build();
    }
}