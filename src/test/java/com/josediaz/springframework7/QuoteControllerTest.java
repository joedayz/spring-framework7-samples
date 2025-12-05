package com.josediaz.springframework7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test para demostrar el uso de @HttpExchange (cliente HTTP declarativo)
 * Nueva característica de Spring Framework 7
 * 
 * Estos tests usan la API real de Chuck Norris: https://api.chucknorris.io/
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuoteControllerTest {

    RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void testGetRandomQuote() {
        client.get()
                .uri("/quotes/random")
                .header("X-API-Version", "1.0") // Agregar header para evitar problemas con versionado
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(quote -> {
                    // Verificar que el chiste de Chuck Norris no está vacío
                    assert quote != null && !quote.isEmpty();
                });
    }

    @Test
    void testGetRandomQuoteJson() {
        client.get()
                .uri("/quotes/random/json")
                .header("X-API-Version", "1.0") // Agregar header para evitar problemas con versionado
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.value").exists()
                .jsonPath("$.icon_url").exists();
    }

    @Test
    void testGetCategories() {
        client.get()
                .uri("/quotes/categories")
                .header("X-API-Version", "1.0") // Agregar header para evitar problemas con versionado
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0]").exists();
    }

    @Test
    void testTestEndpoint() {
        client.get()
                .uri("/quotes/test")
                .header("X-API-Version", "1.0") // Agregar header para evitar problemas con versionado
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("QuoteController is working");
    }
}

