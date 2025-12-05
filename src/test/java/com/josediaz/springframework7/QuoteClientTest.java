package com.josediaz.springframework7;

import com.josediaz.springframework7.client.QuoteClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test directo del cliente HTTP para verificar que funciona correctamente
 */
@SpringBootTest
class QuoteClientTest {

    @Autowired
    private QuoteClient quoteClient;

    @Test
    void testQuoteClientInjected() {
        assertThat(quoteClient).isNotNull();
    }

    @Test
    void testGetRandomJoke() {
        QuoteClient.ChuckNorrisJoke joke = quoteClient.getRandomJoke();
        assertThat(joke).isNotNull();
        assertThat(joke.getValue()).isNotNull();
        assertThat(joke.getId()).isNotNull();
    }

    @Test
    void testGetCategories() {
        String[] categories = quoteClient.getCategories();
        assertThat(categories).isNotNull();
        assertThat(categories.length).isGreaterThan(0);
    }
}

