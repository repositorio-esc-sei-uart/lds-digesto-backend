/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.exception;

import java.util.List;

/**
 *
 * @author Guevara
 */
public class UnicidadFallidaException extends RuntimeException {

    private final List<String> duplicatedFields;

    public UnicidadFallidaException(String message, List<String> duplicatedFields) {
        super(message);
        this.duplicatedFields = duplicatedFields;
    }

    public List<String> getDuplicatedFields() {
        return duplicatedFields;
    }
}
