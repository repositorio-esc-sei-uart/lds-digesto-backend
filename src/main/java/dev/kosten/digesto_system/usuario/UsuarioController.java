/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.usuario;


import dev.kosten.digesto_system.estadoU.EstadoUMapper;
import dev.kosten.digesto_system.rol.RolMapper;
import dev.kosten.digesto_system.usuario.UsuarioDTO;
import dev.kosten.digesto_system.usuario.UsuarioMapper;
import dev.kosten.digesto_system.usuario.UsuarioResponseDTO;
import dev.kosten.digesto_system.usuario.Usuario;
import dev.kosten.digesto_system.usuario.UsuarioService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * Controlador REST para la gestión de usuarios.
 * 
 * Expone endpoints para realizar operaciones CRUD sobre los usuarios:
 * listar, obtener, crear y eliminar. 
 * 
 * Utiliza la capa de servicio {@link UsuarioService} para manejar la lógica de negocio.
 * 
 */
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService servicio;
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    
    /**
     * Obtiene una lista con todos los usuarios registrados en el sistema.
     *
     * @return una lista de objetos {@link UsuarioResponseDTO} con los datos visibles de los usuarios.
     * @response 200 OK - Si la lista se obtiene correctamente.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioResponseDTO> listarUsuarios() {
        logger.info("[GET /usuarios] Listando todos los usuarios");
        List<Usuario> usuarios = servicio.listarUsuarios();
        List<UsuarioResponseDTO> dtos = new ArrayList<>();
        
        // Se transforma cada entidad Usuario en su representación de salida (ResponseDTO)
        for (Usuario usuario : usuarios) {
            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setEmail(usuario.getEmail());
            dto.setRol(RolMapper.toDTO(usuario.getRol()));
            dto.setLegajo(usuario.getLegajo());
            dto.setEstadoU(EstadoUMapper.toDTO(usuario.getEstado()));
            dtos.add(dto);
        }
        
        logger.info("[listarUsuarios] Se recuperaron {} usuarios desde la base", usuarios.size());
        
        return dtos;

    }

    
    /**
     * Obtiene un usuario por su identificador único.
     *
     * @param id el ID del usuario a buscar
     * @return un {@link UsuarioResponseDTO} con los datos visibles del usuario
     * @throws RecursoNoEncontradoException si el usuario no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        logger.info("[GET /usuarios/{}] Consultando usuario", id);
        UsuarioResponseDTO dto = servicio.obtenerPorId(id);
        
        logger.info("[GET /usuarios/{}] Usuario encontrado: email={}", id, dto.getEmail());
        
        return ResponseEntity.ok(dto);
    }


    
    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param dto objeto {@link UsuarioDTO} con los datos del usuario a crear.
     * @return el usuario creado, en formato {@link UsuarioDTO}.
     * @response 201 Created - Si el usuario se crea correctamente.
     * @throws RecursoNoEncontradoException si alguna entidad relacionada (rol, sector, estado, cargo) no existe.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO crearUsuario(@Valid @RequestBody UsuarioDTO dto) {
        logger.info("[POST /usuarios] Creando usuario: email={}, rol={}, cargo={}",
                dto.getEmail(), dto.getIdRol(), dto.getIdCargo());

        Usuario nuevo = servicio.crearUsuario(dto);
        logger.info("[GET /usuarios/{}] Usuario encontrado: email={}", nuevo.getEmail());
        
        return UsuarioMapper.toDTO(nuevo);
    }

    
    /**
     * Elimina un usuario del sistema según su ID.
     *
     * @param id identificador del usuario a eliminar.
     * @response 204 No Content - Si el usuario se elimina correctamente.
     * @response 404 Not Found - Si el usuario no existe.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarUsuario(@PathVariable Integer id) {
        servicio.eliminar(id);
        logger.info("[DELETE /usuarios/{}] Usuario eliminado, id: ", id);
    }
    
        /**
     * Actualiza los datos de un usuario existente en el sistema.
     * <p>
     * Este endpoint permite modificar tanto los datos básicos del usuario
     * (nombre, apellido, email, contraseña, legajo, etc.) como sus relaciones
     * con entidades asociadas ({@link Rol}, {@link Sector}, {@link EstadoU}, {@link Cargo})
     * a partir de los identificadores recibidos en el cuerpo de la solicitud.
     * </p>
     *
     * @param id  el identificador único del usuario que se desea actualizar.
     * @param dto objeto {@link UsuarioDTO} con los nuevos datos del usuario.
     * @return un {@link UsuarioDTO} con la información actualizada del usuario.
     *
     * @response 200 OK - Si la actualización se realiza correctamente.
     * @response 404 Not Found - Si el usuario con el ID especificado no existe.
     * @response 400 Bad Request - Si los datos proporcionados son inválidos.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioDTO actualizarUsuario (@PathVariable Integer id, @RequestBody UsuarioDTO dto){
        Usuario actualizado = servicio.ActualizarUsuario(id,dto.getDni(),dto.getEmail(),
                dto.getPassword(), dto.getNombre(), dto.getApellido(), dto.getLegajo(),
                dto.getIdRol(),dto.getIdSector(),dto.getIdEstadoU(),dto.getIdCargo());
        return UsuarioMapper.toDTO(actualizado);
    }
    
     /**
     * Obtiene todos los datos de un usuario según su identificador.
     * <p>
     * Este endpoint permite recuperar la información completa de un usuario
     * existente en el sistema, incluyendo sus datos personales y las relaciones
     * asociadas (por ejemplo: rol, sector, estado y cargo).
     * </p>
     *
     * @param id el identificador único del usuario a consultar.
     * @return una respuesta {@link ResponseEntity} que contiene un {@link UsuarioDTO}
     *         con todos los datos del usuario.
     *
     * @response 200 OK - Si el usuario se encuentra y se devuelve correctamente.
     * @response 404 Not Found - Si no existe un usuario con el ID especificado.
     */
    @GetMapping("/all/{id}")
    public ResponseEntity<UsuarioDTO> obtenerTodoPorId(@PathVariable Integer id) {
        logger.info("[GET /usuarios/{}] Consultando usuario", id);
        UsuarioDTO dto = servicio.obtenerTodoPorId(id);

        logger.info("[GET /usuarios/{}] Usuario encontrado: email={}", id, dto.getEmail());

        return ResponseEntity.ok(dto);
    }
}


