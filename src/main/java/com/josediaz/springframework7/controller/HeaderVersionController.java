package com.josediaz.springframework7.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ejemplo de versionado basado en REQUEST HEADER
 * Headers: X-API-Version: 1, X-API-Version: 2
 */
@RestController
@RequestMapping("/header-greeting")
public class HeaderVersionController {

    @GetMapping(version = "1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV1() {
        return "Greeting v1 via header";
    }

    @GetMapping(version = "2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV2() {
        return "Greeting v2 via header";
    }

    @GetMapping(version = "3", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV3() {
        return "Greeting v3 via header";
    }
}

