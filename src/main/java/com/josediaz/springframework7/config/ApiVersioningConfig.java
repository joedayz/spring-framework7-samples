package com.josediaz.springframework7.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;

/**
 * Configuración para habilitar el versionado de API
 * Usa Request Header como estrategia
 * 
 * Demuestra el uso de JSpecify @NonNull para null safety
 * Spring Framework 7 adopta JSpecify como estándar para anotaciones de nullabilidad
 * 
 * NOTA: El versionado solo se aplica a endpoints que especifican el atributo 'version'
 * Los endpoints sin 'version' funcionan normalmente sin requerir el header
 */
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    /**
     * Configura el versionado de API usando JSpecify @NonNull
     * 
     * @NonNull garantiza que configurer nunca será null,
     * mejorando la seguridad de tipos y la interoperabilidad con Kotlin
     */
    @Override
    public void configureApiVersioning(@NonNull ApiVersionConfigurer configurer) {
        // Usar header X-API-Version para determinar la versión
        // NOTA: Los endpoints con atributo 'version' requieren este header
        // Los endpoints sin 'version' deberían funcionar sin el header
        configurer.useRequestHeader("X-API-Version");
    }
}

