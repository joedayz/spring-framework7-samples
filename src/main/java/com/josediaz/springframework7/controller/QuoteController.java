package com.josediaz.springframework7.controller;

import com.josediaz.springframework7.client.QuoteClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que demuestra el uso de @HttpServiceClient
 * 
 * Este controlador usa un cliente HTTP declarativo para obtener chistes de Chuck Norris
 * desde la API pública: https://api.chucknorris.io/
 */
@RestController
@RequestMapping("/quotes")
public class QuoteController {

    private final QuoteClient quoteClient;

    public QuoteController(QuoteClient quoteClient) {
        this.quoteClient = quoteClient;
    }

    /**
     * Obtiene un chiste aleatorio de Chuck Norris usando el cliente HTTP declarativo
     * Retorna solo el texto del chiste por defecto
     */
    @GetMapping(value = "/random", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getRandomQuote() {
        QuoteClient.ChuckNorrisJoke joke = quoteClient.getRandomJoke();
        return joke != null && joke.getValue() != null ? joke.getValue() : "No joke available";
    }

    /**
     * Obtiene un chiste aleatorio de Chuck Norris en formato JSON completo
     */
    @GetMapping(value = "/random/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public QuoteClient.ChuckNorrisJoke getRandomQuoteJson() {
        try {
            return quoteClient.getRandomJoke();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching joke: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene un chiste aleatorio de una categoría específica
     */
    @GetMapping(value = "/random/category", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getRandomQuoteByCategory(@RequestParam String category) {
        QuoteClient.ChuckNorrisJoke joke = quoteClient.getRandomJokeByCategory(category);
        return joke != null ? joke.getValue() : "No joke available for category: " + category;
    }

    /**
     * Obtiene la lista de categorías disponibles
     */
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public String[] getCategories() {
        return quoteClient.getCategories();
    }

    /**
     * Endpoint de prueba para verificar que el controlador funciona
     */
    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        return "QuoteController is working";
    }
}

