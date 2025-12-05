package com.josediaz.springframework7.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador separado para el endpoint hello sin versionado
 * 
 * HALLAZGO IMPORTANTE: Spring Framework 7 NO permite que dos controladores
 * compartan la misma ruta cuando uno tiene métodos con 'version' y el otro no.
 * 
 * Solución: Usar una ruta diferente para el endpoint sin versión
 * o desactivar el versionado si no todos los endpoints lo necesitan.
 */
@RestController
@RequestMapping("/hello-default")
public class HelloDefaultController {

    // Endpoint sin versión - en una ruta diferente para evitar conflictos
    @GetMapping
    public String hello() {
        return "Hello, World! Welcome to Spring Framework 7";
    }
}

