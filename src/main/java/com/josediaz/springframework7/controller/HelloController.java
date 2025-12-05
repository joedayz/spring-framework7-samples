package com.josediaz.springframework7.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para endpoints con versionado
 * 
 * NOTA IMPORTANTE: Basado en pruebas y comportamiento observado,
 * Spring Framework 7 parece NO permitir mezclar métodos con atributo 'version'
 * y métodos sin 'version' en el mismo controlador cuando el versionado está activo.
 * 
 * Solución: Todos los métodos en este controlador tienen versión.
 * El método sin versión está en HelloDefaultController.
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    // Versión 1 - Usando atributo version en @GetMapping
    @GetMapping(version = "1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHelloV1() {
        return "Hello World";
    }

    // Versión 2 - Usando atributo version en @GetMapping
    @GetMapping(version = "2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHelloV2() {
        return "Hi World";
    }
    
    // NOTA: El método sin versión se movió a HelloDefaultController
    // para evitar conflictos cuando el versionado está activo
}

