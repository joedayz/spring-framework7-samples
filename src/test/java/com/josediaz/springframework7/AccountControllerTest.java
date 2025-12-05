package com.josediaz.springframework7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test usando RestTestClient - Nueva característica de Spring Framework 7
 * RestTestClient es un cliente ligero para probar endpoints REST sin dependencias reactivas
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void testGetAllAccounts() {
        // Este endpoint no tiene versión específica
        // Cuando el versionado está activo, usar un header válido
        client.get()
                .uri("/accounts")
                .header("X-API-Version", "1.0") // Usar versión válida para endpoints sin versión específica
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").value(size -> {
                    assert (Integer) size >= 3;
                })
                .jsonPath("$[0].id").exists()
                .jsonPath("$[0].name").exists()
                .jsonPath("$[0].email").exists();
    }

    @Test
    void testGetAccountV1_0() {
        client.get()
                .uri("/accounts/1")
                .header("X-API-Version", "1.0")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").exists()
                .jsonPath("$.email").exists()
                .jsonPath("$.phone").doesNotExist(); // v1.0 no incluye teléfono
    }

    @Test
    void testGetAccountV1_1() {
        client.get()
                .uri("/accounts/1")
                .header("X-API-Version", "1.1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").exists()
                .jsonPath("$.email").exists()
                .jsonPath("$.phone").exists(); // v1.1 incluye teléfono
    }

    @Test
    void testGetAccountV2_0() {
        client.get()
                .uri("/accounts/1")
                .header("X-API-Version", "2.0")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").exists()
                .jsonPath("$.email").exists()
                .jsonPath("$.phone").exists()
                .jsonPath("$.createdAt").exists(); // v2.0 incluye fecha de creación
    }

    @Test
    void testCreateAccount() {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts")
                        .queryParam("name", "Test User")
                        .queryParam("email", "test@example.com")
                        .queryParam("phone", "+34 600 000 000")
                        .build())
                .header("X-API-Version", "1.0") // Usar versión válida
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.name").isEqualTo("Test User")
                .jsonPath("$.email").isEqualTo("test@example.com");
    }

    @Test
    void testGetAccountNotFound() {
        // Este endpoint sin versión específica requiere header cuando el versionado está activo
        client.get()
                .uri("/accounts/999")
                .header("X-API-Version", "1.0") // Usar versión válida
                .exchange()
                .expectStatus().isNotFound();
    }
}
