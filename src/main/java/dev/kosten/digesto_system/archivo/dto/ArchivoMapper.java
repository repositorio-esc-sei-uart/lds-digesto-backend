package dev.kosten.digesto_system.archivo.dto;

import dev.kosten.digesto_system.archivo.entity.Archivo;
import org.springframework.stereotype.Component;

/**
 * Mapper estático para conversiones entre Archivo (Entidad) y ArchivoDTO (DTO).
 * Utiliza el patrón Builder para una instanciación de objetos limpia y fluida.
 * @author micael
 * @author Quique
 */
@Component
public class ArchivoMapper {

    /**
     * Convierte una entidad Archivo a su DTO correspondiente.
     * @param entity La entidad de la base de datos.
     * @return Un ArchivoDTO con los datos transferibles.
     */
    public ArchivoDTO toDTO(Archivo entity) {
        if (entity == null) {
            return null;
        }
        return ArchivoDTO.builder()
            .idArchivo(entity.getIdArchivo())
            .nombre(entity.getNombre())
            .url(entity.getUrl())
            .build();
    }

    /**
     * Convierte un ArchivoDTO a una entidad Archivo.
     * @param dto El objeto de transferencia de datos.
     * @return Una entidad Archivo lista para persistir.
     */
    public Archivo toEntity(ArchivoDTO dto) {
        if (dto == null) {
            return null;
        }
        return Archivo.builder()
            .idArchivo(dto.getIdArchivo())
            .nombre(dto.getNombre())
            .url(dto.getUrl())
            .build();
    }
}
