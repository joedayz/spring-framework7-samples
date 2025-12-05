package com.josediaz.springframework7.controller;

import com.josediaz.springframework7.event.HelloWorldEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para publicar eventos que demuestran el uso de múltiples TaskDecorator
 * 
 * Cuando se publica un evento, el listener asíncrono lo procesa
 * y los TaskDecorator se aplican automáticamente
 */
@RestController
@RequestMapping("/events")
public class EventController {

    private final ApplicationEventPublisher eventPublisher;

    public EventController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Publica un evento HelloWorldEvent
     * El listener asíncrono lo procesará y los TaskDecorator se aplicarán
     */
    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String publishHelloWorldEvent(@RequestParam(defaultValue = "Happy Spring Framework 7!") String message) {
        HelloWorldEvent event = new HelloWorldEvent(message);
        eventPublisher.publishEvent(event);
        return "Event published: " + message;
    }
}

