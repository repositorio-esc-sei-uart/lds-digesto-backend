/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.usuario;

import dev.labintec.digesto_system.usuario.UsuarioDTO;
import dev.labintec.digesto_system.cargo.Cargo;
import dev.labintec.digesto_system.sector.Sector;
import dev.labintec.digesto_system.cargo.CargoRepository;
import dev.labintec.digesto_system.sector.SectorRepository;
import dev.labintec.digesto_system.usuario.UsuarioMapper;
import dev.labintec.digesto_system.usuario.UsuarioResponseDTO;
import dev.labintec.digesto_system.estadoU.EstadoU;
import dev.labintec.digesto_system.estadoU.EstadoUMapper;
import dev.labintec.digesto_system.rol.Rol;
import dev.labintec.digesto_system.exception.RecursoDuplicadoException;
import dev.labintec.digesto_system.exception.RecursoNoEncontradoException;
import dev.labintec.digesto_system.log.LogService;
import dev.labintec.digesto_system.estadoU.EstadoURepository;
import dev.labintec.digesto_system.rol.RolMapper;
import dev.labintec.digesto_system.rol.RolRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Servicio que gestiona la lógica de negocio relacionada con los usuarios del sistema.
 * 
 * Se encarga de crear, listar, buscar y eliminar usuarios, 
 * además de manejar la encriptación de contraseñas antes de guardar en la base de datos.
 * 
 * Esta clase utiliza el patrón de capa de servicio de Spring, 
 * actuando como intermediaria entre los controladores y los repositorios.
 * 
 */
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

    
    /**
     * Crea un nuevo usuario a partir de los datos recibidos en un {@link UsuarioDTO}.
     * 
     * El método busca las entidades relacionadas (Rol, Sector, EstadoU, Cargo)
     * según los identificadores proporcionados, mapea el DTO a una entidad {@link Usuario},
     * y encripta la contraseña antes de guardar el registro en la base de datos.
     *
     * @param dto el objeto con los datos del usuario a crear
     * @return un {@link UsuarioDTO} con los datos del usuario recién creado
     * @throws RecursoNoEncontradoException si alguna de las entidades relacionadas no existe
     */
    public Usuario crearUsuario(UsuarioDTO dto) {
        
        // Buscar entidades relacionadas
        Rol rol = rolRepo.findById(dto.getIdRol())
                .orElseThrow(() -> {
                    logService.error("Rol no encontrado con id=" + dto.getIdRol(), null);
                    return new RecursoNoEncontradoException("Rol no encontrado");
                });

        Sector sector = sectorRepo.findById(dto.getIdSector())
                .orElseThrow(() -> {
                    logService.error("Sector no encontrado con id=" + dto.getIdSector(), null);
                    return new RecursoNoEncontradoException("Sector no encontrado");
                });

        EstadoU estado = estadoRepo.findById(dto.getIdEstadoU())
                .orElseThrow(() -> {
                    logService.error("Estado no encontrado con id=" + dto.getIdEstadoU(), null);
                    return new RecursoNoEncontradoException("Estado no encontrado");
                });

        Cargo cargo = cargoRepo.findById(dto.getIdCargo())
                .orElseThrow(() -> {
                    logService.error("Cargo no encontrado con id=" + dto.getIdCargo(), null);
                    return new RecursoNoEncontradoException("Cargo no encontrado");
                });


        // Verificar duplicados
        if (usuarioRepo.findByEmail(dto.getEmail()).isPresent()) {
            logService.warn("Intento de crear usuario duplicado con email=" + dto.getEmail());
            throw new RecursoDuplicadoException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        if (usuarioRepo.findByDni(dto.getDni()).isPresent()) {
            logService.warn("Intento de crear usuario duplicado con DNI=" + dto.getDni());
            throw new RecursoDuplicadoException("Ya existe un usuario con el DNI: " + dto.getDni());
        }
        
        if(usuarioRepo.findByLegajo(dto.getLegajo()).isPresent()){
            logService.warn("Intento de crear usuario duplicado con legajo=" + dto.getLegajo());
            throw new RecursoDuplicadoException("Ya existe un usuario con este legajo: " + dto.getLegajo());
        }

        // Crear la entidad a partir del DTO
        Usuario usuario = mapper.toEntity(dto, rol, sector, estado, cargo);

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        
            
        // Guardar en la base de datos
        Usuario guardado = usuarioRepo.save(usuario);

        logService.operacionExitosa("Usuario", "Creado con id=" + guardado.getIdUsuario());

        return guardado;

    }
    
    
    /**
     * Obtiene una lista de todos los usuarios del sistema.
     * 
     *
     * @return una lista de objetos {@link UsuarioDTO} con los datos de todos los usuarios
     */
    public List<Usuario> listarUsuarios() {
        logService.info("Listando todos los usuarios");
        List<Usuario> usuarios = usuarioRepo.findAll();
        logService.info("Se encontraron " + usuarios.size() + " usuarios");
        return usuarios;
    }

        
    /**
     * Busca un usuario por su identificador único y lo devuelve en un DTO de respuesta.
     *
     * @param id el ID del usuario a buscar
     * @return un {@link UsuarioResponseDTO} con los datos visibles del usuario
     * @throws RecursoNoEncontradoException si el usuario no existe
     */
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        logService.info("Buscando usuario con id=" + id);
        
        Optional<Usuario> opcional = usuarioRepo.findById(id);

        if (!opcional.isPresent()) {
            logService.error("Usuario con id=" + id + " no existe", null);

            throw new RecursoNoEncontradoException("Usuario con id: " + id + " no existe");
        }

        Usuario usuario = opcional.get();
        
        logService.operacionExitosa("Usuario", "Encontrado con id=" + id);
        
        // Convertimos la entidad a DTO de salida
        return toResponseDTO(usuario);
        
    }


    
    /**
     * Busca un usuario por su correo electrónico.
     * 
     * Este método se usa comúnmente en procesos de autenticación o validación de cuentas.
     *
     * @param email el correo electrónico del usuario
     * @return un {@link UsuarioDTO} con los datos del usuario correspondiente
     * @throws RecursoNoEncontradoException si el usuario no existe
     */
    public Usuario obtenerPorEmail(String email) {
        
        Optional<Usuario> opcional = usuarioRepo.findByEmail(email);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("Usuario no existe");
        }
        
        return opcional.get();
    }

    
    /**
     * Elimina un usuario de la base de datos según su identificador.
     *
     * @param id el ID del usuario a eliminar
     * @throws RecursoNoEncontradoException si el usuario no existe
     */
    public void eliminar(Integer id) {
        
        Optional<Usuario> opcional = usuarioRepo.findById(id);
            
        if(opcional.isPresent()){
            usuarioRepo.deleteById(id);
        }
        else{
            throw new RecursoNoEncontradoException("Usuario con id: " + id + " no existe");
        }
    }
    
    
    /**
     * Convierte una entidad {@link Usuario} en un {@link UsuarioResponseDTO}.
     *
     * @param usuario la entidad de usuario
     * @return el DTO con los datos visibles
     */
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


}


