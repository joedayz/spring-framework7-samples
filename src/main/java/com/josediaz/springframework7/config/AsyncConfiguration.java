package com.josediaz.springframework7.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuración para habilitar el procesamiento asíncrono
 * 
 * @EnableAsync habilita el uso de @Async en métodos
 * Los TaskDecorator definidos en TaskDecoratorConfiguration
 * se aplicarán automáticamente a todas las tareas asíncronas
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {
}

