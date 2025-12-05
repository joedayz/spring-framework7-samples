package com.josediaz.springframework7.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ejemplo de versionado basado en PATH
 * URLs: /api/v1/greeting, /api/v2/greeting
 */
@RestController
@RequestMapping(path = "/api/v{version}/greeting")
public class PathBasedVersionController {

    @GetMapping(version = "1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV1() {
        return "Hello from API v1";
    }

    @GetMapping(version = "2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV2() {
        return "Hello from API v2";
    }

    @GetMapping(version = "3", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV3() {
        return "Hello from API v3";
    }
}

