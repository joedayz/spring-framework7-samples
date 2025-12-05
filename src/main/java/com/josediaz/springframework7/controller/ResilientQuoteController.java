package com.josediaz.springframework7.controller;

import com.josediaz.springframework7.client.QuoteClient;
import com.josediaz.springframework7.service.ResilientQuoteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que demuestra el uso de anotaciones de resiliencia
 * 
 * Este controlador usa ResilientQuoteService que tiene métodos anotados con
 * @Retryable y @ConcurrencyLimit para demostrar las nuevas características
 * de resiliencia de Spring Framework 7
 */
@RestController
@RequestMapping("/resilient-quotes")
public class ResilientQuoteController {

    private final ResilientQuoteService resilientQuoteService;

    public ResilientQuoteController(ResilientQuoteService resilientQuoteService) {
        this.resilientQuoteService = resilientQuoteService;
    }

    /**
     * Endpoint que usa @Retryable para reintentar automáticamente
     */
    @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public QuoteClient.ChuckNorrisJoke getRandomJokeWithRetry() {
        return resilientQuoteService.getRandomJokeWithRetry();
    }

    /**
     * Endpoint que usa @ConcurrencyLimit para limitar llamadas concurrentes
     */
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public String[] getCategoriesWithLimit() {
        return resilientQuoteService.getCategoriesWithConcurrencyLimit();
    }

    /**
     * Endpoint que combina ambas anotaciones de resiliencia
     */
    @GetMapping(value = "/random-resilient", produces = MediaType.APPLICATION_JSON_VALUE)
    public QuoteClient.ChuckNorrisJoke getRandomJokeResilient() {
        return resilientQuoteService.getRandomJokeResilient();
    }
}

