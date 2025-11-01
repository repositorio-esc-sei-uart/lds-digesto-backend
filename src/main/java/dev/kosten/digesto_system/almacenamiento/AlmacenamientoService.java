package dev.kosten.digesto_system.almacenamiento;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio dedicado a manejar las operaciones físicas de guardado y borrado
 * de archivos en el sistema de archivos del servidor.
 * Esta clase actúa como una abstracción del disco duro.
 * @author Quique
 */
@Service
public class AlmacenamientoService {
    // Define la carpeta raíz (en la raíz de tu proyecto) donde se guardarán los archivos.
    private final Path rootLocation;

    /**
     * Constructor del servicio.
     * Inyecta la propiedad 'storage.location' desde application.properties.
     * @param storagePath La ruta leída desde el archivo de propiedades.
    */
    public AlmacenamientoService(@Value("${storage.location}") String storagePath) {
        // Resuelve la ruta al iniciar el servicio
        this.rootLocation = Paths.get(storagePath);
    }

    /**
     * Método que se ejecuta automáticamente al iniciar el servicio.
     * Se encarga de crear el directorio 'uploads' si este no existe.
     * @throws RuntimeException si no se puede crear el directorio.
     */
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error: No se pudo inicializar el directorio de subida de archivos.", e);
        }
    }

    /**
     * Guarda un archivo físico en el disco.
     * @param file El archivo binario (MultipartFile) recibido.
     * @param urlRelativa La ruta *relativa* donde se guardará (ej. "documentos/1/anexo.pdf").
     * @throws RuntimeException si el archivo está vacío, tiene un nombre inválido o falla la copia.
     */
    public void save(MultipartFile file, String urlRelativa) {
    try {
        if (file.isEmpty()) {
            throw new RuntimeException("Falló al guardar un archivo vacío.");
        }

    // Resuelve la ruta de destino final 
    Path destinoFinal = this.rootLocation.resolve(urlRelativa).toAbsolutePath();

    // Medida de seguridad: previene ataques de "directory traversal" (ej. ../../etc/passwd)
    if (!destinoFinal.startsWith(this.rootLocation.toAbsolutePath())) {
        throw new RuntimeException("No se puede guardar el archivo fuera del directorio raíz.");
    }

    // Crea las carpetas padres si no existen (ej. /documentos/1/)
    Files.createDirectories(destinoFinal.getParent());

    // Copia el contenido del archivo al destino, reemplazando si ya existe.
    try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinoFinal, StandardCopyOption.REPLACE_EXISTING);
    }
        } catch (IOException e) {
            throw new RuntimeException("Falló al guardar el archivo: " + urlRelativa, e);
        }
    }

    /**
     *
     * @param rutaRelativa
     * @return
     */
    public Resource loadAsResource(String rutaRelativa) {
        try {
            Path file = this.rootLocation.resolve(rutaRelativa).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo: " + rutaRelativa);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error en la URL del archivo: " + rutaRelativa, e);
        }
    }

    /**
     * Borra un archivo físico del disco duro.
     * @param urlRelativa La ruta del archivo a borrar (ej. "documentos/1/anexo.pdf").
     */
    public void delete(String urlRelativa) {
        if (urlRelativa == null || urlRelativa.isBlank()) {
            return; // No hay nada que borrar.
        }

        try {
            Path archivo = this.rootLocation.resolve(urlRelativa).toAbsolutePath();
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            // A diferencia del guardado, si no se puede borrar, solo registramos un warning
            // pero no detenemos la aplicación (podría ser un archivo huérfano).
            System.err.println("No se pudo borrar el archivo: " + urlRelativa + ". Causa: " + e.getMessage());
        }
    }
}
