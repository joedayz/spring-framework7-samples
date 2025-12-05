package com.josediaz.springframework7;

import com.josediaz.springframework7.client.QuoteClient;
import com.josediaz.springframework7.service.ResilientQuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test para demostrar el uso de anotaciones de resiliencia
 * 
 * Estas pruebas verifican que las anotaciones @Retryable y @ConcurrencyLimit
 * funcionan correctamente
 */
@SpringBootTest
class ResilientQuoteServiceTest {

    @Autowired
    private ResilientQuoteService resilientQuoteService;

    @Test
    void testGetRandomJokeWithRetry() {
        // Este método tiene @Retryable, debería funcionar incluso si hay fallos temporales
        QuoteClient.ChuckNorrisJoke joke = resilientQuoteService.getRandomJokeWithRetry();
        assertThat(joke).isNotNull();
        assertThat(joke.getValue()).isNotNull();
        assertThat(joke.getId()).isNotNull();
    }

    @Test
    void testGetCategoriesWithConcurrencyLimit() {
        // Este método tiene @ConcurrencyLimit(5), debería manejar múltiples llamadas concurrentes
        String[] categories = resilientQuoteService.getCategoriesWithConcurrencyLimit();
        assertThat(categories).isNotNull();
        assertThat(categories.length).isGreaterThan(0);
    }

    @Test
    void testConcurrencyLimit() throws Exception {
        // Probar que el límite de concurrencia funciona
        // Intentamos hacer 10 llamadas concurrentes, pero solo 5 deberían ejecutarse simultáneamente
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String[]>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            CompletableFuture<String[]> future = CompletableFuture.supplyAsync(() -> {
                return resilientQuoteService.getCategoriesWithConcurrencyLimit();
            }, executor);
            futures.add(future);
        }

        // Esperar a que todas las llamadas completen
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Verificar que todas las llamadas fueron exitosas
        for (CompletableFuture<String[]> future : futures) {
            String[] categories = future.get();
            assertThat(categories).isNotNull();
            assertThat(categories.length).isGreaterThan(0);
        }

        executor.shutdown();
    }

    @Test
    void testGetRandomJokeResilient() {
        // Este método combina @Retryable y @ConcurrencyLimit
        QuoteClient.ChuckNorrisJoke joke = resilientQuoteService.getRandomJokeResilient();
        assertThat(joke).isNotNull();
        assertThat(joke.getValue()).isNotNull();
    }
}

