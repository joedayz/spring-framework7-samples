package com.josediaz.springframework7.controller;

import com.josediaz.springframework7.client.QuoteClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que demuestra el uso de @HttpServiceClient con versionado
 * 
 * Similar al ejemplo del artículo, usando @HttpServiceClient para obtener
 * un saludo aleatorio de un servicio externo
 */
@RestController
@RequestMapping(path = "/hello", version = "4")
public class HelloV4Controller {

    private final QuoteClient quoteClient;

    public HelloV4Controller(QuoteClient quoteClient) {
        this.quoteClient = quoteClient;
    }

    /**
     * Versión 4 del endpoint hello que usa @HttpServiceClient
     * para obtener un chiste aleatorio de Chuck Norris
     */
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHello() {
        // Usar el cliente HTTP declarativo para obtener un chiste aleatorio de Chuck Norris
        QuoteClient.ChuckNorrisJoke joke = this.quoteClient.getRandomJoke();
        return joke != null ? joke.getValue() : "Hello, World!";
    }
}

