package com.josediaz.springframework7.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ejemplo de versionado basado en QUERY PARAMETER
 * URLs: /greeting?version=1, /greeting?version=2
 */
@RestController
@RequestMapping("/greeting")
public class QueryParamVersionController {

    @GetMapping(version = "1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV1() {
        return "Greeting v1 via query parameter";
    }

    @GetMapping(version = "2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV2() {
        return "Greeting v2 via query parameter";
    }

    @GetMapping(version = "3", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV3() {
        return "Greeting v3 via query parameter";
    }
}

