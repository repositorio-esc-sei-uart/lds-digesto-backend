/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.usuario;

import dev.kosten.digesto_system.usuario.UsuarioDTO;
import dev.kosten.digesto_system.cargo.Cargo;
import dev.kosten.digesto_system.sector.Sector;
import dev.kosten.digesto_system.cargo.CargoRepository;
import dev.kosten.digesto_system.sector.SectorRepository;
import dev.kosten.digesto_system.usuario.UsuarioMapper;
import dev.kosten.digesto_system.usuario.UsuarioResponseDTO;
import dev.kosten.digesto_system.estadoU.EstadoU;
import dev.kosten.digesto_system.estadoU.EstadoUMapper;
import dev.kosten.digesto_system.rol.Rol;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.log.LogService;
import dev.kosten.digesto_system.estadoU.EstadoURepository;
import dev.kosten.digesto_system.exception.UnicidadFallidaException;
import dev.kosten.digesto_system.rol.RolMapper;
import dev.kosten.digesto_system.rol.RolRepository;
import java.util.Arrays;
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

        // Buscar entidades relacionadas (se mantiene igual)
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

        // 1. Determinar si existe duplicidad para cada campo
        boolean emailDuplicado = usuarioRepo.findByEmail(dto.getEmail()).isPresent();
        boolean dniDuplicado = usuarioRepo.findByDni(dto.getDni()).isPresent();
        boolean legajoDuplicado = usuarioRepo.findByLegajo(dto.getLegajo()).isPresent();

        // --- MANEJO DE EXCEPCIONES CON IF/ELSE IF ---

        if (emailDuplicado && dniDuplicado && legajoDuplicado) {
            // Caso 3 duplicados
            logService.warn("Intento de crear usuario duplicado: Email, DNI y Legajo.");
            List<String> errores = Arrays.asList("EMAIL", "DNI", "LEGAJO");
            throw new UnicidadFallidaException("Fallo de unicidad: Email, DNI y Legajo ya existen.", errores);
        } 

        // Casos 2 duplicados
        else if (emailDuplicado && dniDuplicado) {
            logService.warn("Intento de crear usuario duplicado: Email y DNI.");
            List<String> errores = Arrays.asList("EMAIL", "DNI");
            throw new UnicidadFallidaException("Ya existe un usuario con el Email y el DNI ingresados.", errores);
        } else if (emailDuplicado && legajoDuplicado) {
            logService.warn("Intento de crear usuario duplicado: Email y Legajo.");
            List<String> errores = Arrays.asList("EMAIL", "LEGAJO");
            throw new UnicidadFallidaException("Ya existe un usuario con el Email y el Legajo ingresados.", errores);
        } else if (dniDuplicado && legajoDuplicado) {
            logService.warn("Intento de crear usuario duplicado: DNI y Legajo.");
            List<String> errores = Arrays.asList("DNI", "LEGAJO");
            throw new UnicidadFallidaException("Ya existe un usuario con el DNI y el Legajo ingresados.", errores);
        }

        // Casos 1 duplicado
        else if (emailDuplicado) {
            logService.warn("Intento de crear usuario duplicado con email=" + dto.getEmail());
            List<String> errores = Arrays.asList("EMAIL");
            throw new UnicidadFallidaException("Ya existe un usuario con el email: " + dto.getEmail(), errores);
        } else if (dniDuplicado) {
            logService.warn("Intento de crear usuario duplicado con DNI=" + dto.getDni());
            List<String> errores = Arrays.asList("DNI");
            throw new UnicidadFallidaException("Ya existe un usuario con el DNI: " + dto.getDni(), errores);
        } else if (legajoDuplicado) {
            logService.warn("Intento de crear usuario duplicado con legajo=" + dto.getLegajo());
            List<String> errores = Arrays.asList("LEGAJO");
            throw new UnicidadFallidaException("Ya existe un usuario con este legajo: " + dto.getLegajo(), errores);
        }


        // Si no hay duplicados, se continúa con la creación del usuario
        Usuario usuario = mapper.toEntity(dto, rol, sector, estado, cargo);

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Guardar en la base de datos
        Usuario guardado = usuarioRepo.save(usuario);

        logService.operacionExitosa("Usuario", "Creado con id=" + guardado.getIdUsuario());

        return guardado;
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
    
     /**
     * Actualiza los datos de un usuario existente identificado por su ID.
     * <p>
     * Este método busca al usuario en la base de datos y, si existe,
     * actualiza sus atributos básicos (DNI, email, nombre, apellido, contraseña, legajo)
     * y sus relaciones con otras entidades como {@link Rol}, {@link Sector},
     * {@link EstadoU} y {@link Cargo}, usando los IDs proporcionados.
     * Finalmente, guarda los cambios en el repositorio.
     * </p>
     *
     * @param idUsuario      el identificador único del usuario que se desea actualizar
     * @param Nuevodni       el nuevo DNI del usuario
     * @param Nuevoemail     el nuevo correo electrónico del usuario
     * @param Nuevopassword  la nueva contraseña del usuario
     * @param Nuevonombre    el nuevo nombre del usuario
     * @param Nuevoapellido  el nuevo apellido del usuario
     * @param Nuevolegajo    el nuevo legajo del usuario
     * @param NuevoidRol     el ID del nuevo rol asociado al usuario
     * @param NuevoidSector  el ID del nuevo sector asociado al usuario
     * @param NuevoidEstadoU el ID del nuevo estado del usuario
     * @param NuevoidCargo   el ID del nuevo cargo del usuario
     * @return el usuario actualizado y guardado en la base de datos
     * @throws RecursoNoEncontradoException si el usuario con el ID proporcionado no existe
     * @throws RuntimeException si alguno de los IDs de Rol, Sector, EstadoU o Cargo no existe
     */
    public Usuario ActualizarUsuario(Integer idUsuario,Integer Nuevodni, String Nuevoemail,
            String Nuevopassword,String Nuevonombre,String Nuevoapellido,String Nuevolegajo,
            Integer NuevoidRol,Integer NuevoidSector,Integer NuevoidEstadoU,Integer NuevoidCargo) {
        Optional<Usuario> opcional = usuarioRepo.findById(idUsuario);
        
        if(opcional.isPresent()){
            Usuario usuario = opcional.get();
            
            usuario.setDni(Nuevodni);
            usuario.setEmail(Nuevoemail);
            
            if(Nuevopassword != null && !Nuevopassword.isBlank()){
                if(!Nuevopassword.equals(usuario.getPassword())){
                    usuario.setPassword(passwordEncoder.encode(Nuevopassword));
                }
            }
                
            usuario.setNombre(Nuevonombre);
            usuario.setApellido(Nuevoapellido);
            usuario.setLegajo(Nuevolegajo);
            
         Optional<Rol> rolOptional = rolRepo.findById(NuevoidRol);
    
             if (rolOptional.isPresent()) {
             Rol rol = rolOptional.get();
              usuario.setRol(rol); 
            } else {
           throw new RuntimeException("El Rol con ID " + NuevoidRol + " no fue encontrado.");
            }
        
        Optional<Sector> sectorOptional = sectorRepo.findById(NuevoidSector);
            if (sectorOptional.isPresent()) {
            Sector sector = sectorOptional.get();
              usuario.setSector(sector); 
            } else {
           throw new RuntimeException("El Sector con ID " + NuevoidSector + " no fue encontrado.");
            }
        
        Optional<EstadoU> estadoUOptional = estadoRepo.findById(NuevoidEstadoU);
            if (estadoUOptional.isPresent()) {
            EstadoU estadoU = estadoUOptional.get();
              usuario.setEstado(estadoU); 
            } else {
           throw new RuntimeException("El Estado con ID " + NuevoidEstadoU+ " no fue encontrado.");
            }
        
        Optional<Cargo> cargoOptional = cargoRepo.findById(NuevoidCargo);
             if (cargoOptional.isPresent()) {
            Cargo cargo = cargoOptional.get();
              usuario.setCargo(cargo); 
            } else {
            throw new RuntimeException("El Cargo con ID " + NuevoidCargo + " no fue encontrado.");
            }
             
          return usuarioRepo.save(usuario);
        } else{
            throw new RecursoNoEncontradoException("Usuario con id: " + idUsuario + " no existe");
        }
    }
    
     /**
     * Obtiene un usuario por su identificador único y lo convierte en un {@link UsuarioDTO}.
     * <p>
     * Este método busca un usuario en el repositorio de datos según su ID.
     * Si el usuario existe, se registra la operación exitosa y se devuelve
     * un objeto DTO con toda su información. Si no existe, se registra el error
     * y se lanza una excepción {@link RecursoNoEncontradoException}.
     * </p>
     *
     * @param id el identificador único del usuario a buscar.
     * @return un objeto {@link UsuarioDTO} con la información completa del usuario encontrado.
     * @throws RecursoNoEncontradoException si no se encuentra ningún usuario con el ID especificado.
     */
    public UsuarioDTO obtenerTodoPorId(Integer id) {
        logService.info("Buscando usuario con id=" + id);

        Optional<Usuario> opcional = usuarioRepo.findById(id);

        if (!opcional.isPresent()) {
            logService.error("Usuario con id=" + id + " no existe", null);

            throw new RecursoNoEncontradoException("Usuario con id: " + id + " no existe");
        }

        Usuario usuario = opcional.get();

        logService.operacionExitosa("Usuario", "Encontrado con id=" + id);
        
        return mapper.toDTO(usuario);

    }

}


