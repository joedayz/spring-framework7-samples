package com.josediaz.springframework7.controller;

import com.josediaz.springframework7.service.UserService;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador que demuestra el uso de JSpecify para null safety
 * 
 * Las anotaciones @NonNull y @Nullable mejoran:
 * - Detección de errores en tiempo de compilación
 * - Documentación del código
 * - Herramientas del IDE
 * - Interoperabilidad con Kotlin
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final @NonNull UserService userService;

    public UserController(@NonNull UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene todos los usuarios
     * 
     * @return Lista de usuarios (nunca null)
     */
    @GetMapping
    public @NonNull ResponseEntity<Map<Long, UserService.User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Obtiene un usuario por ID
     * 
     * @param id ID del usuario (no puede ser null)
     * @return Usuario si existe, 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserService.User> getUserById(@PathVariable @NonNull Long id) {
        UserService.User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea un nuevo usuario
     * 
     * @param name Nombre (requerido)
     * @param email Email (requerido)
     * @param phone Teléfono (opcional)
     * @return Usuario creado
     */
    @PostMapping
    public @NonNull ResponseEntity<UserService.User> createUser(
            @RequestParam @NonNull String name,
            @RequestParam @NonNull String email,
            @RequestParam(required = false) @Nullable String phone) {
        UserService.User user = userService.createUser(name, email, phone);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Actualiza un usuario existente
     * 
     * @param id ID del usuario
     * @param name Nuevo nombre
     * @param email Nuevo email
     * @param phone Nuevo teléfono (opcional)
     * @return Usuario actualizado o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserService.User> updateUser(
            @PathVariable @NonNull Long id,
            @RequestParam @NonNull String name,
            @RequestParam @NonNull String email,
            @RequestParam(required = false) @Nullable String phone) {
        UserService.User user = userService.updateUser(id, name, email, phone);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina un usuario
     * 
     * @param id ID del usuario
     * @return 204 si se eliminó, 404 si no existía
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Busca un usuario por email
     * 
     * @param email Email del usuario
     * @return Usuario si existe, 404 si no existe
     */
    @GetMapping("/search")
    public ResponseEntity<UserService.User> findByEmail(@RequestParam @NonNull String email) {
        UserService.User user = userService.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}

