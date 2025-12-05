package com.josediaz.springframework7.client;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Cliente HTTP declarativo usando @HttpExchange - Nueva característica de Spring Framework 7
 * 
 * Similar a Feign pero más ligero y completamente integrado con Spring Framework 7
 * 
 * Este cliente consume la API pública de Chuck Norris: https://api.chucknorris.io/
 * 
 * Demuestra el uso de anotaciones de resiliencia (@Retryable, @ConcurrencyLimit)
 * que son nuevas características de Spring Framework 7
 */
@HttpExchange
public interface QuoteClient {

    /**
     * Obtiene un chiste aleatorio de Chuck Norris
     * 
     * Con anotaciones de resiliencia:
     * - @Retryable: Reintenta automáticamente si falla la llamada
     * - @ConcurrencyLimit(3): Limita a 3 llamadas concurrentes
     * 
     * @return Respuesta completa con el chiste
     */
    @GetExchange("/jokes/random")
    @Retryable
    @org.springframework.resilience.annotation.ConcurrencyLimit(3)
    ChuckNorrisJoke getRandomJoke();

    /**
     * Obtiene un chiste aleatorio de una categoría específica
     * @param category La categoría del chiste
     * @return Respuesta completa con el chiste
     */
    @GetExchange("/jokes/random")
    ChuckNorrisJoke getRandomJokeByCategory(@org.springframework.web.bind.annotation.RequestParam("category") String category);

    /**
     * Obtiene la lista de categorías disponibles
     * @return Array de categorías
     */
    @GetExchange("/jokes/categories")
    String[] getCategories();

    /**
     * Clase de respuesta para los chistes de Chuck Norris
     * Basada en la estructura de la API: https://api.chucknorris.io/
     */
    class ChuckNorrisJoke {
        private String icon_url;
        private String id;
        private String url;
        private String value;

        public ChuckNorrisJoke() {
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

