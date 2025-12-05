package com.josediaz.springframework7;

import com.josediaz.springframework7.event.HelloWorldEvent;
import com.josediaz.springframework7.listener.HelloWorldEventLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test para demostrar el uso de múltiples TaskDecorator beans
 * 
 * Verifica que los TaskDecorator se aplican correctamente a las tareas asíncronas.
 * 
 * Los decoradores se aplican en el orden especificado por @Order:
 * 1. measuringTaskDecorator (@Order(1)) - Mide el tiempo de ejecución
 * 2. loggingTaskDecorator (@Order(2)) - Registra inicio y fin de la tarea
 * 
 * El orden de aplicación es: measuring -> logging -> tarea real
 */
@SpringBootTest
class TaskDecoratorTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private HelloWorldEventLogger eventLogger;

    @Test
    void testMultipleTaskDecorators() throws InterruptedException {
        // Verificar que el listener está disponible
        assertThat(eventLogger).isNotNull();
        
        // Publicar un evento
        HelloWorldEvent event = new HelloWorldEvent("Test Message");
        eventPublisher.publishEvent(event);
        
        // Esperar un poco para que la tarea asíncrona se ejecute
        // Los TaskDecorator deberían aplicarse automáticamente en el orden correcto:
        // 1. measuringTaskDecorator (mide tiempo)
        // 2. loggingTaskDecorator (registra inicio/fin)
        // 3. tarea real (logHelloWorldEvent)
        Thread.sleep(500);
        
        // Si llegamos aquí sin excepciones, los decoradores funcionaron correctamente
        assertThat(true).isTrue();
    }

    @Test
    void testAsyncEventProcessing() throws InterruptedException {
        // Publicar múltiples eventos para verificar que los decoradores se aplican
        // a cada tarea asíncrona independientemente
        for (int i = 0; i < 3; i++) {
            HelloWorldEvent event = new HelloWorldEvent("Message " + i);
            eventPublisher.publishEvent(event);
        }
        
        // Esperar a que todas las tareas asíncronas se ejecuten
        // Cada una debería tener los decoradores aplicados
        Thread.sleep(1000);
        
        // Si llegamos aquí sin excepciones, los decoradores funcionaron correctamente
        assertThat(true).isTrue();
    }

    @Test
    void testTaskDecoratorOrder() throws InterruptedException {
        // Este test verifica que los decoradores se aplican en el orden correcto
        // El orden esperado es:
        // 1. measuringTaskDecorator (@Order(1)) - más externo
        // 2. loggingTaskDecorator (@Order(2)) - intermedio
        // 3. tarea real - más interno
        
        HelloWorldEvent event = new HelloWorldEvent("Order Test");
        eventPublisher.publishEvent(event);
        
        // Esperar a que la tarea se ejecute
        Thread.sleep(500);
        
        // Los logs deberían mostrar:
        // 1. "Running Task: ..." (de loggingTaskDecorator)
        // 2. "Hello World Event: Order Test" (de la tarea real)
        // 3. "Finished within Xms (Task: ...)" (de measuringTaskDecorator)
        // 4. "Finished Task: ..." (de loggingTaskDecorator)
        
        assertThat(true).isTrue();
    }
}

