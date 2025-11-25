package dev.kosten.digesto_system.usuario;

import dev.kosten.digesto_system.cargo.Cargo;
import dev.kosten.digesto_system.cargo.CargoRepository;
import dev.kosten.digesto_system.estadoU.EstadoU;
import dev.kosten.digesto_system.estadoU.EstadoUMapper;
import dev.kosten.digesto_system.estadoU.EstadoURepository;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.exception.UnicidadFallidaException;
import dev.kosten.digesto_system.log.LogService;
import dev.kosten.digesto_system.registro.entity.Registro;
import dev.kosten.digesto_system.registro.repository.RegistroRepository; // <--- IMPORTANTE
import dev.kosten.digesto_system.rol.Rol;
import dev.kosten.digesto_system.rol.RolMapper;
import dev.kosten.digesto_system.rol.RolRepository;
import dev.kosten.digesto_system.sector.Sector;
import dev.kosten.digesto_system.sector.SectorRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <--- IMPORTANTE

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private SectorRepository sectorRepo;

    @Autowired
    private EstadoURepository estadoRepo;

    @Autowired
    private CargoRepository cargoRepo;

    @Autowired
    private UsuarioMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private LogService logService;
    
    @Autowired
    private RegistroRepository registroRepository; // <--- 1. INYECCIÓN NECESARIA

    
    public Usuario crearUsuario(UsuarioDTO dto, String emailResponsable) {
        // ... (Toda tu lógica de validación y búsqueda de entidades igual que antes) ...
        // (Para ahorrar espacio, asumo que copias las validaciones que ya tenías arriba)
        // ...
        
        // Buscar entidades (Rol, Sector, etc...)
        Rol rol = rolRepo.findById(dto.getIdRol()).orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));
        Sector sector = sectorRepo.findById(dto.getIdSector()).orElseThrow(() -> new RecursoNoEncontradoException("Sector no encontrado"));
        EstadoU estado = estadoRepo.findById(dto.getIdEstadoU()).orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado"));
        Cargo cargo = cargoRepo.findById(dto.getIdCargo()).orElseThrow(() -> new RecursoNoEncontradoException("Cargo no encontrado"));

        // Validaciones de unicidad (Email, DNI, Legajo) - COPIA TU LÓGICA AQUÍ
        if (usuarioRepo.findByEmail(dto.getEmail()).isPresent()) {
             throw new UnicidadFallidaException("Email ya existe", Arrays.asList("EMAIL"));
        }
        // ... resto de validaciones ...

        Usuario usuario = mapper.toEntity(dto, rol, sector, estado, cargo);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        Usuario guardado = usuarioRepo.save(usuario);

        // --- AUDITORÍA ---
        Usuario responsable = usuarioRepo.findByEmail(emailResponsable).orElse(null);
        registrarOperacion("REGISTRAR", responsable, guardado);
        // ----------------

        logService.operacionExitosa("Usuario", "Creado con id=" + guardado.getIdUsuario());
        return guardado;
    }
    
    public List<Usuario> listarUsuarios() {
        logService.info("Listando todos los usuarios");
        return usuarioRepo.findAll();
    }

    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con id: " + id + " no existe"));
        return toResponseDTO(usuario);
    }

    // --- 2. MÉTODO ELIMINAR (BORRADO FÍSICO CORREGIDO) ---
    @Transactional // Importante para que las operaciones de BD sean atómicas
    public void eliminar(Integer id, String emailResponsable) {
        
        // A. Buscar al usuario
        Usuario usuarioABorrar = usuarioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con id: " + id + " no existe"));

        // B. Buscar al admin responsable (para el registro)
        Usuario responsable = usuarioRepo.findByEmail(emailResponsable)
                .orElseThrow(() -> new RecursoNoEncontradoException("Admin responsable no encontrado"));

        // C. Guardar el registro de que se va a borrar
        registrarOperacion("DESACTIVAR", responsable, usuarioABorrar);

        // D. DESVINCULAR (La parte clave para que no falle el borrado físico)
        // Buscamos donde aparece este usuario en la tabla 'registro' y ponemos NULL
        
        // Caso 1: Fue responsable de algo
        List<Registro> registrosComoResponsable = registroRepository.findByUsuarioResponsable(usuarioABorrar);
        for (Registro r : registrosComoResponsable) {
            r.setUsuarioResponsable(null);
            registroRepository.save(r);
        }

        // Caso 2: Fue afectado por algo
        List<Registro> registrosComoAfectado = registroRepository.findByUsuarioAfectado(usuarioABorrar);
        for (Registro r : registrosComoAfectado) {
            r.setUsuarioAfectado(null);
            registroRepository.save(r);
        }

        // E. AHORA SÍ: BORRADO FÍSICO
        usuarioRepo.deleteById(id);
        
        logService.info("Usuario ID " + id + " eliminado físicamente por " + emailResponsable);
    }
    
    public Usuario ActualizarUsuario(Integer idUsuario, Integer Nuevodni, String Nuevoemail,
                                     String Nuevopassword, String Nuevonombre, String Nuevoapellido, String Nuevolegajo,
                                     Integer NuevoidRol, Integer NuevoidSector, Integer NuevoidEstadoU, Integer NuevoidCargo, 
                                     String emailResponsable) { // <-- Agregado emailResponsable

        // ... (Tu lógica de búsqueda y validación existente) ...
        Usuario usuario = usuarioRepo.findById(idUsuario)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no existe"));
        
        // ... (Setters de los nuevos datos) ...
        // (Simplificado para el ejemplo, mantén tu lógica completa de validación y seteo)
        usuario.setNombre(Nuevonombre);
        // ... etc ...

        Usuario actualizado = usuarioRepo.save(usuario);

        // --- AUDITORÍA ---
        Usuario responsable = usuarioRepo.findByEmail(emailResponsable).orElse(null);
        registrarOperacion("MODIFICAR", responsable, actualizado);
        // ----------------

        return actualizado;
    }

    // ... (Otros métodos auxiliares como obtenerPorEmail, toResponseDTO) ...
    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(RolMapper.toDTO(usuario.getRol()));
        dto.setLegajo(usuario.getLegajo());
        dto.setEstadoU(EstadoUMapper.toDTO(usuario.getEstado()));
        return dto;
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepo.findByEmail(email).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no existe"));
    }

    // --- 3. HELPER PARA AUDITORÍA ---
    private void registrarOperacion(String tipoOperacion, Usuario responsable, Usuario usuarioAfectado) {
        if (responsable == null || usuarioAfectado == null) return;

        Registro registro = Registro.builder()
                .fechaCarga(new Date())
                .tipoOperacion(tipoOperacion)
                .usuarioResponsable(responsable)
                .documentoAfectado(null)
                .usuarioAfectado(usuarioAfectado)
                .build();

        registroRepository.save(registro);
    }
    
    /**
     * Obtiene todos los datos de un usuario (UsuarioDTO) para edición.
     */
    public UsuarioDTO obtenerTodoPorId(Integer id) {
        logService.info("Buscando usuario con id=" + id);

        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con id: " + id + " no existe"));

        return mapper.toDTO(usuario);
    }
    
    
}