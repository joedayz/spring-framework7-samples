package com.josediaz.springframework7.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;

/**
 * Configuración para habilitar el versionado de API
 * Usa Request Header como estrategia
 * 
 * NOTA: El versionado solo se aplica a endpoints que especifican el atributo 'version'
 * Los endpoints sin 'version' funcionan normalmente sin requerir el header
 */
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        // Usar header X-API-Version para determinar la versión
        // NOTA: Los endpoints con atributo 'version' requieren este header
        // Los endpoints sin 'version' deberían funcionar sin el header
        configurer.useRequestHeader("X-API-Version");
    }
}

