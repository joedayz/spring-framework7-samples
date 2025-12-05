package com.josediaz.springframework7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

/**
 * Ejemplo de test usando RestTestClient - Nueva característica de Spring Framework 7
 * RestTestClient es un cliente ligero para probar endpoints REST sin dependencias reactivas
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerTest {

    RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    // NOTA: Los endpoints con atributo 'version' requieren el header X-API-Version
    // cuando el versionado está activo (ApiVersionConfigurer configurado)
    
    @Test
    void testHelloV1() {
        // Test específico para la versión 1 usando header X-API-Version
        client.get()
                .uri("/hello")
                .header("X-API-Version", "1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello World");
    }
    
    @Test
    void testHelloV2() {
        // Test específico para la versión 2 usando header X-API-Version
        client.get()
                .uri("/hello")
                .header("X-API-Version", "2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hi World");
    }
}
