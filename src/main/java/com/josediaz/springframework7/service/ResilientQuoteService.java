package com.josediaz.springframework7.service;

import com.josediaz.springframework7.client.QuoteClient;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.stereotype.Service;

/**
 * Servicio que demuestra el uso de anotaciones de resiliencia de Spring Framework 7
 * 
 * Las anotaciones @Retryable y @ConcurrencyLimit están habilitadas mediante
 * @EnableResilientMethods en HttpClientConfig
 */
@Service
public class ResilientQuoteService {

    private final QuoteClient quoteClient;

    public ResilientQuoteService(QuoteClient quoteClient) {
        this.quoteClient = quoteClient;
    }

    /**
     * Obtiene un chiste aleatorio con política de reintentos
     * 
     * @Retryable: Reintenta automáticamente si falla la llamada
     * Los parámetros específicos pueden variar según la versión de Spring Framework 7
     */
    @Retryable
    public QuoteClient.ChuckNorrisJoke getRandomJokeWithRetry() {
        return quoteClient.getRandomJoke();
    }

    /**
     * Obtiene categorías con límite de concurrencia
     * 
     * @ConcurrencyLimit(5): Permite máximo 5 llamadas concurrentes a este método
     * Si hay más de 5 llamadas simultáneas, las adicionales esperarán
     */
    @ConcurrencyLimit(5)
    public String[] getCategoriesWithConcurrencyLimit() {
        return quoteClient.getCategories();
    }

    /**
     * Método que combina ambas anotaciones de resiliencia
     * 
     * - @Retryable: Reintenta automáticamente si falla
     * - @ConcurrencyLimit(3): Limita a 3 llamadas concurrentes
     */
    @Retryable
    @ConcurrencyLimit(3)
    public QuoteClient.ChuckNorrisJoke getRandomJokeResilient() {
        return quoteClient.getRandomJoke();
    }
}

