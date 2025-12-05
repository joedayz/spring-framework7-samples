package com.josediaz.springframework7.listener;

import com.josediaz.springframework7.event.HelloWorldEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener asíncrono que demuestra el uso de múltiples TaskDecorator beans
 * 
 * Los TaskDecorator se aplican automáticamente a las tareas asíncronas
 * en el orden especificado por @Order
 */
@Component
public class HelloWorldEventLogger {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldEventLogger.class);

    /**
     * Escucha eventos HelloWorldEvent de forma asíncrona
     * Los TaskDecorator se aplicarán automáticamente a esta tarea
     */
    @Async
    @EventListener
    public void logHelloWorldEvent(HelloWorldEvent event) {
        log.info("Hello World Event: {}", event.message());
    }
}

