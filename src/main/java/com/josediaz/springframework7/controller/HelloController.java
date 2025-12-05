package com.josediaz.springframework7.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para endpoints con versionado
 * 
 * Demuestra el uso del atributo 'version' en @GetMapping
 * para crear diferentes versiones del mismo endpoint
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
}

