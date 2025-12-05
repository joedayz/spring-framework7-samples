package com.josediaz.springframework7.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ejemplo de versionado basado en MEDIA TYPE HEADER
 * Headers: Accept: application/json; version=1, Accept: application/json; version=2
 */
@RestController
@RequestMapping("/mediatype-greeting")
public class MediaTypeVersionController {

    @GetMapping(version = "1", produces = MediaType.APPLICATION_JSON_VALUE)
    public GreetingResponse greetingV1() {
        return new GreetingResponse("Greeting v1 via media type", 1);
    }

    @GetMapping(version = "2", produces = MediaType.APPLICATION_JSON_VALUE)
    public GreetingResponse greetingV2() {
        return new GreetingResponse("Greeting v2 via media type", 2);
    }

    @GetMapping(version = "3", produces = MediaType.APPLICATION_JSON_VALUE)
    public GreetingResponse greetingV3() {
        return new GreetingResponse("Greeting v3 via media type", 3);
    }

    // Clase de respuesta
    public static class GreetingResponse {
        private String message;
        private int version;

        public GreetingResponse(String message, int version) {
            this.message = message;
            this.version = version;
        }

        public String getMessage() {
            return message;
        }

        public int getVersion() {
            return version;
        }
    }
}

