package com.josediaz.springframework7.config;

import com.josediaz.springframework7.client.QuoteClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuración para clientes HTTP declarativos - Nueva característica de Spring Framework 7
 * 
 * Esta configuración permite usar clientes HTTP declarativos de manera similar a Feign
 * pero más ligero y completamente integrado con Spring Framework 7
 * 
 * Usa la API pública de Chuck Norris: https://api.chucknorris.io/
 * 
 * @EnableResilientMethods habilita las anotaciones de resiliencia (@Retryable, @ConcurrencyLimit)
 * que son nuevas características de Spring Framework 7
 */
@Configuration
@EnableResilientMethods
public class HttpClientConfig {

    /**
     * Bean del cliente HTTP declarativo QuoteClient
     * 
     * Crea un proxy del cliente HTTP usando RestClient y HttpServiceProxyFactory
     */
    @Bean
    public QuoteClient quoteClient(@Value("${chucknorris.api.base-url:https://api.chucknorris.io}") String baseUrl) {
        // Crear RestClient con URL base
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        
        // Crear HttpServiceProxyFactory usando RestClientAdapter
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build();
        
        return factory.createClient(QuoteClient.class);
    }
}
