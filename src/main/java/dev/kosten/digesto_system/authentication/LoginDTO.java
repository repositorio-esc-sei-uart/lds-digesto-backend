/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.authentication;

import lombok.Data;

/**
 *
 * @author avila
 */
@Data
public class LoginDTO {
    private String email;
    
    private String password;
}
