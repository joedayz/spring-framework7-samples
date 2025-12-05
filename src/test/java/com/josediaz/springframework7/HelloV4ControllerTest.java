package com.josediaz.springframework7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test para el controlador HelloV4 que usa @HttpExchange client
 * Demuestra la integración entre versionado de API y clientes HTTP declarativos
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloV4ControllerTest {

    RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void testHelloV4() {
        // Versión 4 del endpoint hello que usa @HttpExchange client
        client.get()
                .uri("/hello")
                .header("X-API-Version", "4")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(quote -> {
                    // Verificar que retorna una cita (obtenida del QuoteClient)
                    assert quote != null && !quote.isEmpty();
                });
    }
}

