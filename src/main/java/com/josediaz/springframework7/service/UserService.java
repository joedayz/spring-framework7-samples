package com.josediaz.springframework7.service;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio que demuestra el uso de JSpecify para null safety
 * 
 * Spring Framework 7 adopta JSpecify como estándar para anotaciones de nullabilidad.
 * Esto mejora:
 * - Herramientas del IDE (detección de posibles NullPointerException)
 * - Interoperabilidad con Kotlin
 * - Seguridad de tipos en tiempo de compilación
 */
@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    public UserService() {
        // Datos de ejemplo
        users.put(1L, new User(1L, "Alice", "alice@example.com", "123-456-7890"));
        users.put(2L, new User(2L, "Bob", "bob@example.com", null));
        users.put(3L, new User(3L, "Charlie", "charlie@example.com", "987-654-3210"));
    }

    /**
     * Retorna todos los usuarios
     * 
     * @return Lista no nula de usuarios (puede estar vacía pero nunca null)
     */
    public @NonNull Map<Long, User> getAllUsers() {
        return new HashMap<>(users);
    }

    /**
     * Busca un usuario por ID
     * 
     * @param id ID del usuario (no puede ser null)
     * @return Usuario si existe, null si no existe
     */
    public @Nullable User getUserById(@NonNull Long id) {
        return users.get(id);
    }

    /**
     * Crea un nuevo usuario
     * 
     * @param name Nombre del usuario (requerido, no null)
     * @param email Email del usuario (requerido, no null)
     * @param phone Teléfono del usuario (opcional, puede ser null)
     * @return Usuario creado (nunca null)
     */
    public @NonNull User createUser(@NonNull String name, @NonNull String email, @Nullable String phone) {
        User user = new User(nextId++, name, email, phone);
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Actualiza un usuario existente
     * 
     * @param id ID del usuario (requerido, no null)
     * @param name Nuevo nombre (requerido, no null)
     * @param email Nuevo email (requerido, no null)
     * @param phone Nuevo teléfono (opcional, puede ser null)
     * @return Usuario actualizado si existe, null si no existe
     */
    public @Nullable User updateUser(@NonNull Long id, @NonNull String name, @NonNull String email, @Nullable String phone) {
        User user = users.get(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
        }
        return user;
    }

    /**
     * Elimina un usuario
     * 
     * @param id ID del usuario (requerido, no null)
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteUser(@NonNull Long id) {
        return users.remove(id) != null;
    }

    /**
     * Busca un usuario por email
     * 
     * @param email Email del usuario (requerido, no null)
     * @return Usuario si existe, null si no existe
     */
    public @Nullable User findByEmail(@NonNull String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Clase interna que representa un Usuario
     * Demuestra el uso de JSpecify en campos y métodos
     */
    public static class User {
        private final Long id;
        private @NonNull String name;
        private @NonNull String email;
        private @Nullable String phone;

        public User(Long id, @NonNull String name, @NonNull String email, @Nullable String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public Long getId() {
            return id;
        }

        public @NonNull String getName() {
            return name;
        }

        public void setName(@NonNull String name) {
            this.name = name;
        }

        public @NonNull String getEmail() {
            return email;
        }

        public void setEmail(@NonNull String email) {
            this.email = email;
        }

        public @Nullable String getPhone() {
            return phone;
        }

        public void setPhone(@Nullable String phone) {
            this.phone = phone;
        }

        /**
         * Retorna el teléfono o un valor por defecto si es null
         * 
         * @param defaultValue Valor por defecto si phone es null
         * @return Teléfono o valor por defecto (nunca null)
         */
        public @NonNull String getPhoneOrDefault(@NonNull String defaultValue) {
            return phone != null ? phone : defaultValue;
        }
    }
}

