/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author avila
 */
@Service
public class LogService {

    private static final Logger log = LoggerFactory.getLogger(LogService.class);

    public void info(String mensaje) {
        log.info(mensaje);
    }

    public void debug(String mensaje) {
        log.debug(mensaje);
    }

    public void warn(String mensaje) {
        log.warn(mensaje);
    }

    public void error(String mensaje, Throwable e) {
        log.error(mensaje, e);
    }

    // Si querés, podés agregar métodos con prefijos uniformes
    public void operacionExitosa(String entidad, String detalle) {
        log.info("✅ [{}] {}", entidad, detalle);
    }

    public void operacionFallida(String entidad, String detalle, Throwable e) {
        log.error("❌ [{}] {} - Error: {}", entidad, detalle, e.getMessage());
    }
}

