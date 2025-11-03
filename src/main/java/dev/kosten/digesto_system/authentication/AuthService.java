/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.authentication;

import dev.kosten.digesto_system.usuario.Usuario;
import dev.kosten.digesto_system.usuario.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 *
 * @author avila
 */
@Service
public class AuthService {
    
    @Autowired
    UsuarioRepository userRepo;
    
    public String login(String email, String password) {
        Optional<Usuario> user = userRepo.findByEmail(email);
        
        // Generar una clave segura de 256 bits para HS256
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        
        if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
            // Generar el token JWT con los datos del usuario
            String token = Jwts.builder()
            .setSubject(user.get().getEmail()) // identificador principal (puede ser email)
            .claim("idUsuario", user.get().getIdUsuario())
            .claim("nombre", user.get().getNombre())
            .claim("apellido", user.get().getApellido())
            .claim("email", user.get().getEmail())
            .claim("rol", user.get().getRol()) 
            .claim("legajo", user.get().getLegajo())
            .claim("estadoU", user.get().getEstado().getNombre()) 
            .setIssuedAt(new Date()) // fecha de emisión
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // expira en 1 hora
            .signWith(SignatureAlgorithm.HS256, key) // firma con clave secreta
            .compact();

            return token;
        }

        return "Credenciales inválidas";
    }
}
