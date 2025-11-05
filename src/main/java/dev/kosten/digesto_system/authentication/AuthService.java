/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.authentication;

import dev.kosten.digesto_system.usuario.Usuario;
import dev.kosten.digesto_system.usuario.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository userRepo;

    @Value("${jwt.secret}")
    private String secret; // usa la misma clave del application.properties
    
    public String login(String email, String password) {
        Optional<Usuario> user = userRepo.findByEmail(email);
        
        if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {

            // Usar la clave configurada, NO una generada al azar
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

            String token = Jwts.builder()
                .setSubject(user.get().getEmail())
                .claim("idUsuario", user.get().getIdUsuario())
                .claim("nombre", user.get().getNombre())
                .claim("apellido", user.get().getApellido())
                .claim("email", user.get().getEmail())
                .claim("rol", user.get().getRol())
                .claim("legajo", user.get().getLegajo())
                .claim("estadoU", user.get().getEstado().getNombre())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

            return token;
        }

        return "Credenciales inválidas";
    }
}


