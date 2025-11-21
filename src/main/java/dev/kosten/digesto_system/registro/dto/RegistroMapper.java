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

    /**
     * Convierte una entidad Registro a su DTO.
     * @param entity La entidad de la base de datos.
     * @return Un RegistroDTO con los datos transferibles.
     */
    public RegistroDTO toDTO(Registro entity) {
        if (entity == null) {
            return null;
        }

        String nombreUsr = "N/A";
        String legajoUsr = "N/A";
        String numDoc = "N/A";
        String tituloDoc = "N/A";

        if (entity.getUsuarioResponsable()!= null) {
            nombreUsr = entity.getUsuarioResponsable().getNombre() + " " + entity.getUsuarioResponsable().getApellido();
            legajoUsr = entity.getUsuarioResponsable().getLegajo();
        }
        
        if (entity.getDocumentoAfectado()!= null) {
            numDoc = entity.getDocumentoAfectado().getNumDocumento();
            tituloDoc = entity.getDocumentoAfectado().getTitulo();
        }

        return RegistroDTO.builder()
            .idRegistro(entity.getIdRegistro())
            .fechaCarga(entity.getFechaCarga())
            .tipoOperacion(entity.getTipoOperacion())
            .nombreUsuarioResponsable(nombreUsr)
            .legajoUsuarioResponsable(legajoUsr)
            .numDocumentoAfectado(numDoc)
            .tituloDocumentoAfectado(tituloDoc)
            .build();
    }

    /**
     * Convierte un RegistroDTO y sus entidades en una entidad Registro.
     * Utilizado internamente por los servicios.
     * @param dto El DTO con los datos simples.
     * @param usuario La entidad Usuario completa.
     * @param documento La entidad Documento completa.
     * @return Una entidad Registro lista para persistir.
     */
    public Registro toEntity(RegistroDTO dto, Usuario usuario, Documento documento) {
        if (dto == null) {
            return null;
        }
        
        return Registro.builder()
                .idRegistro(dto.getIdRegistro()) // Generalmente null al crear
                .fechaCarga(dto.getFechaCarga())
                .tipoOperacion(dto.getTipoOperacion())
                .usuarioResponsable(usuario) // Asigna la entidad completa
                .documentoAfectado(documento) // Asigna la entidad completa
                .build();
    }
}
