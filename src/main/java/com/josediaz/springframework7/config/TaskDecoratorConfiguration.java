package com.josediaz.springframework7.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskDecorator;

/**
 * Configuración que demuestra el uso de múltiples TaskDecorator beans
 * 
 * Nueva característica de Spring Framework 7:
 * - Puedes definir múltiples TaskDecorator beans
 * - Spring los compone automáticamente en una cadena
 * - Se aplican en el orden especificado por @Order
 * 
 * Esto elimina la necesidad de crear decoradores compuestos manualmente
 */
@Configuration
public class TaskDecoratorConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TaskDecoratorConfiguration.class);

    /**
     * TaskDecorator para logging
     * 
     * @Order(2): Se aplica después del decorador de medición
     * Registra el inicio y fin de cada tarea asíncrona
     */
    @Bean
    @Order(2)
    public TaskDecorator loggingTaskDecorator() {
        return runnable -> () -> {
            log.info("Running Task: {}", runnable);
            try {
                runnable.run();
            } finally {
                log.info("Finished Task: {}", runnable);
            }
        };
    }

    /**
     * TaskDecorator para medición de tiempo
     * 
     * @Order(1): Se aplica primero (más externo en la cadena)
     * Mide el tiempo de ejecución de cada tarea asíncrona
     */
    @Bean
    @Order(1)
    public TaskDecorator measuringTaskDecorator() {
        return runnable -> () -> {
            final var startTime = System.currentTimeMillis();
            try {
                runnable.run();
            } finally {
                final var endTime = System.currentTimeMillis();
                log.info("Finished within {}ms (Task: {})", endTime - startTime, runnable);
            }
        };
    }
}

